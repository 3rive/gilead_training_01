package com.gilead.training.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class CountryScanner {

    public static void main(String[] args) throws FileNotFoundException {
        Country country = new Country();
        Scanner scanner = new Scanner(System.in); //takes input from the console
        //scan the console
        country.setName(scanner.next());
        country.setPopulation(scanner.nextInt());
        country.setArea(scanner.nextFloat());
        File file = new File("C:\\Users\\HP\\Documents\\sample.csv");
        PrintStream pStream = new PrintStream(file);
        System.setOut(pStream);
        System.out.println("Name of the country is :"+country.getName());
        System.out.println("Population of "+country.getName()+ " is :"+country.getPopulation());
        System.out.println("Area of "+country.getName()+ " is :"+country.getArea());
    }
}
