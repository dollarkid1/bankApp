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

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanType getTypeOfLoan() {
        return typeOfLoan;
    }

    public void setTypeOfLoan(LoanType typeOfLoan) {
        this.typeOfLoan = typeOfLoan;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }


}
