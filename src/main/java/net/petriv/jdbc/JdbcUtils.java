package net.petriv.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class JdbcUtils {
    private static boolean initialized;

    public JdbcUtils() {
    }

    public static synchronized void initDriver(String driverClass) {
        if (!initialized) {
            try {
                Class.forName(driverClass);
            } catch (ClassNotFoundException e) {
                e.getCause();
                throw new RuntimeException("Can't initialize driver" + driverClass);
            }
        }
        initialized = true;
    }

    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e ) {
                //NOP
            }
        }
    }

    public static void closeQuietly(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e ) {
                //NOP
            }
        }
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e ) {
                //NOP
            }
        }
    }

    public static void rollbackQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                //NOP
            }
        }
    }
}
