package com.gilead.collections.training;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Student student1 = createStudent(1,"Bevin","Male");
        Student student2 = createStudent(2,"Abinaya","Female");
        Student student3 = createStudent(3,"Arthi","Female");
        Student student4 = createStudent(4,"Denisha","Female");
        Student student5 = createStudent(5,"Alan","Male");
        Student student6 = createStudent(2,"Thanasekar","Male");
        //ArrayList --. Dynamic Array --> List can allow duplicate objects
/*
        List<Student> students_of_12 = new ArrayList<Student>();
        students_of_12.add(student1);
        students_of_12.add(student2);
        students_of_12.add(student3);
        students_of_12.add(student4);
        students_of_12.add(student5);
        students_of_12.add(student6);
        students_of_12.add(null);
        students_of_12.add(null);
        students_of_12.add(null);
        students_of_12.add(null);
        students_of_12.add(null);

        System.out.println("Names of Students in XII- B through list:  ");
        for (Student student_of_12 : students_of_12){
            System.out.print(students_of_12.size());
        System.out.println();
       }

        //HashSet --> Collection Object --> Has unique objects
        HashSet<Student> set_of_students = new HashSet<Student>();
        set_of_students.add(student1);
        set_of_students.add(student2);
        set_of_students.add(student3);
        set_of_students.add(student4);
        set_of_students.add(student5);
        set_of_students.add(student6);
        set_of_students.add(null);
        set_of_students.add(null);
        set_of_students.add(null);
        set_of_students.add(null);

        System.out.println("Names of Students in XII- B through set:  ");
        for (Student student_of_12 : set_of_students){
            System.out.print(set_of_students.size());
        System.out.println();
        }
        */
        HashMap<String,Student> studentMap = new HashMap<String,Student>();
        //inserting data
        studentMap.put(student1.getPerson().getName(),student1);
        studentMap.put(student2.getPerson().getName(),student2);
        studentMap.put(student3.getPerson().getName(),student3);
        studentMap.put(student4.getPerson().getName(),student4);
        studentMap.put(student5.getPerson().getName(),student5);
        studentMap.put(student5.getPerson().getName(),student6);
        List<Student> studentList = new ArrayList<Student>();
        for(Map.Entry<String,Student> entry : studentMap.entrySet()){
            System.out.println("key :: "+entry.getKey()+" value :: "+entry.getValue());
            studentList.add(entry.getValue());
        }
        for (Student student_of_12 : studentList){
            System.out.println(" +++ " + student_of_12.getPerson().getName());
        }

    }

    private static Student createStudent(int rollNo, String name, String gender) {
        Person person = new Person();
        person.setName(name);
        person.setAge(23);
        person.setGender(gender);
        return new Student(rollNo,"XII","B","Day-Scholar","Bio-Maths",person);
    }
}
