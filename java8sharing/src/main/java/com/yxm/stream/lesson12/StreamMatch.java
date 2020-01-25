package com.yxm.stream.lesson12;

import java.util.Arrays;
import java.util.List;

public class StreamMatch {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        //1.allMatch:所有元素都大于
        boolean allMatch = list.stream().allMatch(i -> i > 0);
        System.out.println("allMatch:"+allMatch);//allMatch:true

        //2.anyMatch:存在元素大于
        boolean anyMatch = list.stream().anyMatch(i -> i > 9);
        System.out.println("anyMatch:"+anyMatch);//anyMatch:false

        //3.noneMatch:没有一个元素大于10
        boolean noneMatch = list.stream().noneMatch(i -> i > 10);
        System.out.println("noneMatch:"+noneMatch); //noneMatch:true
    }
}
