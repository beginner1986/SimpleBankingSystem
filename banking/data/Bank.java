package banking.data;

import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Card> cards;
    private Status status;

    public Bank() {
        cards = new ArrayList<>();
        status = Status.NOT_LOGGED;
    }

    public void input(int input) {
        switch (input) {
            case 1:
                // TODO: create an account
                break;
            case 2:
                // TODO: login
                break;
            default:
                break;
        }
    }
}
