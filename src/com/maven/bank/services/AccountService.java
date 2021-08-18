package com.maven.bank.services;

import Entities.Account;
import Entities.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.exceptions.MavenBankException;

import java.math.BigDecimal;

public interface AccountService {
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;
    public long openSavingsAccount(Customer theCustomer) throws MavenBankException;
    public long openCurrentAccount(Customer theCustomer) throws MavenBankException;


    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException;

    public Account findAccount(long accountNumber) throws MavenBankException;

    public Account findAccount(Customer customer, long accountNumber) throws MavenBankException;

    BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException;

    public void  applyForOverdraft(Account theAccount);
    public void  applyForLoan(Account theAccount);
}