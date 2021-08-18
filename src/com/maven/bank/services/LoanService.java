package com.maven.bank.services;

import Entities.Account;
import Entities.Loan;
import com.maven.bank.exceptions.MavenBankLoanException;

public interface LoanService {

    public Loan approveLoan(Account theLoan) throws MavenBankLoanException;
}
