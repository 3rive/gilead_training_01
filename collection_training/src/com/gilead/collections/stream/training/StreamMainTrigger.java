package com.gilead.collections.stream.training;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamMainTrigger {
    public static void main(String[] args) {
        demoStream();
        Stream<Integer> intStream1 = Stream.of(1, 2, 3, 4, 5, 6, 7);
        List<Integer> squareList = intStream1.map(x -> x * x).toList();
        Stream<Integer> intStream2 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> evenList = intStream2.filter(x -> x % 2 == 0).toList();
        squareList.forEach(p -> System.out.print(p + " | "));
        System.out.println();
        evenList.forEach(p -> System.out.print(p + " | "));
        System.out.println();
        //For string
        Stream<String> alphabets = Stream.of("abc","ball","cat","dog","zebra","xylaphone","Cheetah");
        List<String> sortalphabets = alphabets.map(String::toUpperCase).sorted().toList();
        sortalphabets.forEach(p -> System.out.print(p + " | "));
        System.out.println();
        Stream<String> birds = Stream.of("Albatross","BulBul","Crow","Duck","Zylus","Xano","Cuckoo","Chicken","Crane");
        List<String> filteredBirdsWithC = birds.filter(x -> x.toUpperCase().startsWith("C")).sorted().toList();
        filteredBirdsWithC.forEach(p -> System.out.print(p + " | "));
    }

    private static void demoStream() {
        Stream<Integer> intStream = Stream.of(1, 2, 3, 4, 5, 6, 7);
        //print from stream
        intStream.forEach(p -> System.out.print(p + " | "));
        System.out.println("Printed from Integer Stream");

        Stream<String> stringStream = Stream.of("Raja", "Pandian");
        stringStream.forEach(p -> System.out.print(p + " | "));

        System.out.println("Printed from String Stream");
        List<Integer> listOfInteger = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            listOfInteger.add(i);
        }
        listOfInteger.forEach(p -> System.out.print(p + " | "));
        System.out.println("Printed from List");
    }
}
