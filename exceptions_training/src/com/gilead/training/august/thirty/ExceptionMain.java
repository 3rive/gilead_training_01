package com.gilead.training.august.thirty;

public class ExceptionMain {
    public static void main(String[] args) {
        //array
        //a[0] - a[1] - a[2] ......
        int a[] = new int[5];
        try{//
            a[0] = 50;
            a[2] = 22;
            a[3] = 55;
            a[7] = 88;   //quit
        }
        catch (Exception e){
           e.printStackTrace();
        }

        System.out.println(a[0]);

    }
}