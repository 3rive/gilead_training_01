package com.gilead.collections.training;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Student student1 = createStudent(1,"Bevin","Male");
        Student student2 = createStudent(2,"Abinaya","Female");
        Student student3 = createStudent(3,"Arthi","Female");
        Student student4 = createStudent(4,"Denisha","Female");
        Student student5 = createStudent(5,"Alan","Male");
        Student student6 = createStudent(2,"Thanasekar","Male");
        List<Student> students_of_12 = new ArrayList<Student>();
        students_of_12.add(student1);
        students_of_12.add(student2);
        students_of_12.add(student3);
        students_of_12.add(student4);
        students_of_12.add(student5);
        students_of_12.add(student6);
        System.out.println("Names of Students in XII- B:  ");
        for (Student student_of_12 : students_of_12){
            System.out.print(student_of_12.getPerson().getName()+" | ");
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
