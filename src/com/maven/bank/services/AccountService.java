package com.maven.bank.services;

import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;

public interface AccountService {
    public long openAccount(Customer theCustomer, AccountType type);
}
