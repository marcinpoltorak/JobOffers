package pl.joboffers.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OfferUrlDuplicateErrorIntegrationTest extends BaseIntegrationTest {

    @Test
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
