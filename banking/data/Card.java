package banking.data;

import java.util.Random;

class Card {
    private String number;
    private String pin;
    private double balance;

    public Card() {
        this.number = generateCardNumber();
        this.pin = generatePin();
        this.balance = 0;

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(this.number);
        System.out.println("Your card PIN:");
        System.out.println(this.pin);
        System.out.println();
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        int[] number = new int[16];
        // Bank Identification Number
        number[0] = 4;
        for(int i=1; i<=5; i++)
            number[i] = 0;

        // Account identifier
        for(int i=6; i<=14; i++)
            number[i] = random.nextInt(10);

        //checksum
        number[15] = generateChecksum(number);

        for(int i=0; i<number.length; i++) {
            char digit = Character.forDigit(number[i], 10);
            sb.append(digit);
        }

        return sb.toString();
    }

    private int generateChecksum(int[] input) {
       int[] inputCopy = input.clone();
       for(int i=0; i<inputCopy.length; i+=2) {
           inputCopy[i] *= 2;
           if(inputCopy[i] > 9)
               inputCopy[i] -= 9;
       }

       int sum = 0;
       for(int i=0; i<inputCopy.length; i++)
           sum += inputCopy[i];

       int last = sum % 10;

       if(last == 0)
           return 0;
       else
           return 10 - last;
    }

    private String generatePin() {
        Random random = new Random();
        int pin = random.nextInt(9999);

        return String.format("%04d", pin);
    }

    public boolean checkPin(String input) {
        return this.pin.equals(input);
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

}
