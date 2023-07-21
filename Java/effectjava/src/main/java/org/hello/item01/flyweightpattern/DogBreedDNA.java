package org.hello.item01.flyweightpattern;

public class DogBreedDNA {
    private final String breed;
    private final String DNA;


    public DogBreedDNA(String breed, String dna) {
        this.breed = breed;
        this.DNA = dna;
    }

    @Override
    public String toString() {
        return "DogBreedDNA{" +
                "breed='" + breed + '\'' +
                ", DNA='" + DNA + '\'' +
                '}';
    }
}
