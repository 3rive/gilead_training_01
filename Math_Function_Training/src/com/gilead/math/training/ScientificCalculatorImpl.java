package com.gilead.math.training;

public class ScientificCalculatorImpl implements ScientificCalculator,Calculator{
    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public int add(int a, int b) {
        return a+b;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public int sub(int a, int b) {
        return a-b;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public int mul(int a, int b) {
        return a*b;
    }

    /**
     * @param a
     * @param b
     * @return
     */
    @Override
    public double div(double a, double b) {
        return a/b;
    }

    /**
     * @return
     */
    @Override
    public double pi() {
        return 3.14;
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double log(int a) {
        return Math.log(a);
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double sin(double a) {
        return Math.sin(a);
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double cos(double a) {
        return Math.cos(a);
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double tan(double a) {
        return Math.tan(a);
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double square(double a) {
        return a*a;
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double sqrt(double a) {
        return Math.sqrt(a);
    }

    /**
     * @param a
     * @return
     */
    @Override
    public double exp(int a) {
        return Math.exp(a);
    }
}
