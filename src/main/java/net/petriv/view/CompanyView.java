package net.petriv.view;

import net.petriv.controller.CompanyController;

import java.util.Scanner;

public class CompanyView {

    Scanner in = new Scanner(System.in);
    CompanyController companyController;
    int choice;

    public CompanyView() {
        companyController = new CompanyController();
    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                companyController.saveCompany();
                break;
            case 2:
                companyController.showListCompany();
                break;
            case 3:
                companyController.updateCompany();
                break;
            case 4:
                companyController.deleteCompany();
                break;
            case 5:
                MainView mainView = new MainView();
                mainView.mainMenu();
                break;
        }
        menu();
    }

    public void menu() {
        try {
            System.out.println("*************************************");
            System.out.println(" 1 - Create New Company And Save: ");
            System.out.println(" 2 - Show List Companies in File: ");
            System.out.println(" 3 - Update Company: ");
            System.out.println(" 4 - Delete Company: ");
            System.out.println(" 5 - Main menu: ");
            System.out.println("***Please Enter Number Of Your Choise:***");
            choice = in.nextInt();
            action(choice);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            menu();
        }
    }
}
