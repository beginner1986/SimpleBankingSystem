package banking.database;

import banking.data.Card;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Database {
    private SQLiteDataSource dataSource;

    public Database(String url) {
        this.dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try(Connection connection = connect()) {
            createTable(connection);
        } catch(SQLException e) {
            e. printStackTrace();
        }
    }

    public void insert(Card card) {
        String sql = "INSERT INTO card (number, pin, balance) VALUES(?, ?, ?);";

        try(PreparedStatement statement = connect().prepareStatement(sql)) {
            statement.setString(1, card.getNumber());
            statement.setString(2, card.getPin());
            statement.setInt(3, card.getBalance());

            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public Card getCardByNumber(String number) {
        Card card = null;

        try(Statement statement = connect().createStatement()) {
            try(ResultSet result = statement.executeQuery("SELECT * " +
                    "FROM card " +
                    "WHERE number=" + number)) {
                if(!result.next())
                    return null;

                int id = result.getInt("id");
                String pin = result.getString("pin");
                int balance = result.getInt("balance");

                card = new Card(id, number, pin, balance);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return card;
    }

    private Connection connect() throws SQLException {
        Connection connection = dataSource.getConnection();
        if(!connection.isValid(5)) {
            System.out.println("Connection to database failed!");
            System.exit(0);
        }

        return connection;
    }

    private void createTable(Connection connection) {
        try(Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                    "id INTEGER PRIMARY KEY," +
                    "number TEXT," +
                    "pin TEXT," +
                    "balance INTEGER DEFAULT 0)");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCardByNumber(String number) {
        String sql = "DELETE FROM card WHERE number = " + number;

        try(Statement statement = connect().createStatement()) {
            statement.executeUpdate(sql);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int addIncome(String number) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter income:");
        int income = scanner.nextInt();
        int newBalance = income;

        String getBalanceSql = "SELECT balance FROM card WHERE number = ?";
        String updateBalanceSql = "UPDATE card SET balance = ? WHERE number = ?";

        try(Connection connection = connect();
            PreparedStatement getBalance = connection.prepareStatement(getBalanceSql);
            PreparedStatement updateBalance = connection.prepareStatement(updateBalanceSql
        )) {
            connection.setAutoCommit(false);

            getBalance.setString(1, number);
            ResultSet result = getBalance.executeQuery();

            newBalance = result.getInt("balance") + income;

            updateBalance.setInt(1, newBalance);
            updateBalance.setString(2, number);
            updateBalance.executeUpdate();

            connection.commit();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Income was added!");
        return newBalance;
    }

    public int getBalanceByNumber(String number) {
        String sql = "SELECT balance FROM card WHERE number = ?;";
        int balance = 0;

        try(PreparedStatement statement = connect().prepareStatement(sql)) {
            statement.setString(1, number);
            ResultSet result = statement.executeQuery();
            if(!result.next())
                return -1;

            balance = result.getInt("balance");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public int doTransfer(String sourceAccount) {
        Scanner scanner = new Scanner(System.in);
        int balance = getBalanceByNumber(sourceAccount);

        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String destinationAccount = scanner.nextLine();

        if(!isCardNumberValid(destinationAccount)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return balance;
        }

        Card destinationCard = getCardByNumber(destinationAccount);
        if(getBalanceByNumber(destinationAccount) < 0) {
            System.out.println("Such a card does not exist.");
            return balance;
        }

        if(destinationAccount.equals(sourceAccount)) {
            System.out.println("You can't transfer money to the same account!");
            return balance;
        }

        System.out.println("Enter how much money you want to transfer:");
        int value = scanner.nextInt();

        if(balance < value) {
            System.out.println("Not enough money!");
        } else {
            balance = transferMoneyFromAccountToAnother(sourceAccount, destinationAccount, value);
        }

        return balance;
    }

    private int transferMoneyFromAccountToAnother(String sourceAccount, String destinationAccount, int value) {
        int sourceBalance = getBalanceByNumber(sourceAccount);
        int destinationBalance = getBalanceByNumber(destinationAccount);
        String updateBalanceSql = "UPDATE card " +
                "SET balance = ? " +
                "WHERE number LIKE ?";

        try(Connection connection = connect();
            PreparedStatement takeMoney = connection.prepareStatement(updateBalanceSql);
            PreparedStatement giveMoney = connection.prepareStatement(updateBalanceSql)
        ) {
            connection.setAutoCommit(false);

            sourceBalance -= value;
            takeMoney.setInt(1, sourceBalance);
            takeMoney.setString(2, sourceAccount);
            takeMoney.executeUpdate();

            destinationBalance += value;
            giveMoney.setInt(1, destinationBalance);
            giveMoney.setString(2, destinationAccount);
            giveMoney.executeUpdate();

            connection.commit();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return sourceBalance;
    }

    private boolean isCardNumberValid(String number) {
        int size = number.length();
        int[] digits = new int[size];

        for(int i=0; i<size; i++) {
            digits[i] = Character.getNumericValue(number.charAt(i));
        }

        for(int i=0; i<size; i+=2) {
            digits[i] *= 2;

            if(digits[i] > 9)
                digits[i] -= 9;
        }

        int sum = 0;
        for(int d : digits)
            sum += d;

        return (sum % 10) == 0;
    }
}
