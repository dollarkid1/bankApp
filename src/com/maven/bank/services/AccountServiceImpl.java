package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) {
        Account newAccount = new Account ();
        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        newAccount.setTypeOfAccount (type);
        return  newAccount.getAccountNumber ();
    }
}
