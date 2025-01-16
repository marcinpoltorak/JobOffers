package pl.joboffers.http.error;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.domain.offer.OfferFetchable;
import wiremock.org.apache.hc.core5.http.HttpStatus;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class OfferRestTemplateErrorsIntegrationTest {

    @RegisterExtension
    public static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    OfferFetchable offerFetchable = new OfferRestTemplateIntegrationTestConfig().remoteOfferClient(wireMockServer.getPort());

    @Test
    public void should_throw_500_internal_server_error_when_fault_connection_reset_by_peer(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.CONNECTION_RESET_BY_PEER)));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_throw_500_internal_server_error_when_fault_empty_response(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.EMPTY_RESPONSE)));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_throw_500_internal_server_error_when_fault_malformed_response_chunk(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_throw_500_internal_server_error_when_random_data_then_close(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_OK)
                        .withHeader("Content-Type", "application/json")
                        .withFault(Fault.RANDOM_DATA_THEN_CLOSE)));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_throw_204_no_content_when_status_is_204_no_content(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_NO_CONTENT)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                    "title": "Junior Java Developer",
                                    "company": "Fair Place Finance S.A.",
                                    "salary": "6 000 - 9 000 PLN",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                                }]
                                """.trim())));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("204 NO_CONTENT");
    }

    @Test
    public void should_throw_500_internal_server_error_when_response_delay_is_5000ms_and_client_has_1000ms_read_timeout(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_NO_CONTENT)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [{
                                    "title": "Junior Java Developer",
                                    "company": "Fair Place Finance S.A.",
                                    "salary": "6 000 - 9 000 PLN",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-fair-place-finance-remote-kxvnnhb1"
                                }]
                                """.trim())
                        .withFixedDelay(5000)));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("500 INTERNAL_SERVER_ERROR");
    }

    @Test
    public void should_throw_404_not_found_when_http_service_returning_not_found_status(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_NOT_FOUND)
                        .withHeader("Content-Type", "application/json")));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("404 NOT_FOUND");
    }

    @Test
    public void should_throw_401_unauthorized_when_http_service_returning_unauthorized(){
        // given
        wireMockServer.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.SC_UNAUTHORIZED)
                        .withHeader("Content-Type", "application/json")));

        // when
        Throwable throwable = catchThrowable(()-> offerFetchable.fetchOffers());
        // then
        assertThat(throwable).isInstanceOf(ResponseStatusException.class);
        assertThat(throwable.getMessage()).isEqualTo("401 UNAUTHORIZED");
    }
}
