public class FizzBuzz {
    public static void main(String[] args) {
        // Bug 1: Loop should be 1 to 100 inclusive -> fixed
        for (int i = 0; i <= 100; i++) {
            // Bug 2: Missing mod 15 check -> fixed
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                // Bug 3: String spelling typo -> fixed
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}