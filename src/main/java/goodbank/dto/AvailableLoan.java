package goodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailableLoan {
    private Long minLoanSum;
    private Long maxLoanSum;
    private Long defaultLoanSum;
    private Integer minLoanInterestRate;
    private Integer maxLoanInterestRate;
    private Integer defaultLoanInterestRate;
    private List<Integer> possibleLoanTerms;
}
