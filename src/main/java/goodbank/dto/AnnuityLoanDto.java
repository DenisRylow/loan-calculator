package goodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnnuityLoanDto {
    private AnnuityLoanRequest requestedLoan;
    private List<MonthlyLoanStatus> schedule;
}
