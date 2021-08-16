package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankException;
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
        assertEquals (0, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (john.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (john, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (john.getBvn ( )));
            assertFalse (john.getAccounts ( ).isEmpty ( ));
            System.out.println (john.getAccounts ( ).get (0));
            assertEquals (newAccountNumber, john.getAccounts ( ).get (0).getAccountNumber ( ));
        }catch(MavenBankException ex){
            ex.printStackTrace ();
        }

    }

    @Test
    void openAccountWithNoCustomer(){
        assertThrows (MavenBankException.class, ()-> accountService.openAccount (null, AccountType.SAVINGS));
    }

    @Test
    void openAccountWithNoAccountType(){
        assertThrows (MavenBankException.class, ()-> accountService.openAccount (john,null));
    }

    @Test
    void openTheSameTypeOfAccountForTheSameCustomer(){
        try{
            long newAccountNumber = accountService.openAccount (john, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (john.getBvn ( )));
            assertFalse (john.getAccounts ( ).isEmpty ( ));
            System.out.println (john.getAccounts ( ).get (0));
            assertEquals (newAccountNumber, john.getAccounts ( ).get (0).getAccountNumber ( ));
        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

        assertThrows (MavenBankException.class, ()-> accountService.openAccount (john, AccountType.SAVINGS));
        assertEquals (1,BankService.getCurrentAccountNumber ());
        assertEquals (1,john.getAccounts ().size ());
    }

}