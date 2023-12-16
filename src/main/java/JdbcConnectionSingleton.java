import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnectionSingleton {
    private static final String URL = "jdbc:oracle:thin:@//10.154.6.32:1521/pdb_dev";
    private static final String USERNAME = "digital_dev";
    private static final String PASSWORD = "digital_dev";

    private JdbcConnectionSingleton() {
    }

    private static class SingletonHolder {
        private static final Connection INSTANCE;

        static {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                INSTANCE = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Connection getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
