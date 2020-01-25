package com.yxm.stream.lesson6;

import com.yxm.stream.Dish;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class SimpleStream {
    public static void main(String[] args){
        //1.创建一个集合
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));
        //1.集合操作
     /*   List<String> dishNamesByCollections = getDishNamesByCollections(menu);
        System.out.println("dishNamesByCollections:"+dishNamesByCollections);

        //2.流操作
        List<String> dishNamesByStream = getDishNamesByStream(menu);
        System.out.println("dishNamesByStream:"+dishNamesByStream);*/

        //3.流只能遍历一次
        Stream<Dish> stream = menu.stream();
        stream.forEach(System.out::println);
        /*stream.forEach(System.out::println);*/



    }

    public static List<String> getDishNamesByStream(List<Dish> menu){
        List<String>  result = new ArrayList<>();
       return menu.parallelStream().filter(
               (d)->{
                   try {
                       Thread.sleep(100000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   return d.getCalories()<400;
               }).sorted(Comparator.comparing((d)->d.getCalories())).map(Dish::getName).collect(toList());
    }

    public static List<String> getDishNamesByCollections(List<Dish> menu){
        List<String> result = new ArrayList<>();

        List<Dish> lowerMenu = new ArrayList<>();
        //1.获取卡里绿小于400的菜肴
        for (Dish dish:menu){
            if(dish.getCalories()<400){
                lowerMenu.add(dish);
            }
        }

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //2.排序
        Collections.sort(lowerMenu,(d1,d2)->Integer.compare(d1.getCalories(),d2.getCalories()));

        //3.获取名称
        for(Dish dish:lowerMenu){
            result.add(dish.getName());
        }
        return result;
    }
}
