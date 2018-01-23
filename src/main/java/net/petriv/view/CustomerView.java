package net.petriv.view;

import net.petriv.controller.CustomerController;

import java.util.Scanner;

public class CustomerView {

    Scanner in = new Scanner(System.in);
    CustomerController customerController;
    int choice;

    public CustomerView() {
        customerController = new CustomerController();
    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                customerController.saveCustomer();
                break;
            case 2:
                customerController.showListCustomer();
                break;
            case 3:
                customerController.updateCustomer();
                break;
            case 4:
                customerController.deleteCustomer();
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
            System.out.println(" 1 - Create New Costomer And Save: ");
            System.out.println(" 2 - Show List Customers in File: ");
            System.out.println(" 3 - Update Customer: ");
            System.out.println(" 4 - Delete Customer: ");
            System.out.println(" 5 - Main menu:");
            System.out.println("***Please Enter Number Of Your Choise:***");
            choice = in.nextInt();
            action(choice);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            menu();
        }
    }
}
