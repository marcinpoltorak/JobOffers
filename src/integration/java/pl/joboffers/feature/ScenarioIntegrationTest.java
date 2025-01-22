package pl.joboffers.feature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.SampleJobOfferResponse;
import pl.joboffers.domain.offer.dto.OfferResponseDto;
import pl.joboffers.infrastructure.loginandregister.controller.dto.JwtResponseDto;
import pl.joboffers.infrastructure.offer.scheduler.HttpOffersScheduler;

import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ScenarioIntegrationTest extends BaseIntegrationTest implements SampleJobOfferResponse {

    @Autowired
    HttpOffersScheduler offersScheduler;

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offers.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offers.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Test
    public void user_want_to_see_offers() throws Exception {
        // step 1: there are no offers in external HTTP server (http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers)
        // given & when & then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffersJson())));
        // step 2: scheduler ran 1st time and made GET to external server and system added 0 offers to database
        // given & when
        List<OfferResponseDto> savedOffers = offersScheduler.fetchAllOffersAndSaveAllIfNotExist();
        // then
        assertThat(savedOffers).isEmpty();
        // step 3: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        failedLoginRequest
                .andExpect(status().isUnauthorized())
                .andExpect(content().json("""
                        {
                        "message": "Bad Credentials",
                        "status": "UNAUTHORIZED"
                        }
                        """.trim()));

        // step 4: user made GET /offers with no jwt token and system returned UNAUTHORIZED(401)
        // given & when
        ResultActions failedGetOffersRequest = mockMvc.perform(get("/offers")
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        failedGetOffersRequest.andExpect(status().isForbidden());
        // step 5: user made POST /register with username=someUser, password=somePassword and system registered user with status CREATED(201)
        // given & when
        ResultActions registerAction = mockMvc.perform(post("/register")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        registerAction.andExpect(status().isCreated());

        // step 6: user tried to get JWT token by requesting POST /token with username=someUser, password=somePassword and system returned OK(200) and jwttoken=AAAA.BBBB.CCC
        // given & when
        ResultActions successLoginRequest = mockMvc.perform(post("/token")
                .content("""
                        {
                        "username": "someUser",
                        "password": "somePassword"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        MvcResult mvcSuccessLoginResult = successLoginRequest.andExpect(status().isOk()).andReturn();
        String successLoginJson = mvcSuccessLoginResult.getResponse().getContentAsString();
        JwtResponseDto jwtResponseDto = objectMapper.readValue(successLoginJson, JwtResponseDto.class);
        String token = jwtResponseDto.token();
        assertAll(
                () -> assertThat(jwtResponseDto.username()).isEqualTo("someUser"),
                () -> assertThat(token).matches(Pattern.compile("^([A-Za-z0-9-_=]+\\.)+([A-Za-z0-9-_=])+\\.?$"))
        );

        // step 7: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 0 offers
        // when
        ResultActions perform = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        );
        // then
        MvcResult mvcResult = perform.andExpect(status().isOk()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> offers = objectMapper.readValue(json, new TypeReference<>(){});
        assertThat(offers).isEmpty();


        // step 8: there are 2 new offers in external HTTP server
        // given & when & then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffersJson())));
        // step 9: scheduler ran 2nd time and made GET to external server and system added 2 new offers with ids: 1000 and 2000 to database
        // given & when
        List<OfferResponseDto> fetchedAndSavedOffers = offersScheduler.fetchAllOffersAndSaveAllIfNotExist();
        // then
        assertThat(fetchedAndSavedOffers).hasSize(2);


        // step 10: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 2 offers with ids: 1000 and 2000
        // given & when
        ResultActions performGetOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult getOffersMvcResult = performGetOffers.andExpect(status().isOk()).andReturn();
        String jsonWithTwoOffers = getOffersMvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> offerResponseDtoList = objectMapper.readValue(jsonWithTwoOffers, new TypeReference<>() {});
        assertThat(offerResponseDtoList).hasSize(2);
        OfferResponseDto expectedFirstOffer = fetchedAndSavedOffers.get(0);
        OfferResponseDto expectedSecondOffer = fetchedAndSavedOffers.get(1);
        assertThat(offerResponseDtoList).containsExactlyInAnyOrder(
                new OfferResponseDto(expectedFirstOffer.id(), expectedFirstOffer.company(), expectedFirstOffer.position(), expectedFirstOffer.salary(), expectedFirstOffer.offerUrl()),
                new OfferResponseDto(expectedSecondOffer.id(), expectedSecondOffer.company(), expectedSecondOffer.position(), expectedSecondOffer.salary(), expectedSecondOffer.offerUrl())
        );

        // step 11: user made GET /offers/9999 and system returned NOT_FOUND(404) with message “Offer with id 9999 not found”
        // when
        ResultActions performGetWithNotExistingId = mockMvc.perform(get("/offers/9999")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        performGetWithNotExistingId.andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                        "message": "Offer with id 9999 not found",
                        "status": "NOT_FOUND"
                        }
                        """.trim()
                ));


        // step 12: user made GET /offers/1000 and system returned OK(200) with offer
        // given
        String offerIdAddedInDatabase = expectedFirstOffer.id();
        // when
        ResultActions performGetWithExistingId = mockMvc.perform(get("/offers/"+offerIdAddedInDatabase)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult existingOfferMvcResult = performGetWithExistingId.andExpect(status().isOk()).andReturn();
        String existingOfferJson = existingOfferMvcResult.getResponse().getContentAsString();
        OfferResponseDto existingOffers = objectMapper.readValue(existingOfferJson, new TypeReference<>() {});
        assertThat(existingOffers).isNotNull();
        // step 13: there are 2 new offers in external HTTP server
        // given & when & then
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithFourOffersJson())));
        // step 14: scheduler ran 3rd time and made GET to external server and system added 2 new offers with ids: 3000 and 4000 to database
        // given & when
        List<OfferResponseDto> twoNewOffers = offersScheduler.fetchAllOffersAndSaveAllIfNotExist();
        // then
        assertThat(twoNewOffers).hasSize(2);
        // step 15: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 4 offers with ids: 1000,2000, 3000 and 4000
        // given & when
        ResultActions performGetWithTwoNewOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult mvcResultWithTwoNewOffers = performGetWithTwoNewOffers.andExpect(status().isOk()).andReturn();
        String jsonWithTwoNewOffers = mvcResultWithTwoNewOffers.getResponse().getContentAsString();
        List<OfferResponseDto> fourOffers = objectMapper.readValue(jsonWithTwoNewOffers, new TypeReference<>() {});
        assertThat(fourOffers).hasSize(4);
        OfferResponseDto expectedThirdOffer = twoNewOffers.get(0);
        OfferResponseDto expectedFourthOffer = twoNewOffers.get(1);
        assertThat(fourOffers).contains(
                new OfferResponseDto(expectedThirdOffer.id(), expectedThirdOffer.company(), expectedThirdOffer.position(), expectedThirdOffer.salary(), expectedThirdOffer.offerUrl()),
                new OfferResponseDto(expectedFourthOffer.id(), expectedFourthOffer.company(), expectedFourthOffer.position(), expectedFourthOffer.salary(), expectedFourthOffer.offerUrl())
        );


        // step 16: user made POST /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and offer and system returned CREATED(201) with saved offer
        // given & when
        ResultActions performSave = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "company": "Some Random Company",
                        "position": "position",
                        "salary": "4 000 - 8 000 PLN",
                        "offerUrl": "https://myjoboffer.net"
                        }
                        """.trim())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
        );
        // then
        MvcResult mvcSaveResult = performSave.andExpect(status().isCreated()).andReturn();
        String saveResponseJson = mvcSaveResult.getResponse().getContentAsString();
        OfferResponseDto offerResponse = objectMapper.readValue(saveResponseJson, OfferResponseDto.class);
        String id = offerResponse.id();
        assertAll(
                () -> assertThat(offerResponse.company()).isEqualTo("Some Random Company"),
                () -> assertThat(offerResponse.offerUrl()).isEqualTo("https://myjoboffer.net"),
                () -> assertThat(offerResponse.position()).isEqualTo("position"),
                () -> assertThat(offerResponse.salary()).isEqualTo("4 000 - 8 000 PLN"),
                () -> assertThat(id).isNotNull()
        );


        // step 17: user made GET /offers with header “Authorization: Bearer AAAA.BBBB.CCC” and system returned OK(200) with 1 offer
        // given & when
        ResultActions performOffers = mockMvc.perform(get("/offers")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON_VALUE));
        // then
        MvcResult offersMvcResult = performOffers.andExpect(status().isOk()).andReturn();
        String offersJson = offersMvcResult.getResponse().getContentAsString();
        List<OfferResponseDto> offerList = objectMapper.readValue(offersJson, new TypeReference<>() {});
        assertThat(offerList).hasSize(5);
        assertThat(offerList.stream().map(OfferResponseDto::id)).contains(id);
    }
}
