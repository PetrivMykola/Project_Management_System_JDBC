package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Developer;
import net.petriv.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkillDaoImpl implements GeneralDao<Skill> {

    private final String SAVE_SKILL = "INSERT INTO skills (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_SKILL_BY_ID = "SELECT * FROM skills WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM skills;";
    private final String DELETE_ENTITY = "DELETE FROM developers WHERE id = ?;";
    private final String JOIN = "SELECT developers.id, developers.first_name,\n" +
            "     developers.last_name, developers.specialty,\n" +
            "     developers.experience, developers.salary FROM skills\n" +
            "     JOIN developers_skills ON\n" +
            "     ( ? = developers_skills.skill_id)\n" +
            "     JOIN developers ON (developers.id = developers_skills.developer_id)\n" +
            "     GROUP BY first_name;";
    private final String UPDATE = "UPDATE skills SET name = ?  WHERE skills.id = ?;";
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

    public void save(Skill skill) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SAVE_SKILL);
            setSkillField(skill, ps);
            saveRelations(skill, connection);
        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(connection);
        }

    }

    public Skill getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Skill skill = null;

        try {
            ps = conn.prepareStatement(GET_SKILL_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                skill = getSkillFromResultSet(rs);
                skill.setDevelopers(selelectDevelopersBySkillId(skill.getId(), conn));
            }
            return skill;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Skill> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Skill> list = new ArrayList<Skill>();
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Skill skill = getSkillFromResultSet(rs);
                skill.setDevelopers(selelectDevelopersBySkillId(skill.getId(), conn));
                list.add(skill);
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

    public void update(Skill skill) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setSkillField(skill, ps);
            selelectDevelopersBySkillId(skill.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Developer> selelectDevelopersBySkillId(int skillId, Connection conn) {
        List<Developer> list = new ArrayList<Developer>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN);
            ps.setInt(1, skillId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Developer developer = new Developer();
                developer.setId(rs.getInt("id"));
                developer.setFirstName(rs.getString("first_name"));
                developer.setLastName(rs.getString("last_name"));
                developer.setSpecialty(rs.getString("specialty"));
                developer.setExperience(rs.getInt("experience"));
                developer.setSalary(rs.getInt("salary"));
                list.add(developer);
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

    private Skill setSkillField(Skill skill, PreparedStatement ps) {
        try {
            ps.setInt(1, skill.getId());
            ps.setString(2, skill.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return skill;
    }

    private void saveRelations(Skill skill, Connection conn) {
        Iterator<Developer> iterator = skill.getDevelopers().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, iterator.next().getId());
                ps.setInt(2, skill.getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Skill getSkillFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Skill(id, name);
    }
}