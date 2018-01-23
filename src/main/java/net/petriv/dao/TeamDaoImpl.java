package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Developer;
import net.petriv.model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TeamDaoImpl implements GeneralDao<Team> {


    private final String SAVE_TEAM = "INSERT INTO teams (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_TEAM_BY_ID = "SELECT * FROM teams WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM teams;";
    private final String DELETE_ENTITY = "DELETE FROM teams WHERE id = ?;";
    private final String JOIN = "\n" +
            "      SELECT developers.id, developers.first_name,\n" +
            "     developers.last_name, developers.specialty,\n" +
            "     developers.experience, developers.salary FROM teams\n" +
            "     JOIN teams_developers ON\n" +
            "     ( ? = teams_developers.team_id)\n" +
            "     JOIN developers on (developers.id = teams_developers.developer_id)\n" +
            "     GROUP BY first_name;";
    private final String UPDATE = "UPDATE teams SET name = ?  WHERE teams.id = ?;";
    private final String RELETION = "INSERT INTO teams_developers (team_id, developer_id) \n" +
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

    public void save(Team team) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SAVE_TEAM);
            setTeamFields(team, ps);
            saveRelations(team, connection);

        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public Team getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Team team = null;

        try {
            ps = conn.prepareStatement(GET_TEAM_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                team = getTeamFromResultSet(rs);
                team.setDevelopers(selectDevelopersByTeamId(id, conn));
            }
            return team;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Team> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Team> list = new ArrayList<Team>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Team team = getTeamFromResultSet(rs);
                team.setDevelopers(selectDevelopersByTeamId(team.getId(), conn));
                list.add(team);
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

    public void update(Team team) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setTeamFields(team, ps);
            selectDevelopersByTeamId(team.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Developer> selectDevelopersByTeamId(int teamId, Connection conn) {
        List<Developer> list = new ArrayList<Developer>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN);
            ps.setInt(1, teamId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Developer developer = new Developer();
                developer.setId(rs.getInt("id"));
                developer.setFirstName(rs.getString("first_name"));
                developer.setLastName(rs.getString("last_name"));
                developer.setSpecialty(rs.getString("specialty"));
                developer.setExperience(rs.getInt("experiance"));
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

    private Team setTeamFields(Team team, PreparedStatement ps) {
        try {
            ps.setInt(1, team.getId());
            ps.setString(2, team.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return team;
    }

    private void saveRelations(Team team, Connection conn) {
        Iterator<Developer> iterator = team.getDevelopers().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, team.getId());
                ps.setInt(2, iterator.next().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Team getTeamFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Team(id, name);
    }
}
