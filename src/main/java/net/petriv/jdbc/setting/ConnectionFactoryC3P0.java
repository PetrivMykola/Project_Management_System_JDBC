package net.petriv.jdbc.setting;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryC3P0 implements ConnectionFactory {

    private ComboPooledDataSource cpds;

    public Connection newConnection() throws SQLException {
        Properties properties = new Properties();
        FileInputStream fis = null;
        cpds = new ComboPooledDataSource();

        try {
            fis = new FileInputStream("src/main/resources/application.properties");
            properties.load(fis);
            cpds.setJdbcUrl(properties.getProperty("DATABASE_URL"));
            cpds.setUser(properties.getProperty("USER"));
            cpds.setPassword(properties.getProperty("PASSWORD"));
            setC3p0(cpds);
        } catch (IOException e) {
            System.out.println("Problem with read file" + e);
        }

        try {
            fis.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return cpds.getConnection();
    }

    public void setC3p0(ComboPooledDataSource cpds) {
        cpds.setMinPoolSize(5);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(20);
        cpds.setMaxStatements(180);

    }


    public void close() throws SQLException {

    }
}
