package org.hello.item01.flyweight;

import java.util.HashMap;
import java.util.Map;

public class Dog {
    private final String name;
    private final int age;
    private final String gender;

    private final String breed;
    private static final String DNA = "아무튼 엄청 큰 데이터";

    private final Map<String, DogBreedDNA> dogBreedDNAMap = new HashMap<>();
    public Dog(String name, int age, String gender, String breed) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.breed = breed;
    }

     public void addDNA(String breed, String dna) {
         dogBreedDNAMap.put(breed, new DogBreedDNA(breed, dna));
     }


    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", breed='" + breed + '\'' +
                ", dogBreedDNAMap=" + dogBreedDNAMap +
                '}';
    }
}
