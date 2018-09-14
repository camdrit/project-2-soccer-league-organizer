package com.teamtreehouse;

import com.teamtreehouse.exceptions.MaximumTeamsException;
import com.teamtreehouse.model.League;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Team;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
                addPlayer();
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

    public void addPlayer() {
        ArrayList<String> availablePlayers = getAndPrintAvailablePlayers(league.getFreePlayers());
        int choice = promptForInputExpectingInteger("Please choose an option: ", availablePlayers.size()) - 1;
        String playerChoice = availablePlayers.get(choice);
        String teamChoice = promptForTeamChoice();
        try {
            league.addPlayerToTeam(teamChoice, playerChoice);
            System.out.printf("%s added to Team %s.%n", playerChoice, teamChoice);
        } catch (Exception e) {
            System.out.printf("%s%n", e.getMessage());
        }

    }

    private ArrayList<String> getAndPrintAvailablePlayers(Set<Player> players) {
        ArrayList<String> choices = new ArrayList<>();
        int index = 1;
        for (Player player : players) {
            System.out.printf("%d.) %s%n", index, player.toString());
            choices.add(player.getName());
            index++;
        }
        return choices;
    }

    private ArrayList<String> getAndPrintAvailableTeams(Map<String, Team> teams) {
        ArrayList<String> choices = new ArrayList<>();
        int index = 1;
        for (Map.Entry<String, Team> team : teams.entrySet()) {
            System.out.printf("%d.) %s%n", index, team.getValue().toString());
            choices.add(team.getValue().getName());
            index++;
        }
        return choices;
    }

    private String promptForTeamChoice() {
        ArrayList<String> availableTeams = getAndPrintAvailableTeams(league.getTeams());
        int choice = promptForInputExpectingInteger("Please choose an option: ", availableTeams.size()) - 1;
        return availableTeams.get(choice);
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
                System.out.printf("The value you provided was greater than the maximum value: %d%n", maximumValue);
            }
        }
        return response;
    }
}
