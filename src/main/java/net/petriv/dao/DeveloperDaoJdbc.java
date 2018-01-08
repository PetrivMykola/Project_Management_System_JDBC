package net.petriv.dao;

import net.petriv.jdbc.JdbcUtils;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.*;
import java.util.*;

public class DeveloperDaoJdbc implements GeneralDAO<Developer> {
    static final String DATABASE_URL = "jdbc:mysql://localhost/project_management_system";
    static final String USER = "root";
    static final String PASSWORD = "123456789";

    public DeveloperDaoJdbc() { }

    static {
        JdbcUtils.initDriver("com.mysql.jdbc.Driver");
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
    }

    public void save(Developer dev) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)\n" +
                    "VALUES (?, ?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, dev.getId());
            preparedStatement.setString(2, dev.getFirstName());
            preparedStatement.setString(3, dev.getLastName());
            preparedStatement.setString(4, dev.getSpecialty());
            preparedStatement.setInt(5, dev.getExperience());
            preparedStatement.setInt(6, dev.getSalary());
            preparedStatement.executeUpdate();
            connection.commit();
            skillsForDev(dev.getSkills(), dev, connection);
            connection.commit();
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(connection);
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public Developer getById(int id) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Developer dev = null;
        try {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            connection.setAutoCommit(false);
            preparedStatement = connection.prepareStatement("SELECT * FROM developers WHERE ID = ? ");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                dev = extractDeveloperFromResultSet(resultSet);
            }


           dev = extractSkillsDeveloper(connection, id, dev);
            connection.commit();
            System.out.println(dev);

            return dev;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(connection);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public List<Developer> getAll() throws SQLException {
        List<Developer> developerList = new ArrayList<Developer>();
        Connection conn = getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Developer developer = null;
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM developers");
            while (rs.next()) {
                developer = extractDeveloperFromResultSet(rs);
                developer = extractSkillsDeveloper(conn,developer.getId(), developer);
               developerList.add(developer);
            }
            conn.commit();
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(stmt);
            JdbcUtils.closeQuietly(conn);
        }
        return developerList;
    }

    public void delete(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        conn.setAutoCommit(false);
        try {
            ps = conn.prepareStatement("DELETE FROM developers WHERE id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }

    }

    public void update(Developer dev) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        conn.setAutoCommit(false);
        try {
            ps = conn.prepareStatement("Update developers SET first_name = ?, last_name = ?," +
                    "specialty = ?, experience = ? , salary = ? WHERE developers.id = ?");
            ps.setString(1, dev.getFirstName());
            ps.setString(2, dev.getLastName());
            ps.setString(3, dev.getSpecialty());
            ps.setInt(4, dev.getExperience());
            ps.setInt(5, dev.getSalary());
            ps.setInt(6, dev.getId());
            int rowUpdated = ps.executeUpdate();
            conn.commit();
            System.out.println(rowUpdated);
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    private Developer extractDeveloperFromResultSet(ResultSet rs) throws SQLException {
        Developer developer = new Developer();
        developer.setId(rs.getInt("id"));
        developer.setFirstName(rs.getString("first_name"));
        developer.setLastName(rs.getString("last_name"));
        developer.setSpecialty(rs.getString("specialty"));
        developer.setExperience(rs.getInt("experience"));
        developer.setSalary(rs.getInt("salary"));
        return developer;
    }

    private Developer extractSkillsDeveloper(Connection conn, int id, Developer dev) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        preparedStatement = conn.prepareStatement("select skills.id, name from developers join developer_skills on ( ? = developer_skills.developer_id) join skills on (skills.id = developer_skills.skill_id) GROUP BY name;");
        preparedStatement.setInt(1, id);
        resultSet = preparedStatement.executeQuery();
        Set<Skill> set = new HashSet<Skill>();
        try {
            while (resultSet.next()) {
                Skill skill = new Skill();
                skill.setId(resultSet.getInt("id"));
                skill.setName(resultSet.getString("name"));
                set.add(skill);
            }
        }catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
        }
         dev.setSkills(set);
        return dev;
    }

    private void skillsForDev(Set<Skill> idSkills, Developer dev, Connection conn) throws SQLException {
        PreparedStatement preparedStatement = null;
        preparedStatement = conn.prepareStatement("0");

        try {
            Iterator<Skill> iter = idSkills.iterator();
            while (iter.hasNext()) {
                Skill skill = iter.next();
                preparedStatement.setInt(1, dev.getId());
                preparedStatement.setInt(2, skill.getId());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
        }
    }
}


