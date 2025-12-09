package backend;

import java.sql.*;
public class DBHelper {
        private static String url = "jdbc:mysql://localhost:3306/peminjaman_lab";
        private static String user = "root";
        private static String pass = "";
public static Connection getConnection() throws SQLException {
    try {
    Class.forName("com.mysql.cj.jdbc.Driver");  // atau "com.mysql.jdbc.Driver" tergantung versi
} catch (ClassNotFoundException e) {
    e.printStackTrace();
}
return DriverManager.getConnection(url, user, pass);
    }
}