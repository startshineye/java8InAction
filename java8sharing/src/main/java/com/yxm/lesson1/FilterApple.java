package com.yxm.lesson1;
import com.yxm.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class FilterApple {

    //3.匿名内部类调用
    public interface AppleFilter{
        boolean filter(Apple apple);
    }

    public static class GreenAnd160WeightFilter implements AppleFilter {
        @Override
        public boolean filter(Apple apple) {
            return (apple.getColor().equals("green") && apple.getWeight() >= 160);
        }
    }

    public static class YellowLess150WeightFilter implements AppleFilter {
        @Override
        public boolean filter(Apple apple) {
            return (apple.getColor().equals("yellow") && apple.getWeight() < 150);
        }
    }

    public static List<Apple> findApples(List<Apple> appleList,AppleFilter appleFilter){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:appleList){
            if(appleFilter.filter(apple)){
                result.add(apple);
            }
        }
        return result;
    }

    //1. 初试牛刀：筛选绿苹果
    public static List<Apple> findGreenApples(List<Apple> appleList){
        /**
         * 累积苹果的列表
         */
        List<Apple> result = new ArrayList<>();
        /**
         * 仅仅选出绿苹果
         */
         for (Apple apple:appleList){
             if("green".equals(apple.getColor())){
                 result.add(apple);
             }
         }
         return result;
    }

    //2. 再展身手：把颜色作为参数
    public static List<Apple> findApples(List<Apple> appleList,String color){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:appleList){
            if(color.equals(apple.getColor())){
                result.add(apple);
            }
        }
        return result;
    }


    public static void main(String[] args){
        /**
         * 苹果仓储
         */
        List<Apple> storeList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));
        List<Apple> greenApples = findGreenApples(storeList);
        System.out.println(greenApples);

        //=============================================================================================================
        List<Apple> greenApples2 = findApples(storeList, "green");
        List<Apple> redApples2 = findApples(storeList, "red");
        System.out.println("green:"+greenApples2);
        System.out.println("red:"+redApples2);

        //=============================================================================================================


        List<Apple> apples = findApples(storeList, new GreenAnd160WeightFilter());
        //3.匿名内部类调用
        List<Apple> yellowApples = findApples(storeList, new AppleFilter() {
            @Override
            public boolean filter(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });
        System.out.println("redApples:"+yellowApples);
    }
}
