package hello.tdd;

public class FizzBuzz {
    public static String compute(int i) {
        if (i % 3 == 0) {
            return "Fizz";
        } else if (i % 5 == 0) {
            return "Buzz";
        }
        return null;
    }
}
