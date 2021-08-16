package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    private AccountService accountService;
    private Customer john;
    private Account johnAccount;

    @BeforeEach
    void setUp(){
        accountService = new AccountServiceImpl ();
        john = new Customer ();
        john.setBvn (BankService.generateBvn ());
        john.getEmail ("john@doe.com");
        john.setFirstName ("john");
        john.setSurname ("doe");
        john.setPhone ("12345678901");
    }
    @Test
    void openAccount(){
        assertTrue(CustomerRepo.getCustomers ().isEmpty ());
        accountService.openAccount (john, AccountType.SAVINGS);
        assertFalse(CustomerRepo.getCustomers ().isEmpty ());
    }

}