package Entities;

import com.maven.bank.datastore.AccountType;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;

public abstract class Account {
    private long accountNumber;
//    private AccountType type;
//    private AccountType typeOfAccount;
    private BigDecimal balance = BigDecimal.ZERO;
    private String pin;
    private static String accountPin;
    private Loan accountLoan;

    public Loan getAccountLoan() {
        return accountLoan;
    }

    public void setAccountLoan(Loan accountLoan) {
        this.accountLoan = accountLoan;
    }

    public Account (){}

    public Account(long accountNumber){

        this.accountNumber = accountNumber;

    }

    public Account(long accountNumber, BigDecimal balance) {

        this.balance = balance;
        this.pin = pin;
    }



    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }





    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public static String getAccountPin() {
        return accountPin;
    }

    public void setAccountPin(String accountPin) throws MavenBankTransactionException {
        Account.accountPin = accountPin;
        validatePin (accountPin);
    }

    public void validatePin(String pin) throws MavenBankTransactionException {
        if (pin.length () != 4){
            throw new MavenBankTransactionException ( "invalid pin please enter the correct pin" );
        }
        if (!accountPin.equals (pin)){
            throw new MavenBankTransactionException ( "invalid pin please enter the correct pin" );
        }
    }
}
