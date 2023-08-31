package com.gilead.main;

public class ConditionTraining {

    public static void main(String[] args) {

        BankAccount bankAccount = new BankAccount();
        System.out.println(bankAccount.getBalance());
        if(bankAccount.isNilBalance(bankAccount.getBalance())){
            System.out.println("You are now Poor :(");
            bankAccount.depositAmount(1000.00);
            System.out.println(bankAccount.getBalance()+" Rs is now deposited");
        }

        if(!bankAccount.isNilBalance(bankAccount.getBalance())){
            System.out.println("You are no more poor :)");
        }

    }
}
