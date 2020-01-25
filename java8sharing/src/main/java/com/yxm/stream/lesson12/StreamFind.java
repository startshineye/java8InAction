package com.yxm.stream.lesson12;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class StreamFind {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Optional<Integer> optional = list.stream().filter(i -> i % 2 == 0).findAny();
        System.out.println(optional.get());

        Optional<Integer> optional1 = list.stream().filter(i -> i < 10).findAny();
        System.out.println(optional1.orElse(-1));

        Optional<Integer> optional2 = list.stream().filter(i -> i % 2 == 0).findFirst();
        optional2.ifPresent(System.out::println);

        System.out.println(optional2.filter(i -> i == 2).get());

        int result = find(new Integer[]{1, 2, 3, 4, 5, 6, 7}, -1, i -> i < 10);
        System.out.println(result);
    }

    private static int find(Integer[] values, int defaultValue, Predicate<Integer> predicate) {
        for (int i : values) {
            if (predicate.test(i))
                return i;
        }
        return defaultValue;
    }
}
