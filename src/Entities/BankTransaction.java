package Entities;

import com.maven.bank.datastore.BankTransactionType;
import com.maven.bank.services.BankService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankTransaction {
    private long id;
    private BankTransactionType type;
    private LocalDateTime dateTime;
    private BigDecimal amount;

    public  BankTransaction (BankTransactionType type, BigDecimal txAmount){
        this.id = BankService.getCurrentTransactionNumber()   ;
        this.dateTime = LocalDateTime.now();
        this.type = type;
        amount = txAmount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BankTransactionType getType() {
        return type;
    }

    public void setType(BankTransactionType type) {
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
