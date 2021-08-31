package banking.data;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Card> cards;
    private Status status;
    private LoginStage loginStage;
    private int currentCardIndex;

    public Bank() {
        this.cards = new ArrayList<>();
        this.status = Status.MENU;
        this.loginStage = LoginStage.CARD_NUMBER;
    }

    public void input(String input) {
        switch(status) {
            case MENU:
                handleMenu(input);
                break;
            case LOGIN:
                login(input);
                break;
            case AUTHORIZED:
                operations(input);
                break;
            default:
                break;
        }
    }

    private void handleMenu(String input) {
        switch (input) {
            case "1":
                createAccount();
                break;
            case "2":
                this.status = Status.LOGIN;
                System.out.println("Enter your card number:");
                break;
            default:
                break;
        }

        if(status == Status.MENU) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
        }
    }

    private void createAccount() {
        Card card = new Card();
        cards.add(card);

        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.getNumber());
        System.out.println("Your card PIN:");
        System.out.println(card.getPin());
    }

    private void login(String input) {
        switch(loginStage) {
            case CARD_NUMBER:
                currentCardIndex = checkNumber(input);
                loginStage = LoginStage.PIN;
                break;
            case PIN:
                checkPin(input);
                loginStage = LoginStage.CARD_NUMBER;

                if(currentCardIndex == -1 || status != Status.AUTHORIZED) {
                    System.out.println("Wrong card number or PIN!");
                    status = Status.MENU;
                } else {
                    System.out.println("You have successfully logged in!");
                    System.out.println();
                    System.out.println("1. Balance");
                    System.out.println("2. Log out");
                    System.out.println("0. Exit");
                }
                break;
            default:
                break;
        }
    }

    private int checkNumber(String input) {
        for(int i=0; i<this.cards.size(); i++) {
            String number = cards.get(i).getNumber();
            if(number.equals(input)) {
                return i;
            }
        }

        return -1;
    }

    private void checkPin(String input) {
        if(currentCardIndex == -1) {
            status = Status.MENU;
        } else {
            String pin = cards.get(currentCardIndex).getPin();
            if(pin.equals(input)) {
                status = Status.AUTHORIZED;
            } else {
                status = Status.MENU;
            }
        }
    }

    private void operations(String input) {
        switch(input) {
            case "1":
                System.out.printf("Balance: %f\n", cards.get(currentCardIndex).getBalance());
                break;
            case "2":
                status = Status.MENU;
                System.out.println("You have successfully logged out!\n");
                System.out.println("1. Create an account");
                System.out.println("2. Log into account");
                System.out.println("0. Exit");
                break;
            default:
                break;
        }
    }
}
