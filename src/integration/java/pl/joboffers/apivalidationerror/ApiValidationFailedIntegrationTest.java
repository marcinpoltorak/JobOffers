package pl.joboffers.apivalidationerror;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.joboffers.BaseIntegrationTest;
import pl.joboffers.infrastructure.apivalidation.ApiValidationErrorDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ApiValidationFailedIntegrationTest extends BaseIntegrationTest {

    @Test
    public void should_return_400_bad_request_and_validation_message_when_input_content_is_empty() throws Exception {
        // given & when
        ResultActions performSave = mockMvc.perform(post("/offers")
                .content("""
                        {}
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult mvcResult = performSave.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto errorDto = objectMapper.readValue(json, ApiValidationErrorDto.class);
        List<String> errors = errorDto.messages();
        assertThat(errors).containsExactlyInAnyOrder(
            "company must not be null",
            "position must not be null",
            "salary must not be null",
            "offerUrl must not be null",
            "company must not be empty",
            "position must not be empty",
            "salary must not be empty",
            "offerUrl must not be empty"
        );
    }

    @Test
    public void should_return_400_bad_request_and_validation_message_when_input_has_empty_objects() throws Exception {
        // given & when
        ResultActions performSave = mockMvc.perform(post("/offers")
                .content("""
                        {
                        "company": "",
                        "position": "",
                        "salary": "",
                        "offerUrl": ""
                        }
                        """.trim())
                .contentType(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult mvcResult = performSave.andExpect(status().isBadRequest()).andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ApiValidationErrorDto errorDto = objectMapper.readValue(json, ApiValidationErrorDto.class);
        List<String> errors = errorDto.messages();
        assertThat(errors).containsExactlyInAnyOrder(
            "company must not be empty",
            "position must not be empty",
            "salary must not be empty",
            "offerUrl must not be empty"
        );
    }
}
