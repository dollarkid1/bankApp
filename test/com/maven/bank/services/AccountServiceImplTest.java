package com.maven.bank.services;

import Entities.Account;
import Entities.BankTransaction;
import Entities.Customer;
import Entities.LoanRequest;
import com.maven.bank.datastore.*;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankInsufficientFundsException;
import com.maven.bank.exceptions.MavenBankTransactionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        abu.getEmail ("abu@danladi.com");
        abu.setFirstName ("john");
        abu.setSurname ("danladi");
        abu.setPhone ("12345678901");

        bessie = new Customer ();
        bessie.setBvn (BankService.generateBvn ());
        bessie.getEmail ("bessie@blackie.com");
        bessie.setFirstName ("bessie");
        bessie.setSurname ("blackie");
        bessie.setPhone ("90876543211");
    }

    @AfterEach
    void tearDown(){
        BankService.reset ();
        CustomerRepo.reset ();
    }
    @Test
    void openSavingsAccount(){

        assertTrue(abu.getAccounts ().isEmpty ());
        assertEquals (0000110003, BankService.getCurrentAccountNumber());
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGSACCOUNT);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (0000110004, BankService.getCurrentAccountNumber ( ));
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
        assertThrows (MavenBankException.class, ()-> accountService.openAccount (null, AccountType.SAVINGSACCOUNT));
    }


    @Test
    void openTheSameTypeOfAccountForTheSameCustomer(){
     Optional<Customer> johnOptional = CustomerRepo.getCustomers ().values ().stream ().findFirst ();
        Customer john = johnOptional.get ();

        assertEquals (0000110003, BankService.getCurrentAccountNumber ( ));
        assertNotNull (john);
        assertNotNull (john.getAccounts ());
        assertFalse (john.getAccounts ( ).isEmpty ( ));
        assertEquals (AccountType.SAVINGSACCOUNT.toString (), john.getAccounts ( ).get (0).getClass ().getSimpleName ().toUpperCase());

        assertThrows (MavenBankException.class, ()-> accountService.openAccount (john, AccountType.SAVINGSACCOUNT));
        assertEquals (0000110003,BankService.getCurrentAccountNumber ());
        assertEquals (2, john.getAccounts ().size ());
    }

    @Test
    void openAccountForCurrentAccount(){

        assertTrue(abu.getAccounts ().isEmpty ());
        assertEquals (0000110003, BankService.getCurrentAccountNumber());
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.CURRENTACCOUNT);
            assertEquals (0000110004, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertFalse (abu.getAccounts ( ).isEmpty ( ));
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));
        }catch(MavenBankException ex){
            ex.printStackTrace ();
        }
    }


    @Test
    void openDifferentTypeOfAccountForTheSameCustomer(){
        try{
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGSACCOUNT);
            assertEquals (0000110004, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertEquals (1, abu.getAccounts ().size ());
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));

            newAccountNumber = accountService.openAccount (abu, AccountType.CURRENTACCOUNT);
            assertEquals (0000110005, BankService.getCurrentAccountNumber ( ));
            assertEquals (2, abu.getAccounts ().size ());
            assertEquals (newAccountNumber, abu.getAccounts ().get (1).getAccountNumber ());

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

    }

    @Test
    void openSavingsAccountForANewCustomer(){

        assertTrue(abu.getAccounts ().isEmpty ());
        assertEquals (0000110003, BankService.getCurrentAccountNumber());
        try {
            long newAccountNumber = accountService.openAccount (abu, AccountType.SAVINGSACCOUNT);
            assertFalse (CustomerRepo.getCustomers ( ).isEmpty ( ));
            assertEquals (0000110004, BankService.getCurrentAccountNumber ( ));
            assertTrue (CustomerRepo.getCustomers ( ).containsKey (abu.getBvn ( )));
            assertFalse (abu.getAccounts ( ).isEmpty ( ));
            assertEquals (newAccountNumber, abu.getAccounts ( ).get (0).getAccountNumber ( ));

            newAccountNumber = accountService.openAccount (bessie, AccountType.SAVINGSACCOUNT);
            assertEquals (4, CustomerRepo.getCustomers ().size ());
            assertEquals (0000110005, BankService.getCurrentAccountNumber ( ));
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
            Account johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (BigDecimal.ZERO, johnSavingsAccount.getBalance ());

            BigDecimal accountBalance = accountService.deposit (new BigDecimal (50000), 0000110001);

            johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (accountBalance, johnSavingsAccount.getBalance ());
        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace ( );
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }

    @Test
    void depositNegativeAmount(){
        assertThrows (MavenBankException.class,
                () -> accountService.deposit (new BigDecimal (-5000), 0000110001));

    }

    @Test
    void depositToInvalidAccountNumber(){
        assertThrows (MavenBankException.class,
                () -> accountService.deposit (new BigDecimal (-5000), 1000110001));

    }

    @Test
    void depositWithVeryLargeAmount(){
        try{
            Account johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (BigDecimal.ZERO, johnSavingsAccount.getBalance ());
            BigDecimal depositAmount = new BigDecimal ("10000000000000000000");

            BigDecimal accountBalance = accountService.deposit (depositAmount, 0000110001);

            johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (depositAmount, johnSavingsAccount.getBalance ());
        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace ( );
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }



    @Test
    void findAccount(){
        try{
            Account johnCurrentAccount = accountService.findAccount (0000110002);
            assertNotNull (johnCurrentAccount);
            assertEquals (0000110002,johnCurrentAccount.getAccountNumber ());
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

    @Test
    void Withdraw(){
        try{
            Account johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (BigDecimal.ZERO, johnSavingsAccount.getBalance ());

            BigDecimal accountBalance = accountService.deposit (new BigDecimal (50000), 0000110001);
            johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (accountBalance, johnSavingsAccount.getBalance ());

            BigDecimal newAccountBalance = accountService.withdraw(new BigDecimal (5000), 0000110001);
            johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (new BigDecimal (45000), johnSavingsAccount.getBalance ());

        } catch (MavenBankTransactionException ex) {
            ex.printStackTrace ( );
        }catch (MavenBankException ex){
            ex.printStackTrace ();
        }
    }

    @Test
    void withdrawNegativeAmount() throws MavenBankException {
        assertThrows (MavenBankException.class,
                () -> accountService.deposit (new BigDecimal (-5000), 1000110001));
    }

    @Test
    void withdrawFromAnInvalidAccount() throws MavenBankException {
        assertThrows (MavenBankInsufficientFundsException.class,
                () -> accountService.withdraw(new BigDecimal (5000), 00110001));
    }

    @Test
    void withdrawAmountHigherThanAccountBalance() throws MavenBankException {
        try{
            Account johnSavingsAccount = accountService.findAccount (0000110001);
            BigDecimal accountBalance = accountService.deposit (new BigDecimal (50000), 0000110001);
            johnSavingsAccount = accountService.findAccount (0000110001);
            assertEquals (accountBalance, johnSavingsAccount.getBalance ());
        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
        assertThrows (MavenBankInsufficientFundsException.class,
                () -> accountService.withdraw(new BigDecimal (70000), 0000110001));
    }
    @Test
    void applyForLoan(){
        LoanRequest johnLoanRequest = new LoanRequest();
        johnLoanRequest.setLoanAmount(BigDecimal.valueOf(50_000_000));
        johnLoanRequest.setApplyDate(LocalDateTime.now());
        johnLoanRequest.setInterestRate(0.1);
        johnLoanRequest.setStatus(LoanRequestStatus.NEW);
        johnLoanRequest.setTenor(24);
        johnLoanRequest.setTypeOfLoan(LoanType.SME);

        try{
            Account johnCurrentAccount = accountService.findAccount (0000110002);
            assertNotNull(johnCurrentAccount.getAccountLoanRequest());
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

    }

    @Test
    void addBankTransactionWithNullTransaction(){
        assertThrows(MavenBankTransactionException.class,()-> accountService.addBankTransaction(null,abuAccount));
    }
    @Test
    void addBankTransactionWithNullTAccount(){
        BankTransaction transaction =new BankTransaction(BankTransactionType.DEPOSIT,BigDecimal.valueOf(50));
        assertThrows(MavenBankTransactionException.class,()-> accountService.addBankTransaction(transaction,null));
    }
    @Test
    void addBankTransactionWithDeposit(){
        try{
            Account janeSavingsAccount = accountService.findAccount (0000110003);
            assertNotNull(janeSavingsAccount);
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());

            BankTransaction janeDeposit = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(10_000));
            accountService.addBankTransaction(janeDeposit,janeSavingsAccount);
            assertEquals(BigDecimal.valueOf(10_000),janeSavingsAccount.getBalance());
            assertEquals(1,janeSavingsAccount.getTransactions().size());


        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }

    }

    @Test
    void addBankTransactionWithNegativeDeposit(){
        try{
            Account janeSavingsAccount = accountService.findAccount (0000110003);
            assertNotNull(janeSavingsAccount);
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());

            BankTransaction janeDeposit = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(-10_000));
            assertThrows(MavenBankTransactionException.class, ()-> accountService.addBankTransaction(janeDeposit,janeSavingsAccount));
            assertEquals(BigDecimal.ZERO,janeSavingsAccount.getBalance());
            assertEquals(0,janeSavingsAccount.getTransactions().size());
        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
    }

    @Test
    void addTransactionForWithdrawal(){
        try {
            Account janeSavingAccount = accountService.findAccount(0000110003);
            assertNotNull(janeSavingAccount);
            BankTransaction depositTx = new BankTransaction( BankTransactionType.DEPOSIT, BigDecimal.valueOf(50_000));
            accountService.addBankTransaction(depositTx, janeSavingAccount);
            assertEquals(BigDecimal.valueOf(50_000),janeSavingAccount.getBalance());
            assertEquals(1,janeSavingAccount.getTransactions().size());

            BankTransaction withdrawTx = new BankTransaction( BankTransactionType.WITHDRAWAL, BigDecimal.valueOf(20_000));
            accountService.addBankTransaction(withdrawTx, janeSavingAccount);
            assertEquals(BigDecimal.valueOf(30_000), janeSavingAccount.getBalance());
//            assertEquals(2,janeSavingAccount.getTransactions().size());
        }catch (MavenBankException ex){ex.printStackTrace();}
    }


    @Test
    void addBankTransactionWithNegativeWithdrawal(){
       try{
           Account janeSavingAccount = accountService.findAccount(0000110003);
           assertNotNull(janeSavingAccount);
           assertEquals(BigDecimal.ZERO, janeSavingAccount.getBalance());
           assertEquals(0, janeSavingAccount.getTransactions().size());


           BankTransaction withdrawTx = new BankTransaction( BankTransactionType.WITHDRAWAL, BigDecimal.valueOf(-20_000));
            assertThrows(MavenBankTransactionException.class, ()-> accountService.addBankTransaction(withdrawTx, janeSavingAccount));
              assertEquals(0,janeSavingAccount.getTransactions().size());

       }
       catch (MavenBankException ex){ex.printStackTrace();}

}
}