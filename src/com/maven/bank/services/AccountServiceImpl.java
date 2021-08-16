package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankException;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
        if (theCustomer == null || type == null){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        if (accountTypeExists (theCustomer, type )){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }

        Account newAccount = new Account ();
        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        newAccount.setTypeOfAccount (type);
        theCustomer.getAccounts ().add (newAccount);
        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }

    private boolean accountTypeExists(Customer aCustomer, AccountType type){
        boolean accountTypeExists = false;
        for(Account customerAccount : aCustomer.getAccounts ()){
            if (customerAccount.getTypeOfAccount () == type){
                accountTypeExists = true;
                break;
            }
        }
        return accountTypeExists;
    }

}
