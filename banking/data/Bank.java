package banking.data;

import banking.database.Database;

public class Bank {
    Database database;
    private Status status;
    private LoginStage loginStage;
    private int currentCardIndex;

    public Bank(String databaseUrl) {
        this.database = new Database(databaseUrl);
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
        database.insert(card);
    }

    private void login(String input) {
        switch(loginStage) {
            case CARD_NUMBER:
                //currentCardIndex = checkNumber(input);
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

    private void checkPin(String input) {
        if(currentCardIndex == -1) {
            status = Status.MENU;
        } else {
            if(cards.get(currentCardIndex).checkPin(input)) {
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
