package com.yxm.stream.lesson11;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class StreamFilter {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 6, 7, 7, 1);

        //1.filter过滤
        List<Integer> filterCollect = list.stream().filter(i -> i % 2 == 0).collect(toList());
        System.out.println("filterCollect:"+filterCollect);  //filterCollect:[2, 4, 6, 6]

        //2.去重
        List<Integer> distinctCollect = list.stream().distinct().collect(toList());
        System.out.println("distinctCollect:"+distinctCollect);  //distinctCollect:[1, 2, 3, 4, 5, 6, 7]

        //3.跳过前面几个元素
        List<Integer> skipCollect = list.stream().skip(5).collect(toList());
        System.out.println("skipCollect:"+skipCollect); //skipCollect:[6, 6, 7, 7, 1]

        //4.取前面几个元素
        List<Integer> limitCollect = list.stream().limit(5).collect(toList());
        System.out.println("limitCollect:"+limitCollect);  //limitCollect:[1, 2, 3, 4, 5]
    }
}
