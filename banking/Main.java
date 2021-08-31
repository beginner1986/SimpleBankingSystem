package banking;

import banking.data.Bank;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        while(running) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            int input = scanner.nextInt();

            if(input == 0) {
                running = false;
            } else {
                System.out.println();
                bank.input(input);
            }
        }
    }
}