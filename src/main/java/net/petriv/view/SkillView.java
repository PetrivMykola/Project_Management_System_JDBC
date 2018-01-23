package net.petriv.view;


import net.petriv.controller.SkillController;

import java.util.Scanner;

public class SkillView {

    Scanner in = new Scanner(System.in);
    SkillController skillController;
    int choice;

    public SkillView() {
        skillController = new SkillController();
    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                skillController.saveSkill();
                break;
            case 2:
                skillController.showListSkill();
                break;
            case 3:
                skillController.updateSkill();
                break;
            case 4:
                skillController.deleteSkill();
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
            System.out.println(" 1 - Create New Skill And Save: ");
            System.out.println(" 2 - Show List Skill in File:");
            System.out.println(" 3 - Update Skill:");
            System.out.println(" 4 - Delete Skill:");
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
