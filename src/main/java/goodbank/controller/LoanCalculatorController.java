package goodbank.controller;

import goodbank.dto.AnnuityLoanDto;
import goodbank.dto.AnnuityLoanRequest;
import goodbank.dto.AvailableLoan;
import goodbank.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class LoanCalculatorController {

    @Autowired
    private LoanService loanService;

    @PutMapping(value = "/calculator/loans/annuity",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AnnuityLoanDto getAnnuityLoan(@RequestBody AnnuityLoanRequest annuityLoanRequest) {
        return loanService.calculateLoan(annuityLoanRequest);
    }

    @GetMapping(value = "/loans/available",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AvailableLoan getAvailableLoan() {
        return loanService.getAvailableLoan();
    }
}
