package com.practice.kafka.producer;

import com.github.javafaker.Faker;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FileUtilAppend {
    // 피자 메뉴를 설정. getRandomValueFromList()에서 임의의 피자명을 출력하는 데 사용.
    private static final List<String> pizzaNames = List.of("Potato Pizza", "Cheese Pizza",
            "Cheese Garlic Pizza", "Super Supreme", "Peperoni");
//    private static final List<String> pizzaNames = List.of("고구마 피자", "치즈 피자",
//            "치즈 갈릭 피자", "슈퍼 슈프림", "페페로니 피자");

    // 피자 가게명을 설정. getRandomValueFromList()에서 임의의 피자 가게명을 출력하는데 사용.
    private static final List<String> pizzaShop = List.of("A001", "B001", "C001",
            "D001", "E001", "F001", "G001", "H001", "I001", "J001", "K001", "L001", "M001", "N001",
            "O001", "P001", "Q001");

    private static int orderSeq = 5000;
    public FileUtilAppend() {}

    //인자로 피자명 또는 피자가게 List와 Random 객체를 입력 받아서 random한 피자명 또는 피자 가게 명을 반환.
    private String getRandomValueFromList(List<String> list, Random random) {
        int size = list.size();
        int index = random.nextInt(size);

        return list.get(index);
    }

    //random한 피자 메시지를 생성하고, 피자가게 명을 key로 나머지 정보를 value로 하여 Hashmap을 생성하여 반환.
    public HashMap<String, String> produce_msg(Faker faker, Random random, int id) {

        String shopId = getRandomValueFromList(pizzaShop, random);
        String pizzaName = getRandomValueFromList(pizzaNames, random);

        String ordId = "ord"+id;
        String customerName = faker.name().fullName();
        String phoneNumber = faker.phoneNumber().phoneNumber();
        String address = faker.address().streetAddress();
        LocalDateTime now = LocalDateTime.now();
        String message = String.format("%s, %s, %s, %s, %s, %s, %s"
                , ordId, shopId, pizzaName, customerName, phoneNumber, address
                , now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.KOREAN)));
        //System.out.println(message);
        HashMap<String, String> messageMap = new HashMap<>();
        messageMap.put("key", shopId);
        messageMap.put("message", message);

        return messageMap;
    }

    public void writeMessage(String filePath, Faker faker, Random random) {
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            for(int i=0; i < 50; i++) {
                HashMap<String, String> message = produce_msg(faker, random, orderSeq++);
                printWriter.println(message.get("key")+"," + message.get("message"));
            }
            printWriter.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FileUtilAppend fileUtilAppend = new FileUtilAppend();
        // seed값을 고정하여 Random 객체와 Faker 객체를 생성.
        long seed = 2022;
        Random random = new Random(seed);
        Faker faker = Faker.instance(random);
        //여러분의 절대경로 위치로 변경해 주세요.
        String filePath = "/Users/chanpark/MyCode/TIL/Kafka/KafkaProj-01/practice/src/main/resources/pizza_append.txt";
        // 100회 반복 수행.
        for(int i=0; i<1000; i++) {
            //50 라인의 주문 문자열을 출력
            fileUtilAppend.writeMessage(filePath, faker, random);
            System.out.println("###### iteration:"+i+" file write is done");
            try {
                //주어진 기간동안 sleep
                Thread.sleep(20000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
