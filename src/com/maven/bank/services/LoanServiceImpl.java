package com.maven.bank.services;

import Entities.Account;
import Entities.Customer;
import Entities.LoanRequest;
import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;

public class LoanServiceImpl implements LoanService {

    @Override
    public LoanRequest approveLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {
        if (accountSeekingLoan == null){
            throw new MavenBankLoanException("An Account is required to Process the Loan");
        }
        if (accountSeekingLoan.getAccountLoanRequest() == null){
            throw new MavenBankLoanException("No loan request provided for processing");
        }

        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        theLoanRequest.setStatus(decisionOnLoanRequestWithAccountBalance(accountSeekingLoan));
        return theLoanRequest;

    }

    @Override
    public LoanRequest approveLoanRequest(Customer customer,Account accountSeekingLoan)throws MavenBankLoanException {

        LoanRequestStatus decision = decisionOnLoanWIthTotalCustomerBalance(customer, accountSeekingLoan);
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        if (decision != LoanRequestStatus.APPROVED){

            theLoanRequest = approveLoanRequest(accountSeekingLoan);
        }

        return theLoanRequest;
    }

    private LoanRequestStatus decisionOnLoanRequestWithAccountBalance(Account accountSeekingLoan)throws MavenBankLoanException{

        LoanRequestStatus decision = decisionOnLoanWIthAccountBalance(accountSeekingLoan);


        return decision;
    }

    private LoanRequestStatus decisionOnLoanWIthAccountBalance(Account accountSeekingLoan)throws MavenBankLoanException{

        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest();
        BigDecimal accountBalancePercentage = BigDecimal.valueOf(0.2);
        BigDecimal loanAmountApprovedAutomatically = accountSeekingLoan.getBalance().multiply(accountBalancePercentage);
        if (theLoanRequest.getLoanAmount().compareTo(loanAmountApprovedAutomatically) < BigDecimal.ZERO.intValue()){
            decision = LoanRequestStatus.APPROVED;
        }

        return decision;
    }



    private LoanRequestStatus decisionOnLoanWIthLengthRelationship( Account accountSeekingLoan)throws MavenBankLoanException{
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        return decision;
    }

    private LoanRequestStatus decisionOnLoanWIthTotalCustomerBalance(Customer customer, Account accountSeekingLoan)throws MavenBankLoanException{
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        int minimumLengthOfRelation = 6;
        BigDecimal relationVolumePercentage = BigDecimal.valueOf(0.2);

        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        if (customer.getAccounts().size() > BigDecimal.ONE.intValue()){
            for (Account customerAccount : customer.getAccounts()){
                totalCustomerBalance = totalCustomerBalance.add(customerAccount.getBalance());
            }
        }
        BigDecimal loanAmountApprovedAutomatically = totalCustomerBalance.multiply(relationVolumePercentage);
                if (accountSeekingLoan.getAccountLoanRequest().getLoanAmount().compareTo(loanAmountApprovedAutomatically)
                < BigDecimal.ZERO.intValue()){
                    decision = LoanRequestStatus.APPROVED;
                }

        return decision;
    }

}
