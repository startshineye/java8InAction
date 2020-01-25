package com.yxm.stream.lesson9_10;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CreateStream {
    public static void main(String[] args){
        // createStreamFromCollection().forEach(System.out::println);
          //createStreamFromIterate().forEach(System.out::println);
        createStreamFromGenerate().forEach(System.out::println);

    }

    public static Stream<String> createStreamFromCollection(){
        List<String> list = Arrays.asList("hello", "alex", "world", "stream");
        return list.stream();
    }

    public static Stream<String> createStreamFromValues(){
        return Stream.of("hello", "alex", "world", "stream");
    }

    public static Stream<String> createStreamFromArrays(){
        String[] string = {"hello", "alex", "world", "stream"};
        return Arrays.stream(string);
    }

    public static Stream<String> createStreamFromFile(){
        Path path = Paths.get("F:\\yexinming\\github\\java8InAction\\java8sharing\\src\\main\\java\\com\\yxm\\stream\\lesson9\\CreateStream.java");
        try (Stream<String> lines= Files.lines(path)){
            return  lines;
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    public static Stream<Integer> createStreamFromIterate(){
        Stream<Integer> iterate = Stream.iterate(0, n -> n + 2).limit(10);
        return iterate;
    }

    public static Stream<Double> createStreamFromGenerate(){
        Stream<Double> stream = Stream.generate(Math::random).limit(10);
        return stream;
    }
}
