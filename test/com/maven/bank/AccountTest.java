package com.maven.bank;

import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankTransactionException;
import com.maven.bank.services.BankService;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    Customer john;
    Account johnAccount;
    @BeforeEach
    void setUp(){
        john = new Customer();
        johnAccount = new Account();

    }

    @Test
    void openAccount() throws MavenBankTransactionException {
        john.setBvn (BankService.generateBvn ());
        john.getEmail ("john@doe.com");
        john.setFirstName ("john");
        john.setSurname ("doe");
        john.setPhone ("12345678901");

        johnAccount.setAccountNumber(BankService.generateAccountNumber ());
        johnAccount.setTypeOfAccount (AccountType.SAVINGS);
        johnAccount.setBalance (new BigDecimal (5000));
        johnAccount.setAccountPin ("1470");
        john.getAccounts().add(johnAccount);

        assertTrue(CustomerRepo.getCustomers().isEmpty ());

    }

}
