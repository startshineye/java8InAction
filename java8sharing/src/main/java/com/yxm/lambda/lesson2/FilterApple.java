package com.yxm.lambda.lesson2;

import com.yxm.lambda.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterApple {

    //3.匿名内部类调用
    @FunctionalInterface
    public interface AppleFilter{
        boolean filter(Apple apple);
    }

    public static List<Apple> findApples(List<Apple> appleList, AppleFilter appleFilter){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:appleList){
            if(appleFilter.filter(apple)){
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception{
        /**
         * 苹果仓储
         */
        List<Apple> storeList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

       /* List<Apple> innerApples = findApples(storeList, new AppleFilter() {
            @Override
            public boolean filter(Apple apple) {
                return "green".equals(apple.getColor());
            }
        });
        System.out.println("innerApples:"+innerApples);

        List<Apple> apples = findApples(storeList, (Apple apple) -> {
            return "green".equals(apple.getColor());
        });

        System.out.println("lambda:"+apples);*/

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("本线程名称:"+Thread.currentThread().getName());
            }
        }).start();

        new Thread(()-> System.out.println("本线程名称:"+Thread.currentThread().getName())).start();

        Thread.currentThread().join();

    }
}
