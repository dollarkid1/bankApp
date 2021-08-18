package com.maven.bank.services;

import Entities.Account;
import Entities.Loan;
import com.maven.bank.datastore.LoanStatus;
import com.maven.bank.datastore.LoanType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankLoanException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceImplTest {
   private Loan johnLoan;
   private LoanService loanService;
   private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
        johnLoan = new Loan();
        johnLoan.setLoanAmount(BigDecimal.valueOf(50_000_000));
        johnLoan.setApplyDate(LocalDateTime.now());
        johnLoan.setInterestRate(0.1);
        johnLoan.setStatus(LoanStatus.NEW);
        johnLoan.setTenor(24);
        johnLoan.setTypeOfLoan(LoanType.SME);
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void approveLoanWithNullAccount(){
        assertThrows(MavenBankLoanException.class, ()-> loanService.approveLoan(null));
    }
    @Test
    void approveLoanWithNullLoan(){
        assertThrows(MavenBankLoanException.class, ()-> loanService.approveLoan(null));
    }
    @Test
    void approveLoan(){
        try{
            Account johnCurrentAccount = accountService.findAccount (0000110002);
            assertNotNull(johnCurrentAccount.getAccountLoan());
            johnCurrentAccount.setAccountLoan(johnLoan);

            Loan processedLoan = loanService.approveLoan(johnCurrentAccount);
            assertNotNull(processedLoan);

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
    }
}