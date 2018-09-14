package com.teamtreehouse.model;

import com.teamtreehouse.compareUtils.PlayerHeightComparator;
import com.teamtreehouse.exceptions.MaximumTeamsException;
import com.teamtreehouse.exceptions.PlayerAlreadyOnTeamException;
import com.teamtreehouse.exceptions.TeamExistsException;
import com.teamtreehouse.exceptions.TeamFullException;

import java.util.*;

public class League {
    private Map<String, int[]> heightClasses;
    private Map<String, Team> teams;
    private Set<Player> freePlayers;

    public League(Player[] players) {
        freePlayers = new TreeSet<>(Arrays.asList(players));
        heightClasses = new TreeMap<String, int[]>()
        {{
            put("Short", new int[]{35, 40});
            put("Medium", new int[]{41, 46});
            put("Tall", new int[]{47, 50});
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

    public Set<Player> getTeamPlayers(String teamName) {
        return new TreeSet<Player>(teams.get(teamName).getPlayers().values());
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : freePlayers) {
            if (player.getName().equals(playerName)) { return player; }
        }
        return null;
    }

    public int getAssignedPlayerCount() {
        int count = 0;
        for(Map.Entry<String, Team> team : getTeams().entrySet()) {
            count += team.getValue().getPlayers().size();
        }
        return count;
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

    public void addPlayerToTeam(String teamName, String playerName) throws PlayerAlreadyOnTeamException, TeamFullException {
        Team selectedTeam = teams.get(teamName);
        if (selectedTeam.getPlayers().size() >= 11) {
            throw new TeamFullException("Team " + teamName + " is full! You must remove at least 1 player before adding another.");
        } else {
            Player playerToAdd = getPlayerByName(playerName);
            boolean results = teams.get(teamName).addPlayer(playerToAdd);
            if (results) { freePlayers.remove(playerToAdd); } else { throw new PlayerAlreadyOnTeamException(playerName + " is already on Team " + teamName + "!"); }
        }
    }

    public void removePlayerFromTeam(String teamName, String playerName) {
        freePlayers.add(teams.get(teamName).removePlayer(playerName));
    }

    public Map<String, Set<Player>> generateHeightReport(String teamName) {
        Set<Player> playersByHeight = teams.get(teamName).getPlayersByHeight();
        Map<String, Set<Player>> reportResults = new TreeMap<>();
        for (Map.Entry<String, int[]> heightClass : heightClasses.entrySet()) {
            int minHeight = heightClass.getValue()[0];
            int maxHeight = heightClass.getValue()[1];
            String categoryName = minHeight + "-" + maxHeight + " inches (" + heightClass.getKey() + ")";
            reportResults.put(categoryName, new TreeSet<Player>(new PlayerHeightComparator()));
            playersByHeight.forEach(player -> {
                if (player.getHeightInInches() >= minHeight && player.getHeightInInches() <= maxHeight) {
                    reportResults.get(categoryName).add(player);
                }
            });
        }
        return reportResults;
    }

}
