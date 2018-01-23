package net.petriv.view;


import net.petriv.controller.ProjectController;

import java.util.Scanner;

public class ProjectView {

    Scanner in = new Scanner(System.in);
    ProjectController projectController;
    int choice;

    public ProjectView() {
        projectController = new ProjectController();
    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                projectController.saveProject();
                break;
            case 2:
                projectController.showListProject();
                break;
            case 3:
                projectController.updateProject();
                break;
            case 4:
                projectController.deleteProject();
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
            System.out.println(" 1 - Create New Project And Save: ");
            System.out.println(" 2 - Show List Project in File:");
            System.out.println(" 3 - Update Project:");
            System.out.println(" 4 - Delete Project:");
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
