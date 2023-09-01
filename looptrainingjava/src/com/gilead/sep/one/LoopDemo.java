package com.gilead.sep.one;

public class LoopDemo {
    public static void main(String[] args) {
        ForRepeater r = new ForRepeater();
        r.printTimes(3);
        System.out.println();
        WhileRepeater w = new WhileRepeater();
        w.printTimes(3);
        System.out.println();
        DoWhileRepeater dw = new DoWhileRepeater();
        dw.printTimes(3);
        System.out.println();
        EnhancedForLoopRepeaterInt enFI = new EnhancedForLoopRepeaterInt();
        enFI.printTimes();
        System.out.println();
        EnhancedForLoopRepeaterString enFIS = new EnhancedForLoopRepeaterString();
        enFIS.printTimes();
        System.out.println();
        HalfLoopRepeater hlf = new HalfLoopRepeater();
        hlf.printHalfTimes(10);
        System.out.println();
        TwiceLoopIterator tli = new TwiceLoopIterator();
        tli.printTwiceTimes(5);
        System.out.println();
    }
}
