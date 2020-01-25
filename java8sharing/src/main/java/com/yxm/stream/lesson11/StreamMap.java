package com.yxm.stream.lesson11;

import com.yxm.stream.Dish;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class StreamMap {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 6, 7, 7, 1);

        //1.获取偶数
        List<Integer> collect = list.stream().map((i) -> i * 2).collect(toList());

        //2.返回Dish的菜名
        List<Dish> dishList = getDishList();
        dishList.stream().map(d->d.getName()).forEach(System.out::println);
        List<String> mapCollect1 = dishList.stream().map(d -> d.getName()).collect(toList());
        System.out.println(mapCollect1); //[pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon]

        //3.去除单词中相同的符号,并以字符串输出  
        String[] words = {"Hello","World"};

        //{H,e,l,l,o,W,o,r,l,d};
        Stream<String[]> stream = Arrays.stream(words).map(w -> w.split(""));
        //H,e,l,l,o,W,o,r,l,d
        Stream<String> stringStream = stream.flatMap(Arrays::stream);
        stringStream.distinct().forEach(System.out::println);

    }

    private static List<Dish>  getDishList(){
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
        return menu;
    }
}
