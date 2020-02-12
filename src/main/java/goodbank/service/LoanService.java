package goodbank.service;

import goodbank.LoanConstants;
import goodbank.dto.AnnuityLoanDto;
import goodbank.dto.AnnuityLoanRequest;
import goodbank.dto.AvailableLoan;
import goodbank.dto.MonthlyLoanStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Validated
public class LoanService {
    @Value("${goodbank.default-interest-rate}")
    private Integer interestRate;

    @Value("${goodbank.default-loan-sum}")
    private Long loanSum;

    public AvailableLoan getAvailableLoan() {
        return new AvailableLoan(
                LoanConstants.MINIMAL_LOAN_SUM,
                LoanConstants.MAXIMAL_LOAN_SUM,
                loanSum,
                LoanConstants.MINIMAL_INTEREST_RATE,
                LoanConstants.MAXIMAL_INTEREST_RATE,
                interestRate,
                getPossibleTerms()
        );
    }

    private List<Integer> getPossibleTerms() {
        return Stream
                .iterate(LoanConstants.MINIMAL_LOAN_TERM, (term) -> term + LoanConstants.NUMBER_OF_MONTHS_IN_YEAR)
                .limit(5)
                .collect(Collectors.toList());
    }

    public AnnuityLoanDto calculateLoan(@Valid AnnuityLoanRequest annuityLoanRequest) {
        GregorianCalendar calendar = new GregorianCalendar(1, Calendar.JANUARY,1);
        BigDecimal totalMonthlyPayment = calculateMonthlyPayment(
                annuityLoanRequest.getLoanedSum(),
                annuityLoanRequest.getInterestRateInBasisPoints(),
                annuityLoanRequest.getLoanTerm()
        );
        BigDecimal totalPayment = calculateMonthlyPayment(
                annuityLoanRequest.getLoanedSum(),
                annuityLoanRequest.getInterestRateInBasisPoints(),
                annuityLoanRequest.getLoanTerm()
        );
        BigDecimal debt = BigDecimal.valueOf(annuityLoanRequest.getLoanedSum());
        List<MonthlyLoanStatus> list = new ArrayList<>();
        int period = 1;
        do {
            BigDecimal interest = calculateInterest(
                    debt,
                    annuityLoanRequest.getInterestRateInBasisPoints()
            );
            BigDecimal paymentToPrincipal = totalMonthlyPayment.subtract(interest);
            debt = debt.subtract(paymentToPrincipal);
            MonthlyLoanStatus monthlyLoanStatus = new MonthlyLoanStatus(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    paymentToPrincipal,
                    interest,
                    debt,
                    totalPayment
            );
            calendar.add(Calendar.MONTH,1);
            list.add(monthlyLoanStatus);
        } while (++period < annuityLoanRequest.getLoanTerm());

        BigDecimal lastPeriodPrincipal = debt;
        BigDecimal lastPeriodInterest = totalMonthlyPayment.subtract(lastPeriodPrincipal);
        MonthlyLoanStatus monthlyLoanStatus = new MonthlyLoanStatus(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                lastPeriodPrincipal,
                lastPeriodInterest,
                BigDecimal.ZERO,
                totalMonthlyPayment
        );
        list.add(monthlyLoanStatus);
        return new AnnuityLoanDto(annuityLoanRequest, list);
    }

    /**
     * Calculates the monthly payment of annuity loan.
     *
     * @param principal - loan amount
     * @param interestRate - loan interest rate in basis points
     * @param loanTerm - term of the loan in months
     * @return payment per month
     */
    BigDecimal calculateMonthlyPayment(Long principal, Integer interestRate, Integer loanTerm) {
        Objects.requireNonNull(principal);
        Objects.requireNonNull(interestRate);
        Objects.requireNonNull(loanTerm);
        double P = interestRate / ((double) 12 * 100 * 100); // Absolute interest rate.
        double x = principal * (P + P / (Math.pow(1 + P, loanTerm.doubleValue()) - 1));
        return BigDecimal.valueOf(x).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculates the amount of money paid towards the interest.
     *
     * @param principal - loan sum
     * @param interestRate - loan interest rate in basis points
     * @return the amount of money paid towards the interest
     */
    BigDecimal calculateInterest(BigDecimal principal, Integer interestRate) {
        Objects.requireNonNull(principal);
        Objects.requireNonNull(interestRate);
        double P = interestRate / ((double) 12 * 100 * 100);
        return principal.multiply(BigDecimal.valueOf(P)).setScale(2, RoundingMode.HALF_UP);
    }
}

