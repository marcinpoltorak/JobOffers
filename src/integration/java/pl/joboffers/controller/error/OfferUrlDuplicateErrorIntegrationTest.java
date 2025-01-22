package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
        registry.add("offers.http.client.config.uri", () -> WIRE_MOCK_HOST);
        registry.add("offers.http.client.config.port", () -> wireMockServer.getPort());
    }

    @Test
    @WithMockUser
    public void should_return_409_conflict_when_added_second_offer_with_same_offer_url() throws Exception {
        // step 1
        // given && when
        ResultActions performSave = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "company": "Some Random Company",
                        "position": "position",
                        "salary": "4 000 - 8 000 PLN",
                        "offerUrl": "https://myjoboffer.net"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
        );
        // then
        performSave.andExpect(status().isCreated());

        // step 2
        // given && when
        ResultActions performSave2 = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "company": "Some Random Company",
                        "position": "position",
                        "salary": "4 000 - 8 000 PLN",
                        "offerUrl": "https://myjoboffer.net"
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON + ";charset=UTF-8")
        );
        //then
        performSave2.andExpect(status().isConflict());
    }
}
