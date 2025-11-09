import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static String url;
    private static String user;
    private static String pass;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties p = new Properties();
            try (FileInputStream fis = new FileInputStream("db.properties")) {
                p.load(fis);
            }
            url = p.getProperty("url");
            user = p.getProperty("user");
            pass = p.getProperty("pass");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Gagal inisialisasi DB: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
