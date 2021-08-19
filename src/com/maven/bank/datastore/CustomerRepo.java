package com.maven.bank.datastore;

import Entities.*;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.services.AccountService;
import com.maven.bank.services.AccountServiceImpl;
import com.maven.bank.services.BankService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {
    private static Map<Long, Customer> customers = new HashMap<> ();

    public static  Map<Long, Customer> getCustomers() {
        return customers;
    }

    private void setCustomers(Map<Long, Customer> customers) {
        this.customers = customers;
    }

    static{
        reset ();
    }

    public static void reset() {
        Customer john = new Customer ();
        john.setBvn (BankService.generateBvn ());
        john.getEmail ("john@doe.com");
        john.setFirstName ("john");
        john.setSurname ("doe");
        john.setPhone ("12345678901");

        Account johnSavingsAccount = new SavingsAccount(0000110001);
        john.setRelationshipStartDate(johnSavingsAccount.getStartDate());
        BankTransaction initialDeposit = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(300_000));
        johnSavingsAccount.getTransactions().add(initialDeposit);

        BankTransaction mayAllowance = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(50_000));
            mayAllowance.setDateTime(LocalDateTime.now().minusMonths(3));
        johnSavingsAccount.getTransactions().add(mayAllowance);


        BankTransaction juneAllowance = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(50_000));
            juneAllowance.setDateTime(LocalDateTime.now().minusMonths(2));
        johnSavingsAccount.getTransactions().add(juneAllowance);


        BankTransaction julyAllowance = new BankTransaction(BankTransactionType.DEPOSIT, BigDecimal.valueOf(50_000));
            julyAllowance.setDateTime(LocalDateTime.now().minusMonths(2));
        johnSavingsAccount.getTransactions().add(julyAllowance);

        johnSavingsAccount.setBalance(BigDecimal.valueOf(450000));




        john.getAccounts ().add (johnSavingsAccount);
        CurrentAccount johnCurrentAccount = new CurrentAccount( 0000110002);
        john.getAccounts ().add (johnCurrentAccount);
        customers.put (john.getBvn (), john);

        Customer jane = new Customer ();
        jane.setBvn (BankService.generateBvn ());
        jane.getEmail ("jane@blackie.com");
        jane.setFirstName ("jane");
        jane.setSurname ("blackie");
        jane.setPhone ("90876543211");

       Account janeSavingsAccount = new SavingsAccount( 0000110003);
       jane.setRelationshipStartDate(janeSavingsAccount.getStartDate());
        jane.getAccounts ().add (janeSavingsAccount);
        customers.put (jane.getBvn (), jane);
    }
}
