package com.gilead.main;

public class StringCalculator extends AbstractOperations implements Operations{

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public String add(String a, String b) {
        return a+b;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public Integer subtract(Integer a, Integer b) {
        return 1;
    }
}
