package com.example.creditservice;

import java.io.Serializable;

public class Debtor implements Serializable {

    private  int id;
    private  String firstName,lastName,status;
    private  int credit;

    int position;

    public Debtor(int id, String firstName, String lastName,  int credit,String status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.credit = credit;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public int getCredit() {
        return credit;
    }




}
