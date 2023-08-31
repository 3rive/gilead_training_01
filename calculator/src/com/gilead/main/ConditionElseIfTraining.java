package com.gilead.main;

public class ConditionElseIfTraining {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();
        account.setBalance(100.00);
        Double bankBalance = account.getBalance(); //Camel casing
        System.out.println("Current Balance ----> : "+bankBalance);
        String economicStatus;
        //string can be instantiated in 2 ways
        String myString = new String("Raja");   //object pool --> Do not recommend
        System.out.println(myString);
        String myString2 = "Raja";  //string pool  ---> Specialized memory
        System.out.println(myString);
        //float
        float someFloatValue = 1;
        if (bankBalance == 0.0) {
            economicStatus = "VERY POOR";
        } else if (bankBalance > 0.0 && bankBalance <= 100.0) {
            economicStatus = "POOR";
        } else if (bankBalance > 100.0 && bankBalance <= 1000.0) {
            economicStatus = "MODERATELY POOR";
        } else {
            economicStatus = "RICH";
        }
        System.out.println("The economic status of the person is ----->" + economicStatus);
        switch (economicStatus){
            case "VERY POOR":
                System.out.println("You are very very much below Poverty line :( :( :(");
                break;
            case "POOR":
                System.out.println("You are very much below Poverty line :( :(");
                break;
            case "MODERATELY POOR":
                System.out.println("You are below Poverty line :(");
                break;
            default :
                System.out.println("You are above Poverty line :)");
                break;
        }

    }


}
