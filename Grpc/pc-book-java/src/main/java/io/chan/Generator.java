package io.chan;

import com.google.protobuf.Timestamp;

import java.time.Instant;
import java.util.Random;

public class Generator {
    private Random rand;
    public Generator() {
        rand = new Random();
    }

    public static void main(String[] args){
        Generator generator = new Generator();
        Laptop laptop = generator.NewLaptop();
        System.out.println(laptop);
    }
    public Keyboard NewKeyboard() {
        return Keyboard.newBuilder()
                .setLayout(radomKeyboardLayout())
                .setBacklit(rand.nextBoolean())
                .build();
    }

    public CPU NewCPU() {
        String brand = randomCPUBrand();
        String name = randomCPUName(brand);
        int numberCores = randomInt(2, 8);
        int numberThreads = randomInt(numberCores, 12);

        double minGhz = randomDouble(2.0, 3.5);
        double maxGhz = randomDouble(minGhz, 5.0);

        return CPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setNumberCores(numberCores)
                .setNumberThreads(numberThreads)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .build();
    }

    public GPU NewGPU() {
        String brand = randomGPUBrand();
        String name = randomGPUName(brand);
        double minGhz = randomDouble(1.0, 1.5);
        double maxGhz = randomDouble(minGhz, 2.0);

        Memory memory = Memory.newBuilder()
                .setValue(randomInt(4, 16))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return GPU.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setMemory(memory)
                .setMinGhz(minGhz)
                .setMaxGhz(maxGhz)
                .build();
    }

    public Memory NewMemory() {
        return Memory.newBuilder()
                .setValue(randomInt(4, 16))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();
    }

    public Storage NewStorage() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setMemory(memory)
                .setDriver(Storage.Driver.SSD)
                .build();
    }

    public Storage NewHDD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setMemory(memory)
                .setDriver(Storage.Driver.HDD)
                .build();
    }

    public Storage NewSSD() {
        Memory memory = Memory.newBuilder()
                .setValue(randomInt(128, 1024))
                .setUnit(Memory.Unit.GIGABYTE)
                .build();

        return Storage.newBuilder()
                .setMemory(memory)
                .setDriver(Storage.Driver.SSD)
                .build();
    }

    public Screen NewScreen() {
        int height = randomInt(1024, 2160); // 720p, 1080p, 1440p, 4k (2160p
        int width = height * 16 / 9; // 16:9 aspect ratio
        return Screen.newBuilder()
                .setSizeInch(randomFloat(13.0, 17.0))
                .setPanel(randomScreenPanel())
                .setMultitouch(rand.nextBoolean())
                .build();
    }

    public Laptop NewLaptop() {
        String brand = randomLaptopBrand();
        String name = randomLaptopName(brand);

        double weightKg = randomDouble(1.0, 3.0);
        double priceUsd = randomDouble(1500.0, 3500.0);

        int releaseYear = randomInt(2015, 2021);
        return Laptop.newBuilder()
                .setBrand(brand)
                .setName(name)
                .setKeyboard(NewKeyboard())
                .setCpu(NewCPU())
                .addGpu(NewGPU())
                .setMemory(NewMemory())
                .addStorage(NewSSD())
                .addStorage(NewHDD())
                .setWeightKg(weightKg)
                .setPriceUsd(priceUsd)
                .setReleaseYear(releaseYear)
                .setScreen(NewScreen())
                .setUpdatedAt(timestampNow())
                .build();
    }

    private Timestamp timestampNow() {
        Instant now = Instant.now();
        return Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();
    }

    private String randomLaptopName(final String brand) {
        return switch (brand) {
            case "Apple" -> new String[]{"MacBook Air", "MacBook Pro"}[rand.nextInt(2)];
            case "Dell" -> new String[]{"XPS 13", "XPS 15", "Alienware"}[rand.nextInt(3)];
            case "HP" -> new String[]{"Spectre x360", "Envy", "Omen"}[rand.nextInt(3)];
            case "Lenovo" -> new String[]{"ThinkPad", "Yoga", "Legion"}[rand.nextInt(3)];
            case "Asus" -> new String[]{"ZenBook", "VivoBook", "ROG"}[rand.nextInt(3)];
            case "Acer" -> new String[]{"Swift", "Aspire", "Predator"}[rand.nextInt(3)];
            default -> new String[]{"GS65", "GE75", "GT76"}[rand.nextInt(3)];
        };
    }

    private String randomLaptopBrand() {
        return new String[] {"Apple", "Dell", "HP", "Lenovo", "Asus", "Acer", "MSI"}[rand.nextInt(7)];
    }

    private Screen.Panel randomScreenPanel() {
        int pick = rand.nextInt(Screen.Panel.values().length);
        return Screen.Panel.values()[pick];
    }

    private float randomFloat(final double min, final double max) {
        return (float) (min + (max - min) * rand.nextDouble());
    }

    private String randomGPUName(final String brand) {
        if (brand.equals("Nvidia")) {
            return new String[] {"GTX 1050", "GTX 1060", "GTX 1070", "GTX 1080"}[rand.nextInt(4)];
        } else {
            return new String[] {"RX 560", "RX 570", "RX 580", "RX 590"}[rand.nextInt(4)];
        }
    }

    private String randomGPUBrand() {
        return new String[] {"Nvidia", "AMD"}[rand.nextInt(2)];
    }

    private double randomDouble(final double min, final double max) {
        return min + (max - min) * rand.nextDouble();
    }

    private int randomInt(final int min, final int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    private String randomCPUName(final String brand) {
        if (brand.equals("Intel")) {
            return new String[] {"i3", "i5", "i7", "i9"}[rand.nextInt(4)];
        } else {
            return new String[] {"Ryzen 3", "Ryzen 5", "Ryzen 7", "Ryzen 9"}[rand.nextInt(4)];
        }
    }

    private String randomCPUBrand() {
        return new String[] {"Intel", "AMD"}[rand.nextInt(2)];
    }

    private Keyboard.Layout radomKeyboardLayout() {
        int pick = rand.nextInt(Keyboard.Layout.values().length);
        return Keyboard.Layout.values()[pick];
    }
}
