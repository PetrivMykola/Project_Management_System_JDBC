package net.petriv.jdbc.setting;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class ConnectionFactoryFactory {
    public static enum FactoryType {JDBC, C3P0}

    private static FactoryType currentType = FactoryType.C3P0;

    public static synchronized void setType(FactoryType type) {
        currentType = type;
    }

    public static Connection newConnectionFactory() {

        Connection result = null;

        try {
            switch (currentType) {
                case JDBC:
                    result = new ConnectionFactoryJdbc().newConnection();
                    break;
                case C3P0:
                    result = new ConnectionFactoryC3P0().newConnection();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}