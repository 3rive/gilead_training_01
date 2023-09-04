package com.gilead.math.training;

public class Main {
    public static void main(String[] args) {
       ScientificCalculator casio = new ScientificCalculatorImpl();
       System.out.println("PI:::"+casio.pi());
        System.out.println("sqrt:::"+casio.sqrt(2.2));
        System.out.println("cos:::"+casio.cos(60.0));
        System.out.println("log:::"+casio.log(2));
    }
}
