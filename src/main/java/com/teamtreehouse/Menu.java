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
                removePlayer();
                break;
            case "report":
                reportHeight();
                break;
            case "balance":
                reportLeagueBalance();
                break;
            case "roster":
                printTeamRoster();
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
        if (league.getTeams().size() < 1) {
            System.out.print("There are currently no teams in the league! Please create a team first.");
        } else if (league.getFreePlayers().size() < 1) {
            System.out.print("There are currently no free players in the league. No players may be assigned.");
        } else {
            String playerChoice = promptForPlayerChoice(league.getFreePlayers());
            String teamChoice = promptForTeamChoice(false);
            try {
                league.addPlayerToTeam(teamChoice, playerChoice);
                System.out.printf("%s added to Team %s.%n", playerChoice, teamChoice);
            } catch (Exception e) {
                System.out.printf("%s%n", e.getMessage());
            }
        }
    }

    public void removePlayer() {
        if (league.getTeams().size() < 1) {
            System.out.printf("There are currently no teams in the league! Please create a team first.");
        } else if (league.getAssignedPlayerCount() < 1) {
            System.out.printf("There are currently no players assigned to any teams in the league! Please assign a player first.");
        } else {
            String teamChoice = promptForTeamChoice(true);
            String playerChoice = promptForPlayerChoice(league.getTeamPlayers(teamChoice));
            league.removePlayerFromTeam(teamChoice, playerChoice);
            System.out.printf("%s removed from Team %s.%n", playerChoice, teamChoice);
        }

    }

    public void reportHeight() {
        if (league.getTeams().size() < 1) {
            System.out.printf("There are currently no teams in the league! Please create a team first.");
        } else if (league.getAssignedPlayerCount() < 1) {
            System.out.printf("There are currently no players assigned to any teams in the league! Please assign a player first.");
        } else {
            String teamChoice = promptForTeamChoice(true);
            Map<String, Set<Player>> heightReport = league.generateHeightReport(teamChoice);
            System.out.printf("%n==== Height Report for Team %s ====%n", teamChoice);
            heightReport.forEach((s, players) -> {
                System.out.printf("%n%s:%n", s);
                players.forEach(player -> {
                    System.out.printf(" - %s%n", player.toString());
                });
                System.out.printf("Total Players in Class: %d%n", players.size());

            });
            System.out.printf("Experience Rating: %.0f%%%n", league.getTeams().get(teamChoice).calculateExperienceRating());
        }
    }

    public void reportLeagueBalance() {
        if (league.getTeams().size() < 1) {
            System.out.printf("There are currently no teams in the league! Please create a team first.");
        } else if (league.getAssignedPlayerCount() < 1) {
            System.out.printf("There are currently no players assigned to any teams in the league! Please assign a player first.");
        } else {
            System.out.printf("==== League Balance Report ====%n");
            Map<String, Team> teams = league.getTeams();
            teams.forEach((s, team) -> {
                if (team.getPlayers().size() > 1) {
                    System.out.printf("%nTeam %s:%n", s);
                    team.getPlayersByExperience().forEach(player -> {
                        System.out.printf(" - %s%n", player.toString());
                    });
                    System.out.printf("Experience Rating: %.0f%%%n", team.calculateExperienceRating());
                    printAggregateHeightTotals(team.getName());
                }
            });
        }
    }

    public void printTeamRoster() {
        if (league.getTeams().size() < 1) {
            System.out.printf("There are currently no teams in the league! Please create a team first.");
        } else if (league.getAssignedPlayerCount() < 1) {
            System.out.printf("There are currently no players assigned to any teams in the league! Please assign a player first.");
        } else {
            String teamChoice = promptForTeamChoice(true);
            printTeamPlayers(teamChoice);
        }
    }

    private void printAggregateHeightTotals(String teamName) {
        Map<String, Set<Player>> heightReport = league.generateHeightReport(teamName);
        System.out.printf("Players By Height Class: ");
        heightReport.forEach(((s, players) -> {
            System.out.printf("%s: %d | ", s, players.size());
        }));
        System.out.printf("%n");
    }

    private void printTeamPlayers(String teamName) {
        System.out.printf("==== Team %s Roster ====%n%n", teamName);
        league.getTeamPlayers(teamName).forEach(player ->  {
            System.out.printf("%s%n", player.toString());
        });
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

    private ArrayList<String> getAndPrintAvailableTeams(Map<String, Team> teams, boolean onlyNonEmptyTeams) {
        ArrayList<String> choices = new ArrayList<>();
        int index = 1;
        for (Map.Entry<String, Team> team : teams.entrySet()) {
            if ((team.getValue().getPlayers().size() > 0 && onlyNonEmptyTeams) || !onlyNonEmptyTeams) {
                System.out.printf("%d.) %s%n", index, team.getValue().toString());
                choices.add(team.getValue().getName());
                index++;
            }
        }
        return choices;
    }

    private String promptForPlayerChoice(Set<Player> players) {
        System.out.printf("Available Players:%n");
        ArrayList<String> availablePlayers = getAndPrintAvailablePlayers(players);
        int choice = promptForInputExpectingInteger("Please choose an option: ", availablePlayers.size()) - 1;
        return availablePlayers.get(choice);
    }

    private String promptForTeamChoice(boolean onlyNonEmptyTeams) {
        System.out.printf("Available Teams:%n");
        ArrayList<String> availableTeams = getAndPrintAvailableTeams(league.getTeams(), onlyNonEmptyTeams);
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
