package com.yxm.lesson5;

import com.yxm.Apple;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class MethodReference {

    public static void main(String[] args){
        //1.书写lambda表达式，然后在其他方法中调用
        Consumer<String> consumer = (s)->System.out.println(s);
        useConsumer(consumer,"Hello Tom");

        //2.直接使用lambda表达式
        useConsumer((s)->System.out.println(s),"Hello Tom");

        //3.使用函数式签名
        useConsumer(System.out::println,"Hello Tom");

        //4.使用lambda表达式对集合进行排序
        List<Apple> appleList = Arrays.asList(new Apple("Green", 100), new Apple("red", 200), new Apple("blue", 300));
        System.out.println("appleList before sort:"+appleList);
        appleList.sort((a1,a2)->a1.getColor().compareTo(a2.getColor()));
        System.out.println("appleList after sort:"+appleList);

        //5.循环遍历打印出appleList
        appleList.stream().forEach(System.out::println);

        //类::静态方法
        Function<String, Integer> f = Integer::parseInt;
        Integer apply1 = f.apply("123");
        System.out.println(apply1);

        //类::实例方法
        BiFunction<String, Integer, Character> f2 = String::charAt;
        Character apply2 = f2.apply("Hello", 2);
        System.out.println("f2:"+apply2);

        //对象::实例
        String str = new String("Hello");
        Function<Integer, Character> f3 = str::charAt;
        Character apply3 = f3.apply(2);
        System.out.println("f3:"+apply3);

        BiFunction<String, Integer, Apple> appleBiFunction = Apple::new;
        Apple apple = appleBiFunction.apply("red", 100);
        System.out.println("appleBiFunction:"+apple);
    }



    private static <T> void useConsumer(Consumer<T> consumer,T t){
        System.out.println("====useConsumer====start");
        consumer.accept(t);
        consumer.accept(t);
        System.out.println("====useConsumer====end");
    }

}
