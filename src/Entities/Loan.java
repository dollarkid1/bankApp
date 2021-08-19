package Entities;

import com.maven.bank.datastore.LoanStatus;
import com.maven.bank.datastore.LoanType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Loan {
    private BigDecimal loanAmount;
    private LoanType typeOfLoan;
    private LocalDateTime applyDate;
    private LocalDateTime startDate;
    private int tenor;
    private double interestRate;
    private LoanStatus status;
}
