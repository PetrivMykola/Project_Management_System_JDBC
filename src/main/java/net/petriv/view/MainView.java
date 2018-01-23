package net.petriv.view;

import java.util.Scanner;

public class MainView {
    DeveloperView developerView;
    SkillView skillView;
    TeamView teamView;
    ProjectView projectView;
    CompanyView companyView;
    CustomerView customerView;

    public MainView() {
        developerView = new DeveloperView();
        skillView = new SkillView();
        teamView = new TeamView();
        projectView = new ProjectView();
        companyView = new CompanyView();
        customerView = new CustomerView();
    }

    Scanner in = new Scanner(System.in);
    int mainChoise;

    public void mainMenu() {
        System.out.println("****************************************");
        System.out.println("1. Work with 'Developer' ");
        System.out.println("2. Work with 'Skill' ");
        System.out.println("3. Work with 'Team' ");
        System.out.println("4. Work with 'Project' ");
        System.out.println("5. Work with 'Company' ");
        System.out.println("6. Work with 'Customer' ");
        System.out.println("7. 'Exit' ");
        System.out.println("Enter number of your choise: ");
        mainChoise = in.nextInt();
        action(mainChoise);

    }

    public void action(int choise) {
        switch (choise) {
            case 1:
                developerView.menu();
                break;
            case 2:
                skillView.menu();
                break;
            case 3:
                teamView.menu();
                break;
            case 4:
                projectView.menu();
                break;
            case 5:
                companyView.menu();
                break;
            case 6:
                customerView.menu();
                break;
            case 7:
                System.exit(0);
                break;
        }
    }

}

