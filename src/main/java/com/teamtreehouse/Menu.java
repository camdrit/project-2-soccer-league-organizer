package com.teamtreehouse;

import com.teamtreehouse.model.League;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;

public class Menu {
    private Map<String, String> menuOptions;
    private BufferedReader reader;
    private League league;

    public Menu(League league) {
        reader = new BufferedReader(new InputStreamReader(System.in));
        this.league = league;
        menuOptions = new TreeMap<>();
        menuOptions.put("Create", "Create a new team");
        menuOptions.put("Add", "Add a player to a team");
        menuOptions.put("Remove", "Remove a player from a team");
        menuOptions.put("Report", "View a report of a team by height");
        menuOptions.put("Balance", "View the League Balance Report");
        menuOptions.put("Roster", "View roster");
        menuOptions.put("Quit", "Exits the program");
    }

    public void displayMenu() {
        try {
            String response = "";

            while (!response.equals("quit")) {
                System.out.printf("%nMenu%n%n");
                menuOptions.forEach((option, optionText) -> { System.out.printf("%s - %s %n", option, optionText); });
                System.out.printf("%nChoose an option: ");

                response = reader.readLine().trim().toLowerCase();
                parseChoice(response);
            }
            System.out.print("Exiting...");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseChoice(String choice) throws IOException {
        switch (choice) {
            case "create":
                //createTeam();
                break;
            case "add":
                //addPlayer();
                break;
            case "remove":
                //removePlayer();
                break;
            case "report":
                //reportHeight();
                break;
            case "balance":
                //reportLeagueBalance();
                break;
            case "roster":
                //printTeamRoster();
                break;
            case "quit":
                break;
            default:
                System.out.printf("That didn't seem to be a valid choice. Please try again. %n");
                break;
        }
    }
}
