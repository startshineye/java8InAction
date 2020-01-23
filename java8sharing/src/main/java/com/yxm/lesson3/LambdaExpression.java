package com.yxm.lesson3;
import com.yxm.Apple;
import com.yxm.lesson2.FilterApple;
import jdk.management.resource.ResourceId;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaExpression {


    public static void main(String[] args){
        List<Apple> storeApples = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        //1.匿名内部类
        Comparator<Apple> comparator1 = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getColor().compareTo(o2.getColor());
            }
        };

        //3.lambda表达式1
        Comparator<Apple> comparator3 = (o1,o2) -> o1.getColor().compareTo(o2.getColor());

        List<Apple> list = Collections.emptyList();
        list.sort(comparator3);

        // 带返回值的Function
        Function<String,Integer> stringConsumer = (String s) -> s.length();

        //boolean判断
        Predicate<Apple> green = (Apple a) -> a.getColor().equals("green");

        ResourceId resourceId = () -> "hello world";




    }
}
