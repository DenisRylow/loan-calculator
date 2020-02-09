package goodbank.service;

import goodbank.AbstractTest;
import goodbank.dto.AnnuityLoanDto;
import goodbank.dto.AnnuityLoanRequest;
import goodbank.dto.AvailableLoan;
import goodbank.dto.MonthlyLoanStatus;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class LoanServiceTest extends AbstractTest {

    @Autowired
    private LoanService loanService;

    @Test
    public void testLoanTerms() {
        AvailableLoan availableLoan = loanService.getAvailableLoan();
        List<Integer> expected = Arrays.asList(12, 24, 36, 48, 60);
        Assert.assertEquals(expected, availableLoan.getPossibleLoanTerms());
    }

    @Test
    public void testInterestRate() {
        AvailableLoan availableLoan = loanService.getAvailableLoan();
        Assert.assertEquals(Integer.valueOf(INTEREST_RATE), availableLoan.getDefaultLoanInterestRate());
    }

    @Test
    public void testMinLoan() {
        AvailableLoan availableLoan = loanService.getAvailableLoan();
        Assert.assertEquals(100000, availableLoan.getMinLoanSum().intValue());
    }

    @Test
    public void testMaxLoan() {
        AvailableLoan availableLoan = loanService.getAvailableLoan();
        Assert.assertEquals(5000000, availableLoan.getMaxLoanSum().intValue());
    }

    @Test
    public void testCalculatePayment() {
        Long loan = 123450L;
        BigDecimal payment = loanService.calculateMonthlyPayment(loan, INTEREST_RATE, 12);
        // Expected value obtained from website https://www.ipotek.ru/calc1n/results.php?matr=1.
        Assert.assertEquals(new BigDecimal("11435.73"), payment);
    }

    @Test
    public void testInterest() {
        Long loan = 1200L;
        BigDecimal payment = loanService.calculateInterest(new BigDecimal(loan), INTEREST_RATE);
        Assert.assertEquals(new BigDecimal(20).setScale(2), payment);
    }

    @Test
    public void testCalculateSchedule() {
        Long loan = 123450L;
        BigDecimal payment = loanService.calculateMonthlyPayment(loan, INTEREST_RATE, 12);
        AnnuityLoanRequest request = new AnnuityLoanRequest(INTEREST_RATE, 12, loan);
        AnnuityLoanDto annuityLoanDto = loanService.calculateLoan(request);
        assertTotalPaymentEquals(annuityLoanDto.getSchedule(), payment);
        assertScheduleIsCorrect(annuityLoanDto.getSchedule());
    }

    @Test
    public void testCalculateTwoYearSchedule() {
        Long loan = 123450L;
        BigDecimal payment = loanService.calculateMonthlyPayment(loan, INTEREST_RATE, 24);
        AnnuityLoanRequest request = new AnnuityLoanRequest(INTEREST_RATE, 24, loan);
        AnnuityLoanDto annuityLoanDto = loanService.calculateLoan(request);
        assertTotalPaymentEquals(annuityLoanDto.getSchedule(), payment);
        assertScheduleIsCorrect(annuityLoanDto.getSchedule());
    }

    private void assertTotalPaymentEquals(List<MonthlyLoanStatus> schedule, BigDecimal payment) {
        for (MonthlyLoanStatus status : schedule) {
            Assert.assertEquals(payment, status.getTotalPayment());
        }
    }

    @TestConfiguration
    public static class TestConfig {

        @Bean
        public LoanService loanService() {
            return new LoanService();
        }
    }
}
