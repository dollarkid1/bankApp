package com.maven.bank;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Customer {
    private long bvn;
    private String firstName;
    private String surname;
    private String email;
    private String phone;
    private String password;

    public String getEmail() {
        return email;
    }

    private List<Account> accounts = new ArrayList<> ();

    public long getBvn() {
        return bvn;
    }

    public void setBvn(long bvn) {
        this.bvn = bvn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail(String s) {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List getAccounts() {
        return accounts;
    }
}
