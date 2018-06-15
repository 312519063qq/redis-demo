package com.gsj.rediswatch.controller;

import com.gsj.rediswatch.model.Persion;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class Test {
    public static void main(String[] args) {

        List<Persion> javaProgrammers = new ArrayList<Persion>() {
            {
                add(new Persion("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Persion("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Persion("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Persion("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Persion("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Persion("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Persion("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Persion("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Persion("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Persion("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };

        List<Persion> phpProgrammers = new ArrayList<Persion>() {
            {
                add(new Persion("Jarrod", "Pace", "PHP programmer", "male", 34, 1550));
                add(new Persion("Clarette", "Cicely", "PHP programmer", "female", 23, 1200));
                add(new Persion("Victor", "Channing", "PHP programmer", "male", 32, 1600));
                add(new Persion("Tori", "Sheryl", "PHP programmer", "female", 21, 1000));
                add(new Persion("Osborne", "Shad", "PHP programmer", "male", 32, 1100));
                add(new Persion("Rosalind", "Layla", "PHP programmer", "female", 25, 1300));
                add(new Persion("Fraser", "Hewie", "PHP programmer", "male", 36, 1100));
                add(new Persion("Quinn", "Tamara", "PHP programmer", "female", 21, 1000));
                add(new Persion("Alvin", "Lance", "PHP programmer", "male", 38, 1600));
                add(new Persion("Evonne", "Shari", "PHP programmer", "female", 40, 1800));
            }
        };
//        javaProgrammers.forEach((persion -> {
//            System.out.println("persion.getFirstName() = " + persion.getFirstName());
//        }));
//        System.out.println("\"=============================================\" = " + "=============================================");
//        phpProgrammers.forEach((persion -> { System.out.println();}));
//        phpProgrammers.forEach(new Consumer<Persion>() {
//            @Override
//            public void accept(Persion persion) {
//                persion.setSalary((int) (persion.getSalary()*1.05));
//            }
//        });
//        phpProgrammers.forEach((persion -> {persion.setSalary((int) (persion.getSalary()*1.05));}));
//        phpProgrammers.forEach((p)->{ System.out.println("p.getSalary() = " + p.getSalary()); });
//        phpProgrammers.stream().filter(new Predicate<Persion>() {
//            @Override
//            public boolean test(Persion persion) {
//                return persion.getSalary()>1400;
//            }
//        }).forEach(new Consumer<Persion>() {
//            @Override
//            public void accept(Persion persion) {
//                System.out.println("persion.getFirstName() = " + persion.getFirstName());
//            }
//        });
//        System.out.println("\"**********************************\" = " + "**********************************");
//        phpProgrammers.stream().filter((persion ->(persion.getSalary()>1400)))
//                                .forEach((persion)-> {System.out.println("persion = " + persion.getFirstName());});
//        System.out.println("下面是年龄大于 24岁且月薪在$1,400以上的女PHP程序员:");
//        phpProgrammers.stream().filter(new Predicate<Persion>() {
//            @Override
//            public boolean test(Persion persion) {
//                return persion.getAge()>24;
//            }
//        }).filter(new Predicate<Persion>() {
//            @Override
//            public boolean test(Persion persion) {
//                return persion.getSalary()>1400;
//            }
//        }).filter(new Predicate<Persion>() {
//            @Override
//            public boolean test(Persion persion) {
//                return "female".equals(persion.getGender());
//            }
//        }).forEach(new Consumer<Persion>() {
//            @Override
//            public void accept(Persion persion) {
//                System.out.println("persion.getFirstName() = " + persion.getFirstName());
//            }
//        });
//        System.out.println("\"===============================================================\" = " + "===============================================================");
//
//        phpProgrammers.stream().filter((persion)-> persion.getAge()>2)
//                                .filter((persion -> persion.getSalary()>1400))
//                                .filter((persion -> "female".equals(persion.getGender())))
//                                .forEach((persion -> System.out.println(persion.getFirstName())));
//        System.out.println("\"*****************************************************************\" = " + "*****************************************************************");
//        phpProgrammers.stream().filter((persion -> {return persion.getAge()>2;}))
//                                .limit(3)
//                                .filter((persion -> {return persion.getSalary()>1400;}))
//                                .filter((persion -> {return "female".equals(persion.getGender());}))
//                                .forEach((persion -> {System.out.println(persion.getFirstName());}));
//        System.out.println("根据 name 排序,并显示前5个 Java programmers:");
//        phpProgrammers.stream().sorted(new Comparator<Persion>() {
//            @Override
//            public int compare(Persion o1, Persion o2) {
//                return o1.getFirstName().compareTo(o2.getFirstName());
//            }
//        })
//        .limit(5)
//        .forEach(new Consumer<Persion>() {
//            @Override
//            public void accept(Persion persion) {
//                System.out.println(persion.getFirstName());
//            }
//        });
//        System.out.println("\"********************************************\" = " + "********************************************");
//        phpProgrammers.stream().sorted((o1,o2)->{return o1.getFirstName().compareTo(o2.getFirstName());})
//                                .limit(5)
//                                .forEach((persion -> {System.out.println(persion.getFirstName()); }));
//        System.out.println("\"================================================\" = " + "================================================");
//        phpProgrammers =  phpProgrammers.stream().sorted((o1,o2)->(o1.getFirstName().compareTo(o2.getFirstName())))
//                               .limit(5)
//                                .collect(Collectors.toList());
//        phpProgrammers.forEach(System.out::println);
//        System.out.println("工资最低的 Java programmer:");
//        System.out.println("工资最低的 = " + phpProgrammers.stream().min(new Comparator<Persion>() {
//            @Override
//            public int compare(Persion o1, Persion o2) {
//                return o1.getSalary()-o2.getSalary();
//            }
//        }).get().getFirstName());
//        System.out.println("\"===============================================\" = " + "===============================================");
//        System.out.println("工资最低的 = " + phpProgrammers.stream().min((o1,o2)->{return o1.getSalary()-o2.getSalary();})
//                .get().getFirstName());
//        System.out.println("将 PHP programmers 的 first name 拼接成字符串:");
//        String phpDevelopers = phpProgrammers
//                .stream()
//                .map(Persion::getFirstName)
//                .collect(joining(" ; "));
//        System.out.println("phpDevelopers = " + phpDevelopers);
//
//
//        Set<Object> phpDevelopers2 =  phpProgrammers.stream().map(new Function<Persion, Object>() {
//            @Override
//            public Object apply(Persion persion) {
//                return persion.getFirstName();
//            }
//        }).collect(toSet());
//        System.out.println("phpDevelopers2 = " + phpDevelopers2);
//
//        Object phpDevelopers3 =  phpProgrammers.stream().map(new Function<Persion, String>() {
//            @Override
//            public String apply(Persion persion) {
//                return persion.getFirstName();
//            }
//        }).collect(joining(" ; "));
        List<String> idStr = new ArrayList<>();
        idStr.add("1");
        idStr.add("2");
        idStr.add("3");
        idStr.add("4");
        idStr.add("5");
        idStr.stream().map(Long::valueOf).collect(toList()).forEach((id)->{
            System.out.println("idStr = " + id);
        });



    }
}

