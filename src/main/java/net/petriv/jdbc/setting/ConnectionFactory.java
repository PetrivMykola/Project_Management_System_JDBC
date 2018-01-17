package net.petriv.jdbc.setting;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    public Connection newConnection() throws SQLException;

    public void close() throws SQLException;
}
