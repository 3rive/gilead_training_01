package com.gilead.main;

public class Calculator extends AbstractOperations implements Operations{

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public String add(String a, String b) {
        return "I am calculator not a String calculator kindly pass Integer!!!";
    }

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public Integer subtract(Integer a, Integer b) {
        return a-b;
    }
}
