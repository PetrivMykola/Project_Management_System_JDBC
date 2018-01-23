package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.*;
import java.util.*;

public class DeveloperDaoImpl implements GeneralDao<Developer> {

    private final String SAVE_DEVELOPER = "INSERT INTO developers (id, first_name, last_name, specialty, experience, salary)\n" +
            "VALUES (?, ?, ?, ?, ?, ?);";
    private final String GET_DEVELOPER_BY_ID = "SELECT * FROM developers WHERE id = ?;";
    private final String GET_ALL = "SELECT * FROM developers;";
    private final String DELETE_ENTITY = "DELETE FROM developers WHERE id = ?;";
    private final String JOIN_TABLE = "SELECT skills.id, name FROM developers JOIN developers_skills ON " +
            "( ? = developers_skills.developer_id) JOIN skills ON " +
            "  (skills.id = developers_skills.skill_id) GROUP BY name;";
    private final String UPDATE = "UPDATE developers SET id = ?, first_name = ?, \n" +
            "     last_name = ?, specialty = ?, experience = ? , salary = ? \n" +
            "     WHERE developers.id = ?;";
    private final String RELETION = "INSERT INTO developers_skills (developer_id, skill_id) \n" +
            "VALUES (?, ?);";

    private static Connection getConnection() {
        Connection connection = null;
        try {
            connection = ConnectionC3P0.newConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection == null) {
            throw new IllegalStateException("problem with connection");
        }
        return connection;
    }

    public void save(Developer developer) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(SAVE_DEVELOPER);
            setDeveloperField(developer, ps);
            saveRelations(developer, conn);

        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(conn);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public Developer getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Developer developer = null;

        try {
            ps = conn.prepareStatement(GET_DEVELOPER_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                developer = getDeveloperFromResultSet(rs);
                developer.setSkills(selelectSkillsByDeveloperId(id, conn));
            }
            return developer;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Developer> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Developer> list = new ArrayList<Developer>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Developer developer = getDeveloperFromResultSet(rs);
                developer.setSkills(selelectSkillsByDeveloperId(developer.getId(), conn));
                list.add(developer);
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

    public void delete(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(DELETE_ENTITY);
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

    public void update(Developer developer) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setDeveloperField(developer, ps);
            selelectSkillsByDeveloperId(developer.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Skill> selelectSkillsByDeveloperId(int developerId, Connection conn) {
        List<Skill> list = new ArrayList<Skill>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN_TABLE);
            ps.setInt(1, developerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Skill skill = new Skill();
                skill.setId(rs.getInt("id"));
                skill.setName(rs.getString("name"));
                list.add(skill);
            }
        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
        }
        return list;
    }

    private Developer setDeveloperField(Developer dev, PreparedStatement ps) {
        try {
            ps.setInt(1, dev.getId());
            ps.setString(2, dev.getFirstName());
            ps.setString(3, dev.getLastName());
            ps.setString(4, dev.getSpecialty());
            ps.setInt(5, dev.getExperience());
            ps.setInt(6, dev.getSalary());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return dev;
    }

    private void saveRelations(Developer dev, Connection conn) {
        Iterator<Skill> iterator = dev.getSkills().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, dev.getId());
                ps.setInt(2, iterator.next().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Developer getDeveloperFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String specialty = rs.getString("specialty");
        int experience = rs.getInt("experience");
        int salary = rs.getInt("salary");
        return new Developer(id, firstName, lastName, specialty, experience, salary);
    }

}












