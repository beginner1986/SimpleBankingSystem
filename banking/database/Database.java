package banking.database;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {

    public Database(String url) {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(url);

        try(Connection connection = dataSource.getConnection()) {
            if(!connection.isValid(5)) {
                System.out.printf("Connection to %s failed!\n", url);
                System.exit(0);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
