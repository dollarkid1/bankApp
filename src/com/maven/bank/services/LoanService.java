package com.maven.bank.services;

import Entities.Account;
import Entities.Customer;
import Entities.LoanRequest;
import com.maven.bank.exceptions.MavenBankLoanException;

public interface LoanService {

    public LoanRequest approveLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException;
    public LoanRequest approveLoanRequest(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException;
}
