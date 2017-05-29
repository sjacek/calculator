package pl.sjacek.calculator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.sjacek.calculator.dto.CalculateDTO;
import pl.sjacek.calculator.repositories.CalculationRepository;
import pl.sjacek.calculator.service.CalculationService;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jacek on 27.05.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ContextConfiguration(classes = {
        CalculationRepository.class,
        CalculationService.class,
        CalculatorController.class
})
@ActiveProfiles("test")
@Ignore
public class CalculatorControllerITest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void shouldHaveMessageInResponse() throws Exception {
        CalculateDTO dto = CalculateDTO.builder()
                .expression("2+2-5")
                .build();
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        json = mapper.writeValueAsString(dto);
        mockMvc.perform(post("/v1/calculator/calculate")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
