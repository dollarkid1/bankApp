package Entities;

import java.math.BigDecimal;

public class CurrentAccount extends Account{
    public CurrentAccount() {
    }

    public CurrentAccount(long accountNumber) {
        super.setAccountNumber(accountNumber);
    }

    public CurrentAccount(long accountNumber, BigDecimal balance) {
        setAccountNumber(accountNumber);
        setBalance(balance);
    }
}
