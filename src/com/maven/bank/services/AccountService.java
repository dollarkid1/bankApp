package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;

public interface AccountService {
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;

    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankTransactionException;

    public Account findAccount(long accountNumber) throws MavenBankException;

    public Account findAccount(Customer customer, long accountNumber) throws MavenBankException;
}