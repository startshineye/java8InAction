package com.yxm.stream.lesson12;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StreamReduce {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        Integer result = list.stream().reduce(0, (i, j) -> i + j);
        System.out.println(result); //28

        list.stream().reduce((i, j) -> i + j).ifPresent(System.out::println); //28

        list.stream().reduce(Integer::max).ifPresent(System.out::println); //7


        list.stream().reduce(Integer::min).ifPresent(System.out::println); //1

        list.stream().reduce((i, j) -> i > j ? j : i).ifPresent(System.out::println); //1


        int result2 =  list.stream().filter(i -> i % 2 == 0).reduce(1, (i, j) -> i * j);

        Optional.of(result2).ifPresent(System.out::println); //48

    }
}
