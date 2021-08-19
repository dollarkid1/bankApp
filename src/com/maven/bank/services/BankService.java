package com.maven.bank.services;

import java.time.LocalDate;
import java.time.Period;

public class BankService {

    private static long currentBVN = 2;
    private static long currentAccountNumber = 0000110003;
    private static long currentTransactionId = 0;
    public static long generateBvn(){
        currentBVN++;
        return currentBVN;
    }

    public static long generateAccountNumber(){
        currentAccountNumber++;
        return currentAccountNumber;

    }
    public static long generateTransaction(){
        currentTransactionId++;
        return currentTransactionId;

    }

    public static long getCurrentAccountNumber() {
        return currentAccountNumber;
    }

    public static long getCurrentBVN() {
        return currentBVN;
    }

    private static void setCurrentBVN(long currentBVN) {
        BankService.currentBVN = currentBVN;
    }

    private static void setCurrentAccountNumber(long currentAccountNumber){
        BankService.currentAccountNumber = currentAccountNumber;
    }

    public static void reset(){
        currentBVN = 2;
        currentAccountNumber = 0000110003;
    }

    public static long getCurrentTransactionNumber() {
        return currentTransactionId;
    }

    private static void setCurrentTransactionId(long currentTransactionId) {
        BankService.currentTransactionId = currentTransactionId;
    }
}
