package net.petriv.controller;


import net.petriv.dao.CompanyDaoImpl;
import net.petriv.dao.ProjectDaoImpl;
import net.petriv.model.Company;
import net.petriv.model.Project;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyController  {

    Scanner in = new Scanner(System.in);
    Company company;
    CompanyDaoImpl dao;

    public CompanyController() {
        dao = new CompanyDaoImpl();
    }

    public void saveCompany() throws SQLException {
        dao.save(createNewCompany());
        System.out.println("Create and save Company was successful");
        System.out.println("##############################");
    }

    public void showListCompany() throws SQLException {
        System.out.println(dao.getAll());
        System.out.println("##############################");
    }

    public void updateCompany() throws SQLException {
        System.out.println("Enter id Company for update: ");
        int id = in.nextInt();
        System.out.println("Company for udate: " + dao.getById(id));
        Company newCompany = enterFieldsForCompany(dao.getById(id));
        dao.update(newCompany);
        System.out.println("###################################");
    }

    public void deleteCompany()throws SQLException{
        System.out.println("Enter id Company for delete: ");
        int id = in.nextInt();
        dao.delete(id);
        System.out.println("##############################");
    }

    public Company createNewCompany() {
        company = new Company();
        System.out.println("Enter id:");
        company.setId(in.nextInt());
        System.out.println("Enter name:");
        company.setName(in.next());
        System.out.println("Enter id projects for company: ");
        company.setProjects(getProjectsForCompany(in.nextInt()));
        return company;
    }

    public Company enterFieldsForCompany(Company company) {
        System.out.println("Enter id:");
        company.setId(in.nextInt());
        System.out.println("Enter name:");
        company.setName(in.next());
        System.out.println("Enter id project for company: ");
        company.setProjects(getProjectsForCompany(in.nextInt()));
        return company;
    }

    public List<Project> getProjectsForCompany(int id) {
        List<Project> list = new ArrayList<Project>();
        try {
            ProjectDaoImpl projectDao = new ProjectDaoImpl();
            Project project = projectDao.getById(id);
            list.add(project);
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
        }
        return list;
    }
}
