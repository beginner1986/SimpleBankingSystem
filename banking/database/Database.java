package banking.database;

import banking.data.Card;
import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        try(Statement statement = connect().createStatement()) {
            statement.executeUpdate("INSERT INTO card(number, pin, balance) " +
                    "VALUES('" + card.getNumber() +
                    "', '" + card.getPin() +
                    "', " + card.getBalance() + ");");
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
}
