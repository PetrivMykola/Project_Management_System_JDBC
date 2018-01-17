package net.petriv.jdbc.setting;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryJdbc implements ConnectionFactory {

    public Connection newConnection() {
        Properties properties = new Properties();
        FileInputStream fis = null;
        Connection con = null;
        try {
            fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);

            con = DriverManager.getConnection(properties.getProperty("DATABASE_URL"),
                                                properties.getProperty("USER"),
                                                    properties.getProperty("PASSWORD"));
        } catch (IOException e) {
            System.out.println("Problem with read file" + e);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        try {
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return con;
    }

    public void close() throws SQLException {
        //NOP
    }
}
