package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankTransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {
    private AccountService accountService;
    private Customer abu;
    private Customer bessie;
    private Account abuAccount;
    private Account bessieAccount;

    @BeforeEach
    void setUp(){
        accountService = new AccountServiceImpl ();
        abu = new Customer ();
        abu.setBvn (BankService.generateBvn ());
        abu.getEmail ("john@doe.com");
        abu.setFirstName ("john");
        abu.setSurname ("doe");
        abu.setPhone ("12345678901");

        bessie = new Customer ();
        bessie.setBvn (BankService.generateBvn ());
        bessie.getEmail ("jane@blackie.com");
        bessie.setFirstName ("jane");
        bessie.setSurname ("blackie");
        bessie.setPhone ("90876543211");
    }

//    @AfterEach
//    void tearDown(){
//
//    }
    @Test
    void openSavingsAccount(){

        assertTrue(abu.getAccounts ().isEmpty ());
        assertEquals (3, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (abu.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (4, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertFalse (abu.getAccounts ( ).isEmpty ( ));
            System.out.println (abu.getAccounts ( ).get (0));
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));
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
        assertThrows (MavenBankException.class, ()-> accountService.openAccount (abu,null));
    }

    @Test
    void openTheSameTypeOfAccountForTheSameCustomer(){
//        System.out.println (CustomerRepo.getCustomers () );
//        System.out.println (CustomerRepo.getCustomers () .get (1));
        Optional<Customer> johnOptional = CustomerRepo.getCustomers ().values ().stream ().findFirst ();
        Customer john = johnOptional.get ();

//        Customer john = CustomerRepo.getCustomers ( ).get ((long)1 );
        assertEquals (3, BankService.getCurrentAccountNumber ( ));
        assertFalse (john.getAccounts ( ).isEmpty ( ));
        assertEquals (AccountType.SAVINGS, john.getAccounts ( ).get (0).getTypeOfAccount ());

        assertThrows (MavenBankException.class, ()-> accountService.openAccount (john, AccountType.SAVINGS));
        assertEquals (3,BankService.getCurrentAccountNumber ());
        assertEquals (2, john.getAccounts ().size ());
    }

    @Test
    void openAccountForCurrentAccount(){
        assertTrue(CustomerRepo.getCustomers ().isEmpty ());
        assertEquals (0, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (abu.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.CURRENT);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertFalse (abu.getAccounts ( ).isEmpty ( ));
            System.out.println (abu.getAccounts ( ).get (0));
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));
        }catch(MavenBankException ex){
            ex.printStackTrace ();
        }
    }


    @Test
    void openDifferentTypeOfAccountForTheSameCustomer(){
        try{
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertEquals (1, abu.getAccounts ().size ());
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));
            newAccountNumber = accountService.openAccount (abu, AccountType.CURRENT);
            assertEquals (2, BankService.getCurrentAccountNumber ( ));
            assertEquals (2, abu.getAccounts ().size ());
            assertEquals (newAccountNumber, abu.getAccounts ().get (1).getAccountNumber ());

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

    }

    @Test
    void openSavingsAccountForANewCustomer(){

        assertTrue(CustomerRepo.getCustomers ().isEmpty ());
        assertEquals (0, BankService.getCurrentAccountNumber());
        assertFalse (CustomerRepo.getCustomers ().containsKey (abu.getBvn ()));
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGS);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (1, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertFalse (abu.getAccounts ( ).isEmpty ( ));
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));

            newAccountNumber = accountService.openAccount (bessie, AccountType.SAVINGS);
            assertEquals (2, CustomerRepo.getCustomers ().size ());
            assertEquals (2, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (bessie.getBvn ( )));
            assertFalse (bessie.getAccounts ( ).isEmpty ( ));
            assertEquals (1, bessie.getAccounts ().size ());
            assertEquals (newAccountNumber, bessie.getAccounts ( ).get (0).getAccountNumber ( ));
            assertEquals (1, abu.getAccounts ().size ());
        }catch(MavenBankException ex){
            ex.printStackTrace ();
        }

    }

    @Test
    void deposit(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1);
            assertEquals (BigDecimal.ZERO, johnSavingsAccount.getBalance ());

            BigDecimal accountBalance = accountService.deposit (new BigDecimal (50000), 1);

            johnSavingsAccount = accountService.findAccount (1);
            assertEquals (accountBalance, johnSavingsAccount.getBalance ());
        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace ( );
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }


    @Test
    void findAccount(){
        try{
            Account johnCurrentAccount = accountService.findAccount (2);
            assertNotNull (johnCurrentAccount);
            assertEquals (2,johnCurrentAccount.getAccountNumber ());
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }

    @Test
    void findAccountWithInvalidAccountNumber(){
        try{
            Account johnCurrentAccount = accountService.findAccount (2000);
            assertNull (johnCurrentAccount);
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }
}