package sample;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionClass {

    public Connection connection;
    public Connection getConnection(){
        try {
            String dbName="bankdb";
            String userName="root";
            String password="";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName+"?useUnicode=true&useLegacyDatetimeCode=false&serverTimezone=Turkey",userName,password);
        } catch (Exception e) {
            System.out.println(e);
        }

        return connection;
    }
}
