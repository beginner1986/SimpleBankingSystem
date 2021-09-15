package banking;

import banking.data.Bank;
import banking.database.Database;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String parameter = args[0];
        if(!parameter.equals("-fileName")) {
            System.out.println("Wrong parameter!");
            System.out.println("Correct program parameters: -fileName database.db");
            System.exit(0);
        }

        String databaseUrl = "jdbc:sqlite:" + args[1];
        Bank bank = new Bank(databaseUrl);
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");

        while(running) {
            String input = scanner.nextLine();

            if(input.equals("0")) {
                running = false;
                System.out.println("Bye!");
            } else {
                System.out.println();
                bank.input(input);
            }
        }
    }
}