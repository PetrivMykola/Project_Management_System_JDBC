package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Project;
import net.petriv.model.Team;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectDaoImpl implements GeneralDao<Project> {

    private final String SAVE_PROJECT = "INSERT INTO projects (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_PROJECT_BY_ID = "SELECT * FROM projects WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM projects;";
    private final String DELETE_ENTITY = "DELETE FROM project WHERE id = ?;";
    private final String JOIN = " SELECT teams.id, teams.name FROM projects\n" +
            "     JOIN projects_teams  ON\n" +
            "     ( ? = projects_teams.project_id)\n" +
            "     JOIN teams ON (teams.id = projects_teams.team_id)\n" +
            "     GROUP BY name;";
    private final String UPDATE = "UPDATE project SET name = ?  WHERE project.id = ?;";
    private final String RELETION = "INSERT INTO projects_teams (project_id, team_id) \n" +
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

    public void save(Project project) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SAVE_PROJECT);
            setProjectField(project, ps);
            saveRelations(project, connection);

        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public Project getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Project project = null;

        try {
            ps = conn.prepareStatement(GET_PROJECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                project = getProjectFromResultSet(rs);
                project.setTeams(selectTeamsByProjectId(id, conn));
            }
            return project;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Project> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Project> list = new ArrayList<Project>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Project project = getProjectFromResultSet(rs);
                project.setTeams(selectTeamsByProjectId(project.getId(), conn));
                list.add(project);
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

    public void update(Project project) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setProjectField(project, ps);
            selectTeamsByProjectId(project.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Team> selectTeamsByProjectId(int projectId, Connection conn) {
        List<Team> list = new ArrayList<Team>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN);
            ps.setInt(1, projectId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Team team = new Team();
                team.setId(rs.getInt("id"));
                team.setName(rs.getString("name"));
                list.add(team);
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

    private Project setProjectField(Project project, PreparedStatement ps) {
        try {
            ps.setInt(1, project.getId());
            ps.setString(2, project.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return project;
    }

    private void saveRelations(Project project, Connection conn) {
        Iterator<Team> iterator = project.getTeams().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, project.getId());
                ps.setInt(2, iterator.next().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Project getProjectFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Project(id, name);
    }
}

