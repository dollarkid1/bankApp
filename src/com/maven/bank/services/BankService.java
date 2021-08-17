package com.maven.bank.services;

public class BankService {
    private static long currentBVN = 2;
    private static long currentAccountNumber = 0000110003;
    public static long generateBvn(){
        currentBVN++;
        return currentBVN;
    }

    public static long generateAccountNumber(){
        currentAccountNumber++;
        return currentAccountNumber;

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
}
