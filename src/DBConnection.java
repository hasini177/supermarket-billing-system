import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/billing_system";

    private static final String USER = "root";

    private static final String PASSWORD =
            "your_actual_mysql_password";

    public static Connection getConnection() throws SQLException {

        DriverManager.registerDriver(
                new com.mysql.cj.jdbc.Driver()
        );

        return DriverManager.getConnection(
                URL,
                USER,
                PASSWORD
        );
    }
}