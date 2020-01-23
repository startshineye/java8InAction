package com.yxm.lesson4;

import com.yxm.Apple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

public class LambdaUsage {

    private static List<Apple> findApples(List<Apple> list, Predicate<Apple> predicate){
        List<Apple> result = new ArrayList<>();
         for(Apple apple:list){
             if(predicate.test(apple)){
              result.add(apple);
             }
         }
        return result;
    }

    private static List<Apple> findApplesByLongPredicate(List<Apple> list, LongPredicate predicate){
        List<Apple> result = new ArrayList<>();
         for(Apple apple:list){
             if(predicate.test(apple.getWeight())){
              result.add(apple);
             }
        }
        return result;
    }

    private static List<Apple> findApplesByBiPredicate(List<Apple> list, BiPredicate<String,Long> biPredicate){
        List<Apple> result = new ArrayList<>();
        for(Apple apple:list){
            if(biPredicate.test(apple.getColor(),apple.getWeight())){
                result.add(apple);
            }
        }
        return result;
    }

    private static List<Apple> simpleConsumer(List<Apple> list, Consumer<Apple> consumer){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:list){
            consumer.accept(apple);
            result.add(apple);
        }
       return result;
    }

    private static List<Apple> simpleBiConsumer(List<Apple> list, BiConsumer<Apple,Long> biConsumer){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:list){
            biConsumer.accept(apple,apple.getWeight());
            result.add(apple);
        }
        return result;
    }

    private static Apple testFunction(Apple apple, Function<Apple,String> function){
        function.apply(apple);
        return apple;
    }

    private static Apple createApple(Supplier<Apple> supplier) {
        return supplier.get();
    }

    public static void main(String[] args){
        List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        Apple green = createApple(() -> new Apple("green", 100));
        System.out.println("green:"+green);


       /* Apple apple = testFunction(new Apple("yellow", 200), (a) -> a.toString());
        System.out.println("functionApple:"+apple);*/


       /* List<Apple> consumerList = simpleConsumer(appleList, (apple) -> System.out.println(apple));
        System.out.println("consumerList:"+consumerList);

        List<Apple> simpleBiConsumer = simpleBiConsumer(appleList, (a, s) -> System.out.println(s + a.getColor() + ":Weight=>" + a.getWeight()));
        System.out.println("simpleBiConsumer:"+simpleBiConsumer);
*/




        /*List<Apple> greenList = findApples(appleList, (apple) -> apple.getColor().equals("green"));//boolean判断
        System.out.println("Predicate:"+greenList);

        List<Apple> applesByLongPredicate = findApplesByLongPredicate(appleList, (w) -> w > 160);
        System.out.println("LongPredicate:"+applesByLongPredicate);

        List<Apple> applesByBiPredicate = findApplesByBiPredicate(appleList, (s, w) -> s.equals("green") && w > 100);
        System.out.println("BiPredicate:"+applesByBiPredicate);*/


        //lambada表达式就是:函数式接口的实例

        //1.定义一个lambda表达式:
       /* Runnable r1 = () -> System.out.println("Hello");

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };
        process(r1);
        process(r2);
        process(()->System.out.println("Hello"));*/

    }

    public static void process(Runnable r){
          r.run();
    }
}
