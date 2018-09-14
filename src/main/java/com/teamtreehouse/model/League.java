package com.teamtreehouse.model;

import com.teamtreehouse.exceptions.MaximumTeamsException;
import com.teamtreehouse.exceptions.PlayerAlreadyOnTeamException;
import com.teamtreehouse.exceptions.TeamExistsException;

import java.util.*;

public class League {
    private Map<String, int[]> heightClasses;
    private Map<String, Team> teams;
    private Set<Player> freePlayers;

    public League(Player[] players) {
        freePlayers = new TreeSet<>(Arrays.asList(players));
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

    public Set<Player> getFreePlayers() {
        return freePlayers;
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : freePlayers) {
            if (player.getName().equals(playerName)) { return player; }
        }
        return null;
    }

    public boolean createTeam(String teamName, String coachName) throws Exception {
        if (teams.size() > freePlayers.size()) {
            throw new MaximumTeamsException("The number of teams exceeds the amount of free players. No more teams may be created!");
        } else {
            Team newTeam = new Team(teamName, coachName);
            if  (teams.put(teamName, newTeam) == null) {
                return true;
            } else throw new TeamExistsException("The team " + teamName + " already exists!");
        }
    }

    public void addPlayerToTeam(String teamName, String playerName) throws PlayerAlreadyOnTeamException {
        Player playerToAdd = getPlayerByName(playerName);
        boolean results = teams.get(teamName).addPlayer(playerToAdd);
        if (results) { freePlayers.remove(playerToAdd); } else { throw new PlayerAlreadyOnTeamException(playerName + " is already on Team " + teamName + "!"); }
    }

    public void removePlayerFromTeam(String teamName, String playerName) {
        freePlayers.add(teams.get(teamName).removePlayer(playerName));
    }

}
