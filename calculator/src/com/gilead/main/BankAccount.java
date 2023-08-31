package com.gilead.main;

public class BankAccount {

    public Double balance = 1000.0;
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void depositAmount(Double amount){
        setBalance(amount);
    }

    public boolean isNilBalance(Double balance){
        if(balance == 0.0) {
            System.out.println("You are a Pauper !!!");
            return true;
        }
        else {
            System.out.println("You seem to be rich !!!");
            return false;
        }
    }

}
