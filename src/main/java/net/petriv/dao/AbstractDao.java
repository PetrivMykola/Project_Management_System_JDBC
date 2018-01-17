package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionFactoryFactory;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.jdbc.enricher.Enricher;
import net.petriv.jdbc.extractor.Extractor;
import net.petriv.jdbc.setter.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> {

    protected static Connection getConnection() {
        Connection connection = new ConnectionFactoryFactory().newConnectionFactory();
        if (connection == null) {
            throw new IllegalStateException ("problem with connection");
        }
        return connection;
    }

    protected void save(T obj, String sql, Setter<T> setter) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(sql);
            setter.setOne(obj, preparedStatement);
        }catch (SQLException e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);

        }
    }

    protected List<T> getAll(String sql, Extractor<T> extractor, Enricher<T> enricher ) throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<T>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            while (rs.next()) {
                T record = extractor.extractOne(rs);
                list.add(record);
                enricher.enrich(record);
            }
        } catch (SQLException e) {
            e.getErrorCode();
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(statement);
            JdbcUtils.closeQuietly(conn);
        }
        return list;
    }

    public T getById(int id, String sql, Extractor<T> extractor, Enricher<T> enricher) throws SQLException {
       Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        T obj = null;

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
               obj =  extractor.extractOne(rs);
               enricher.enrich(obj);
            }
            return obj;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    protected void delete(int id, String ref, String sql) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(ref);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }

    }

    public void update(T obj, String sql, Setter<T> setter) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            setter.setOne(obj, ps);
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }


    public List<T> selectEntityById(int id, String sql, Setter<T> setter, Extractor<T> extractor) {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<T> list = new ArrayList<T>();

       try {
        preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T obj =extractor.extractOne(resultSet);
               list.add(setter.setOne(obj, preparedStatement));
            }
        }catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(conn);
        }
        return list;
    }

    public void relationship(int id, List<Integer> arrId, String sql) {
        Connection conn = getConnection();
        PreparedStatement preparedStatement = null;
        for (int i=0; i<arrId.size(); i++) {
            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, arrId.get(i));
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.getErrorCode();
            }
        }
    }
}


