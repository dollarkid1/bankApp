package com.maven.bank.services;

import Entities.Account;
import Entities.Customer;
import Entities.LoanRequest;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.datastore.LoanType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankLoanException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoanServiceImplTest {
   private LoanRequest johnLoanRequest;
   private LoanService loanService;
   private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
        loanService = new LoanServiceImpl();


        johnLoanRequest = new LoanRequest();
        johnLoanRequest.setApplyDate(LocalDateTime.now());
        johnLoanRequest.setInterestRate(0.1);
        johnLoanRequest.setStatus(LoanRequestStatus.NEW);
        johnLoanRequest.setTenor(24);
        johnLoanRequest.setTypeOfLoan(LoanType.SME);
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void approveLoanRequestWithNullAccount(){
        assertThrows(MavenBankLoanException.class, ()-> loanService.approveLoanRequest( null));
    }
    @Test
    void approveLoanRequestWithNullLoan(){
        assertThrows(MavenBankLoanException.class, ()-> loanService.approveLoanRequest(null));
    }
    @Test
    void approveLoanRequestWithAccountBalance(){
        try{
            Account johnCurrentAccount = accountService.findAccount (0000110002);
            assertNull(johnCurrentAccount.getAccountLoanRequest());
            johnLoanRequest.setLoanAmount(BigDecimal.valueOf(9_000_000));
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);

            LoanRequest processedLoanRequest = loanService.approveLoanRequest( johnCurrentAccount);
            assertNotNull(processedLoanRequest);

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
    }

    @Test
    void approveLoanRequestWithAccountBalanceANdHighLoanRequest(){
        try{
            Account johnCurrentAccount = accountService.findAccount (0000110002);
            assertNull(johnCurrentAccount.getAccountLoanRequest());
            johnLoanRequest.setLoanAmount(BigDecimal.valueOf(90_000_000));
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);

            LoanRequest processedLoanRequest = loanService.approveLoanRequest( johnCurrentAccount);
            assertNotNull(processedLoanRequest);

        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
    }

    @Test
    void approveLoanRequestWithLengthOfRelationAndTransactionVolume(){
        try{
            Account johnSavingsAccount = accountService.findAccount (0000110001);
            Optional<Customer> optionalCustomer = CustomerRepo.getCustomers().values().stream().findFirst();
            Customer john = optionalCustomer.isPresent()? optionalCustomer.get() : null;
            assertNotNull(john);
            john.setRelationshipStartDate(johnSavingsAccount.getStartDate().minusYears(2));
            assertEquals(BigDecimal.valueOf(450_000), johnSavingsAccount.getBalance());
            johnLoanRequest.setLoanAmount(BigDecimal.valueOf(3_000_000));
            johnSavingsAccount.setAccountLoanRequest(johnLoanRequest);
            LoanRequest johnLoanRequest  =loanService.approveLoanRequest(john, johnSavingsAccount);
            assertEquals(LoanRequestStatus.APPROVED,johnLoanRequest.getStatus());



        } catch (MavenBankException e) {
            e.printStackTrace ( );
        }
    }
}