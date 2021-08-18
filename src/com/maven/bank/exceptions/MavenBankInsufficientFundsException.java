package com.maven.bank.exceptions;

public class MavenBankInsufficientFundsException extends MavenBankException{
    public MavenBankInsufficientFundsException() {
        super ( );
    }

    public MavenBankInsufficientFundsException(String message) {
        super (message);
    }

    public MavenBankInsufficientFundsException(String message, Throwable ex) {
        super (message, ex);
    }

    public MavenBankInsufficientFundsException(Throwable ex) {
        super (ex);
    }
}
