package org.hello.chapter04.item20.templatemethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.function.BiFunction;

public class FileProcessor {

    public final int process(BiFunction<Integer, Integer, Integer> operator) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));) {
            int result;
            String line = reader.readLine();
            String[] split = line.split(",");
            result = operator.apply(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FileProcessor fileProcessor = new FileProcessor();
        int result = fileProcessor.process(Integer::sum);
        System.out.println("result: " + result);
    }
}
