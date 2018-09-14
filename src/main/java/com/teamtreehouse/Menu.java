package com.teamtreehouse;

import com.teamtreehouse.exceptions.MaximumTeamsException;
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
                response = promptForInput("%nPlease Choose an option: ");
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
                createTeam();
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

    public void createTeam() {
        String teamName = promptForInput("What is the team name? ");
        String coachName = promptForInput("What is the coach name? ");


        try {
            league.createTeam(teamName, coachName);
            System.out.printf("Team %s coached by %s created.%n", teamName, coachName);
        } catch (Exception e) {
            System.out.printf("%s", e.getMessage());
        }
    }

    private String promptForInput(String message) {
        String response = "";
        while (response.isEmpty()) {
            try {
                System.out.printf(message);
                response = reader.readLine();
            } catch (IOException e) {
                System.out.printf("It looks like there was an issue handling your input. The message was: %s",
                        e.getMessage());
                System.out.print("Please try again. ");
            }
        }
        return response;
    }

    private int promptForInputExpectingInteger(String message, int maximumValue) {
        int response = 0;
        while (response <= 0 || response > maximumValue) {
            try {
                response = Integer.parseInt(promptForInput(message));
                if (response <= 0) {
                    throw new NumberFormatException();
                } else if (response > maximumValue) {
                    throw new IndexOutOfBoundsException();
                }
            } catch (NumberFormatException e) {
                System.out.printf("The value you provided was not a non-zero number. Please try again. %n");
            } catch (IndexOutOfBoundsException e) {
                System.out.printf("The value you provided was greater than the maximum value: %d", maximumValue);
            }
        }
        return response;
    }
}
