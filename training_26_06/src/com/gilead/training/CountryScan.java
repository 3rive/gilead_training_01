package com.gilead.training;

import com.gilead.training.scanner.Country;

import java.util.Scanner;

public class CountryScan {
        public static void main(String[] args) {
        Country country = new Country();
        Scanner scanner = new Scanner(System.in); //takes input from the console
        //scan the console
        country.setName(scanner.next());
        country.setPopulation(scanner.nextInt());
        country.setArea(scanner.nextFloat());
        System.out.println("Name of the country is :"+country.getName());
        System.out.println("Population of "+country.getName()+ " is :"+country.getPopulation());
        System.out.println("Area of "+country.getName()+ " is :"+country.getArea());
    }
}
