package goodbank.dto;

import goodbank.LoanConstants;
import goodbank.validation.DivisableBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnuityLoanRequest {
    @NotNull
    @Min(LoanConstants.MINIMAL_INTEREST_RATE)
    @Max(LoanConstants.MAXIMAL_INTEREST_RATE)
    private Integer interestRateInBasisPoints;

    @NotNull
    @Min(LoanConstants.MINIMAL_LOAN_TERM)
    @Max(LoanConstants.MAXIMAL_LOAN_TERM)
    @DivisableBy(12L)
    private Integer loanTerm;

    @NotNull
    @Min(LoanConstants.MINIMAL_LOAN_SUM)
    @Max(LoanConstants.MAXIMAL_LOAN_SUM)
    private Long loanedSum;
}
