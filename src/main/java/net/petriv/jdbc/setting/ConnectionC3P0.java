package net.petriv.jdbc.setting;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionC3P0 {

    private static ComboPooledDataSource cpds;
    private static String DATABASE_URL;
    private static String USER;
    private static String PASSWORD;

    static {
        Properties properties = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            DATABASE_URL = properties.getProperty("DATABASE_URL");
            USER = properties.getProperty("USER");
            PASSWORD = properties.getProperty("PASSWORD");
        } catch (IOException e) {
            System.out.println("Problem with read file" + e);
        }
        try {
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection newConnection() throws SQLException {
        cpds = new ComboPooledDataSource();
        cpds.setJdbcUrl(DATABASE_URL);
        cpds.setUser(USER);
        cpds.setPassword(PASSWORD);
        setC3p0(cpds);
        return cpds.getConnection();
    }

    public static void setC3p0(ComboPooledDataSource cpds) {
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }
}
