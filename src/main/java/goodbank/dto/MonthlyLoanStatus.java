package goodbank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MonthlyLoanStatus {
    private Integer year;
    private Integer month;
    private BigDecimal paymentToPrincipal;
    private BigDecimal paymentToInterest;
    private BigDecimal debt;
    private BigDecimal totalPayment;
}
