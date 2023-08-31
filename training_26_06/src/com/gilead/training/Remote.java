package com.gilead.training;

public class Remote {
    public static void main(String[] args) throws InterruptedException {
        /*
        Fan fan = new Fan();
        fan.on();
        run();
        fan.off();
        Television tv = new Television();
        tv.on();
        run();
        tv.off();
        */

        Person aarthi = new Person();
        //instance out of the blueprint  --> Person 1
        aarthi.setFirstname("Arthi");
        aarthi.setLastname("Murugesan");
        aarthi.setAge(27);

        //instance out of the blueprint  --> Person 2
        Person jency = new Person();
        jency.setFirstname("Jency");
        jency.setLastname("Saral Jayakumar");
        jency.setAge(21);

        System.out.println("Age of Arthi is --> "+aarthi.getAge());
        System.out.println("Age of Jency is --> "+jency.getAge());

    }

    private static void run() throws InterruptedException {
        System.out.print("Running");
        int i;
        for (i = 0; i < 50; i++) {
            System.out.print(".");
            Thread.sleep(500);
        }
        System.out.print("\n");
    }

}
