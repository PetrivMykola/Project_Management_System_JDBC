package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Company;
import net.petriv.model.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompanyDaoImpl implements GeneralDao<Company> {

    private final String SAVE_Company = "INSERT INTO companies (id, name)\n" +
            "VALUES (?, ?);";
    private final String GET_COMPANY_BY_ID = "SELECT * FROM companies WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM companies;";
    private final String DELETE_ENTITY = "DELETE FROM companies WHERE id = ?;";
    private final String JOIN = "  SELECT projects.id, projects.name FROM companies\n" +
            "     JOIN companies_projects  ON\n" +
            "     ( ? = companies_projects.company_id)\n" +
            "     JOIN projects ON (projects.id = companies_projects.project_id)\n" +
            "     GROUP BY name;";
    private final String UPDATE = "UPDATE companies SET name = ?  WHERE companies.id = ?;";
    private final String RELETION = "INSERT INTO companies_projects (company_id, project_id) \n" +
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

    public void save(Company company) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SAVE_Company);
            setCompanyFields(company, ps);
            saveRelations(company, connection);

        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public Company getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Company company = null;

        try {
            ps = conn.prepareStatement(GET_COMPANY_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                company = getCompanyFromResultSet(rs);
                company.setProjects(selectProjectByCompanyId(id, conn));
            }
            return company;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Company> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Company> list = new ArrayList<Company>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Company company = getCompanyFromResultSet(rs);
                company.setProjects(selectProjectByCompanyId(company.getId(), conn));
                list.add(company);
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

    public void update(Company company) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setCompanyFields(company, ps);
            selectProjectByCompanyId(company.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Project> selectProjectByCompanyId(int companyId, Connection conn) {
        List<Project> list = new ArrayList<Project>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN);
            ps.setInt(1, companyId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Project team = new Project();
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

    private Company setCompanyFields(Company company, PreparedStatement ps) {
        try {
            ps.setInt(1, company.getId());
            ps.setString(2, company.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return company;
    }

    private void saveRelations(Company company, Connection conn) {
        Iterator<Project> iterator = company.getProjects().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, company.getId());
                ps.setInt(2, iterator.next().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Company getCompanyFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        return new Company(id, name);
    }
}
