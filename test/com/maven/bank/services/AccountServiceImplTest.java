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
    private Customer jane;
    private Account johnAccount;
    private Account JaneAccount;

    @BeforeEach
    void setUp(){
        accountService = new AccountServiceImpl ();
        john = new Customer ();
        john.setBvn (BankService.generateBvn ());
        john.getEmail ("john@doe.com");
        john.setFirstName ("john");
        john.setSurname ("doe");
        john.setPhone ("12345678901");

        jane = new Customer ();
        jane.setBvn (BankService.generateBvn ());
        jane.getEmail ("jane@blackie.com");
        jane.setFirstName ("jane");
        jane.setSurname ("blackie");
        jane.setPhone ("90876543211");
    }
    @Test
    void openSavingsAccount(){

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

    @Test
    void openAccountForCurrentAccount(){
        assertTrue(CustomerRepo.getCustomers ().isEmpty ());
        assertEquals (0, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (john.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (john, AccountType.CURRENT);
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
    void openDifferentTypeOfAccountForTheSameCustomer(){
        try{
            long newAccountNumber = accountService.openAccount (john, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (john.getBvn ( )));
            assertEquals (1, john.getAccounts ().size ());
            assertEquals (newAccountNumber, john.getAccounts ( ).get (0).getAccountNumber ( ));
            newAccountNumber = accountService.openAccount (john, AccountType.CURRENT);
            assertEquals (2, BankService.getCurrentAccountNumber ( ));
            assertEquals (2, john.getAccounts ().size ());
            assertEquals (newAccountNumber, john.getAccounts ().get (1).getAccountNumber ());

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

    }

    @Test
    void openSavingsAccountForANewCustomer(){

        assertTrue(CustomerRepo.getCustomers ().isEmpty ());
        assertEquals (0, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (john.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (john, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (john.getBvn ( )));
            assertFalse (john.getAccounts ( ).isEmpty ( ));
            assertEquals (newAccountNumber, john.getAccounts ( ).get (0).getAccountNumber ( ));

            newAccountNumber = accountService.openAccount (jane, AccountType.SAVINGS);
            assertEquals (2, CustomerRepo.getCustomers ().size ());
            assertEquals (2, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (jane.getBvn ( )));
            assertFalse (jane.getAccounts ( ).isEmpty ( ));
            assertEquals (1, jane.getAccounts ().size ());
            assertEquals (newAccountNumber, jane.getAccounts ( ).get (0).getAccountNumber ( ));
            assertEquals (1, john.getAccounts ().size ());
        }catch(MavenBankException ex){
            ex.printStackTrace ();
        }

    }



}