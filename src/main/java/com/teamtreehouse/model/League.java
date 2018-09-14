package com.teamtreehouse.model;

import com.teamtreehouse.exceptions.MaximumTeamsException;

import java.util.*;

public class League {
    private TreeMap heightClasses;
    private Map<String, Team> teams;
    private TreeMap<String, Player> freePlayers;

    public League(Player[] players) {
        freePlayers = new TreeMap<>();
        Arrays.asList(players).forEach((player -> {
            freePlayers.put(player.getName(), player);
        }));
        heightClasses = new TreeMap<String, int[]>()
        {{
            put("small", new int[]{35, 40});
            put("medium", new int[]{41, 46});
            put("large", new int[]{47, 50});
        }};
        teams = new TreeMap<>();
    }

    public Map<String, int[]> getHeightClasses() {
        return heightClasses;
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public Map<String, Player> getFreePlayers() {
        return freePlayers;
    }

    public boolean createTeam(String teamName, String coachName) throws MaximumTeamsException {
        if (teams.size() > freePlayers.size()) {
            throw new MaximumTeamsException("The number of teams exceeds the amount of free players. No more teams may be created!");
        } else {
            Team newTeam = new Team(teamName, coachName);
            if  (teams.put(teamName, newTeam) == null) {
                return true;
            } else return false;
        }
    }

    public boolean addPlayerToTeam(String teamName, String playerName) {
        return teams.get(teamName).addPlayer(freePlayers.remove(playerName));
    }

    public void removePlayerFromTeam(String teamName, String playerName) {
        freePlayers.put(playerName, teams.get(teamName).removePlayer(playerName));
    }

}
