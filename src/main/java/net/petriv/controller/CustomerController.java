package net.petriv.controller;


import net.petriv.dao.CompanyDaoImpl;
import net.petriv.dao.CustomerDaoImpl;
import net.petriv.model.Company;
import net.petriv.model.Customer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerController {

    Scanner in = new Scanner(System.in);
    Customer customer;
    CustomerDaoImpl dao;

    public CustomerController() {
        dao = new CustomerDaoImpl();
    }

    public void saveCustomer() throws SQLException {
        dao.save(createNewCustomer());
        System.out.println("Create and save Company was successful");
        System.out.println("##############################");
    }

    public void showListCustomer() throws SQLException {
        System.out.println(dao.getAll());
        System.out.println("##############################");
    }

    public void updateCustomer() throws SQLException {
        System.out.println("Enter id Company for update: ");
        int id = in.nextInt();
        System.out.println("Company for udate: " + dao.getById(id));
        Customer newCustomer = enterFieldsForCustomer(dao.getById(id));
        dao.update(newCustomer);
        System.out.println("###################################");
    }

    public void deleteCustomer() throws SQLException {
        System.out.println("Enter id Company for delete: ");
        int id = in.nextInt();
        dao.delete(id);
        System.out.println("##############################");
    }

    public Customer createNewCustomer() {
        customer = new Customer();
        System.out.println("Enter id:");
        customer.setId(in.nextInt());
        System.out.println("Enter name:");
        customer.setFirstName(in.next());
        System.out.println("Enter Last name:");
        customer.setLastName(in.next());
        System.out.println("Enter address:");
        customer.setAddress(in.next());
        System.out.println("Enter id company for customer: ");
        customer.setCompanies(getCompanyForCustomer(in.nextInt()));
        return customer;
    }

    public Customer enterFieldsForCustomer(Customer customer) {
        System.out.println("Enter id:");
        customer.setId(in.nextInt());
        System.out.println("Enter name:");
        customer.setFirstName(in.next());
        System.out.println("Enter Last name:");
        customer.setLastName(in.next());
        System.out.println("Enter address:");
        customer.setAddress(in.next());
        System.out.println("Enter id company for customer: ");
        customer.setCompanies(getCompanyForCustomer(in.nextInt()));
        return customer;
    }

    public List<Company> getCompanyForCustomer(int id) {
        List<Company> list = new ArrayList<Company>();
        try {
            CompanyDaoImpl companyDao = new CompanyDaoImpl();
            Company company = companyDao.getById(id);
            list.add(company);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return list;
    }
}
