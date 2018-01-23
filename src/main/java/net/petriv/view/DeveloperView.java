package net.petriv.view;


import net.petriv.controller.DeveloperController;

import java.util.Scanner;

public class DeveloperView {

    Scanner in = new Scanner(System.in);
    DeveloperController devController;
    int choice;

    public DeveloperView() {
        devController = new DeveloperController();

    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                devController.saveDeveloper();
                break;
            case 2:
                devController.showListDevelopers();
                break;
            case 3:
                devController.updateDeveloper();
                break;
            case 4:
                devController.deleteDeveloper();
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
            System.out.println(" 1 - Create New Developer And Save: ");
            System.out.println(" 2 - Show List Developers in File:");
            System.out.println(" 3 - Update Developer:");
            System.out.println(" 4 - Delete Developer:");
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

