package net.petriv.dao;

import net.petriv.jdbc.setting.ConnectionC3P0;
import net.petriv.jdbc.setting.JdbcUtils;
import net.petriv.model.Company;
import net.petriv.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomerDaoImpl implements GeneralDao<Customer> {

    private final String SAVE_CUSTOMER = "INSERT INTO customers (id, first_name, last_name, address)\n" +
            "VALUES (?, ?, ?, ?);";
    private final String GET_CUSTOMER_BY_ID = "SELECT * FROM customers WHERE ID = ?;";
    private final String GET_ALL = "SELECT * FROM customers;";
    private final String DELETE_ENTITY = "DELETE FROM customers WHERE id = ?;";
    private final String JOIN_TABLE = "SELECT companies.id, companies.name FROM customers\n" +
            "     JOIN customers_companies  ON\n" +
            "     ( ? = customers_companies.customer_id)\n" +
            "     JOIN companies ON (companies.id = customers_companies.company_id)\n" +
            "     GROUP BY name;";
    private final String UPDATE = "UPDATE customers SET id = ?, first_name = ?,\n" +
            "      last_name = ?, address = ?\n" +
            "       WHERE developers.id = ?;";
    private final String RELETION = "INSERT INTO customers_companies (customer_id, company_id) \n" +
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

    public void save(Customer customer) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(SAVE_CUSTOMER);
            setCustomerField(customer, ps);
            saveRelations(customer, connection);

        } catch (Exception e) {
            JdbcUtils.rollbackQuietly(connection);
            System.out.println(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(connection);
        }
    }

    public Customer getById(int id) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            ps = conn.prepareStatement(GET_CUSTOMER_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                customer = getCustomerFromResultSet(rs);
                customer.setCompanies(selectCompaniesByCustomerId(id, conn));
            }
            return customer;

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw e;
        } finally {
            JdbcUtils.closeQuietly(rs);
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }
    }

    public List<Customer> getAll() throws SQLException {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        List<Customer> list = new ArrayList<Customer>();

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(GET_ALL);

            while (rs.next()) {
                Customer customer = getCustomerFromResultSet(rs);
                customer.setCompanies(selectCompaniesByCustomerId(customer.getId(), conn));
                list.add(customer);
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

    public void update(Customer customer) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            setCustomerField(customer, ps);
            selectCompaniesByCustomerId(customer.getId(), conn);

        } catch (SQLException e) {
            JdbcUtils.rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            JdbcUtils.closeQuietly(ps);
            JdbcUtils.closeQuietly(conn);
        }


    }

    private List<Company> selectCompaniesByCustomerId(int customerId, Connection conn) {
        List<Company> list = new ArrayList<Company>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(JOIN_TABLE);
            ps.setInt(1, customerId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Company company = new Company();
                company.setId(rs.getInt("id"));
                company.setName(rs.getString("name"));
                list.add(company);
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

    private Customer setCustomerField(Customer customer, PreparedStatement ps) {
        try {
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getFirstName());
            ps.setString(3, customer.getLastName());
            ps.setString(4, customer.getAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return customer;
    }

    private void saveRelations(Customer customer, Connection conn) {
        Iterator<Company> iterator = customer.getCompanies().iterator();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(RELETION);
            while (iterator.hasNext()) {
                ps.setInt(1, customer.getId());
                ps.setInt(2, iterator.next().getId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        } finally {
            JdbcUtils.closeQuietly(ps);
        }
    }

    private Customer getCustomerFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String address = rs.getString("address");
        return new Customer(id, firstName, lastName, address);
    }
}

