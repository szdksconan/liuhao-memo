package com.liuhao.lambdas;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LambdasLearn {

    static {
        List<Person> javaProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };

        List<Person> phpProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));
                add(new Person("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));
                add(new Person("Victor", "Channing", "PHP programmer", "male", 32, 1600));
                add(new Person("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));
                add(new Person("Osborne", "Shad", "PHP programmer", "male", 32, 1100));
                add(new Person("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));
                add(new Person("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));
                add(new Person("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));
                add(new Person("Alvin", "Lance", "PHP programmer", "male", 38, 1600));
                add(new Person("Evonne", "Shari", "PHP programmer", "female", 40, 1800));
            }
        };


        System.out.println("所有程序员的姓名:");
        javaProgrammers.forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));
        phpProgrammers.forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));


        System.out.println("给程序员加薪 5% :");
        javaProgrammers.forEach((p) -> p.setSalary(p.getSalary() + p.getSalary() / 100 * 5));


        System.out.println("下面是月薪超过 $1,400 的PHP程序员:");
        phpProgrammers.stream().filter((p)->(p.getSalary()>1400)).forEach((p)-> System.out.printf("%s %s;",p.getFirstName(),p.getLastName()));


        System.out.println("下面是年级大于5 ，月薪超过 $1,400 女的PHP程序员:");
        Predicate<Person> ageFilter = (p)->p.getAge()>5;
        Predicate<Person> salary = (p)->p.getSalary()>1400;
        Predicate<Person> genderFilter = (p)->"female".equals(p.getGender());

        phpProgrammers.stream().filter(ageFilter)
                .filter(salary)
                .filter(genderFilter)
                .forEach((p) -> System.out.printf("%s %s;", p.getFirstName(), p.getLastName()));


        System.out.println("最前面的3个 Java programmers:");
        javaProgrammers.stream()
                .limit(3)
                .forEach((p) -> System.out.printf("%s %s; ", p.getFirstName(), p.getLastName()));



        System.out.println("根据 name 排序,并显示前5个 Java programmers:");
        List<Person> sortedJavaProgrammers = javaProgrammers.stream()
                .sorted((p,p1)->p.getFirstName().compareTo(p.getFirstName()))
                .limit(5)
                .collect(Collectors.toList());

        sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));


        System.out.println("根据 salary 排序 Java programmers:");
        sortedJavaProgrammers = javaProgrammers
                .stream()
                .sorted( (p, p2) -> (p.getSalary() - p2.getSalary()) )
                .collect(Collectors.toList());

        sortedJavaProgrammers.forEach((p) -> System.out.printf("%s %s; %n", p.getFirstName(), p.getLastName()));


        System.out.println("工资最低的 Java programmer:");
        Person salaryMinPerson = javaProgrammers.stream()
                .min((p,p1)->p.getSalary()-p1.getSalary())
                .get();

        System.out.printf("Name: %s %s; Salary: $%,d.", salaryMinPerson.getFirstName(), salaryMinPerson.getLastName(), salaryMinPerson.getSalary());



        System.out.println("将 PHP programmers 的 first name 拼接成字符串:");
        String phpProgrammerString = phpProgrammers.stream()
                .map(Person::getFirstName)
                .collect(Collectors.joining(";"));
        System.out.println("phpProgrammerString: "+phpProgrammerString);
    }

    public static void main(String[] args) {

    }

}
