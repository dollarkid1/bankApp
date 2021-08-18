package com.maven.bank.services;

import Entities.Account;
import Entities.Loan;
import com.maven.bank.datastore.LoanStatus;
import com.maven.bank.exceptions.MavenBankLoanException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;

public class LoanServiceImpl implements LoanService {
    @Override
    public Loan approveLoan(Account accountSeekingLoan)throws MavenBankLoanException {
        if (accountSeekingLoan == null){
            throw new MavenBankLoanException("An Account is required to Process the Loan");
        }
        if (accountSeekingLoan.getAccountLoan() == null){
            throw new MavenBankLoanException("No loan provided for processing");
        }

       Loan theLoan = accountSeekingLoan.getAccountLoan();
       theLoan.setStatus(decisionOnLoan(accountSeekingLoan));

        return theLoan;
    }

    private LoanStatus decisionOnLoan( Account accountSeekingLoan)throws MavenBankLoanException{

        LoanStatus decision = LoanStatus.PENDING;
        Loan theLoan = accountSeekingLoan.getAccountLoan();
        BigDecimal accountBalancePercentage = BigDecimal.valueOf(0.2);
        BigDecimal loanAmountApprovedAutomatically = accountSeekingLoan.getBalance().multiply(accountBalancePercentage);
        if (theLoan.getLoanAmount().compareTo(loanAmountApprovedAutomatically) < BigDecimal.ZERO.intValue()){
            decision = LoanStatus.APPROVED;
        }

        return decision;
    }
}
