package goodbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import goodbank.AbstractTest;
import goodbank.dto.AnnuityLoanDto;
import goodbank.dto.AnnuityLoanRequest;
import goodbank.dto.AvailableLoan;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testBasicScenario() throws Exception {
        MvcResult res1  = mockMvc
                .perform(MockMvcRequestBuilders.get("/loans/available"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        AvailableLoan loan = mapper.readValue(res1.getResponse().getContentAsString(), AvailableLoan.class);
        AnnuityLoanRequest request =
                new AnnuityLoanRequest(INTEREST_RATE, loan.getPossibleLoanTerms().get(0), loan.getMinLoanSum());
        MvcResult res2  = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/calculator/loans/annuity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        AnnuityLoanDto loanDto = mapper.readValue(res2.getResponse().getContentAsString(), AnnuityLoanDto.class);
        Assert.assertNotNull(loanDto);
        Assert.assertNotNull(loanDto.getSchedule());
        Assert.assertEquals(loanDto.getRequestedLoan(), request);
        assertScheduleIsCorrect(loanDto.getSchedule());
    }

    @Test
    public void testIncorrectTerm() throws Exception {
        AnnuityLoanRequest request =
                new AnnuityLoanRequest(1600, -1, 222222L);
        MvcResult res2  = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/calculator/loans/annuity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        Assert.assertTrue(res2.getResponse().getContentAsString().contains("code"));
    }

    @Test
    public void testIncorrectLoan() throws Exception {
        AnnuityLoanRequest request =
                new AnnuityLoanRequest(1600, 24, 2L);
        MvcResult res2  = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/calculator/loans/annuity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        Assert.assertTrue(res2.getResponse().getContentAsString().contains("code"));
    }

    @Test
    public void testIncorrectRate() throws Exception {
        AnnuityLoanRequest request =
                new AnnuityLoanRequest(10, 24, 222222L);
        MvcResult res2  = mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/calculator/loans/annuity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        Assert.assertTrue(res2.getResponse().getContentAsString().contains("code"));
    }
}
