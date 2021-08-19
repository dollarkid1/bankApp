package com.maven.bank.services;

import Entities.Account;
import Entities.CurrentAccount;
import Entities.Customer;
import Entities.SavingsAccount;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankInsufficientFundsException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
       long accountNumber = BigDecimal.ZERO.longValue();
        if (type == AccountType.SAVINGSACCOUNT)
           accountNumber =  openSavingsAccount(theCustomer);
        else if (type == AccountType.CURRENTACCOUNT)
            accountNumber = openCurrentAccount(theCustomer);
        return accountNumber;
    }

    @Override
    public long openSavingsAccount(Customer theCustomer) throws MavenBankException {
        if (theCustomer == null){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        Account newAccount = new SavingsAccount();
        if (accountTypeExists (theCustomer, newAccount.getClass().getTypeName() )){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }

        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        theCustomer.getAccounts ().add (newAccount);
        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }

    @Override
    public long openCurrentAccount(Customer theCustomer) throws MavenBankException {
        if (theCustomer == null){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        Account newAccount = new CurrentAccount();
        if (accountTypeExists (theCustomer,  newAccount.getClass().getTypeName()  )){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }
        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        theCustomer.getAccounts ().add (newAccount);
        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }

    @Override
    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException {
        Account depositAccount = findAccount (accountNumber);
        validTransaction (amount, depositAccount);

        BigDecimal newBalance = BigDecimal.ZERO;
        newBalance = depositAccount.getBalance ().add (amount);
        depositAccount.setBalance (newBalance);
        return newBalance;
    }

    @Override
    public Account findAccount(long accountNumber) throws MavenBankException {
        Account foundAccount = null;
        boolean accountFound = false;

        for(Customer customer : CustomerRepo.getCustomers ().values ()){
            for (Account anAccount : customer.getAccounts ()) {
                if (anAccount.getAccountNumber () == accountNumber){
                    foundAccount = anAccount;
                    accountFound = true;
                    break;
                }
            }
            if (accountFound){
                break;
            }
        }
        return foundAccount;
    }

    @Override
    public Account findAccount(Customer customer, long accountNumber) throws MavenBankException {
        return null;
    }

    @Override
    public void applyForOverdraft(Account theAccount) {
        // TODO
    }

    @Override
    public void applyForLoan(Account theAccount) {

    }

    @Override
    public BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException {
       Account theAccount = findAccount(accountNumber);
       validTransaction(amount, theAccount);
       try{
           checkForSufficientBalance(amount, theAccount);
       } catch (MavenBankInsufficientFundsException insufficientFundsException) {

       }
        BigDecimal newBalance = debitAccount (amount, accountNumber);

        return newBalance;

    }

    public BigDecimal debitAccount(BigDecimal amount, long accountNumber) throws MavenBankException {

        Account theAccount = findAccount (accountNumber);
       BigDecimal newBalance =  theAccount.getBalance ().subtract (amount);
        theAccount.setBalance (newBalance);
        return newBalance;

    }

    private void validTransaction(BigDecimal amount, Account account) throws MavenBankTransactionException, MavenBankException{
        if (amount.compareTo (BigDecimal.ZERO) < BigDecimal.ONE.intValue ()){
            throw new MavenBankTransactionException ( "Transaction amount cannot be Negative!!" );
        }

        if (account == null){
            throw new MavenBankTransactionException ( "Transaction account not found" );
        }

    }



    private void checkForSufficientBalance(BigDecimal amount, Account account) throws MavenBankInsufficientFundsException {
        if (amount.compareTo (account.getBalance()) < BigDecimal.ZERO.intValue()){
            throw new MavenBankInsufficientFundsException("Insufficient Funds");
        }
    }

    private boolean accountTypeExists(Customer aCustomer, String typeName){
        boolean accountTypeExists = false;
        for(Account customerAccount : aCustomer.getAccounts ()){
            ;
            if (customerAccount.getClass().getTypeName().equals(typeName)){
                accountTypeExists = true;
                break;
            }
        }
        return accountTypeExists;
    }





}
