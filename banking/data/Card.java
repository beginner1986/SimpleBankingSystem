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
    }

    private String generateCardNumber() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        sb.append("400000");
        for(int i=0; i<10; i++) {
            char digit = (char) (random.nextInt(10) + '0');
            sb.append(digit);
        }

        return sb.toString();
    }

    private String generatePin() {
        Random random = new Random();
        int pin = random.nextInt(9999);

        return String.format("%04d", pin);
    }

    public String getNumber() {
        return number;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }
}
