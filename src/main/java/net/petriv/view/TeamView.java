package net.petriv.view;

import net.petriv.controller.TeamController;

import java.util.Scanner;

public class TeamView {

    Scanner in = new Scanner(System.in);
    TeamController teamController;
    int choice;

    public TeamView() {
        teamController = new TeamController();
    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                teamController.saveTeam();
                break;
            case 2:
                teamController.showListTeam();
                break;
            case 3:
                teamController.updateTeam();
                break;
            case 4:
                teamController.deleteTeam();
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
            System.out.println(" 1 - Create New Team And Save: ");
            System.out.println(" 2 - Show List Teams in File:");
            System.out.println(" 3 - Update Team:");
            System.out.println(" 4 - Delete Team:");
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
