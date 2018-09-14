package com.teamtreehouse.model;

import com.teamtreehouse.compareUtils.PlayerExperienceComparator;
import com.teamtreehouse.compareUtils.PlayerHeightComparator;

import java.util.*;

public class Team implements Comparable<Team> {
    private String teamName;
    private String coachName;
    private Map<String, Player> players;

    public Team(String teamName, String coachName) {
        this.teamName = teamName;
        this.coachName = coachName;
        players = new TreeMap<String, Player>();
    }

    public String getName() {
        return teamName;
    }

    public String getCoachName() {
        return coachName;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Player getPlayerByName(String playerName) {
        for (Map.Entry<String, Player> player : players.entrySet()) {
            if (playerName.equals(player.getKey())) {
                return player.getValue();
            }
        }
        return null;
    }

    public boolean addPlayer(Player player) {
        if (players.get(player.getName()) == null) {
            players.put(player.getName(), player);
            return true;
        } else return false;
    }

    public Player removePlayer(String playerName) {
        return players.remove(playerName);
    }

    public double calculateExperienceRating() {
        int countOfExperienced = 0;
        for (Map.Entry<String, Player> player : players.entrySet()) {
            if (player.getValue().isPreviousExperience()) {
                countOfExperienced++;
            }
        }
        return (countOfExperienced / players.size()) * 100;
    }

    public Set<Player> getPlayersByHeight() {
        Set<Player> results = new TreeSet<>(new PlayerHeightComparator());
        results.addAll(players.values());
        return results;
    }

    public Set<Player> getPlayersByExperience() {
        Set<Player> results = new TreeSet<>(new PlayerExperienceComparator());
        results.addAll(players.values());
        return results;
    }

    @Override
    public int compareTo(Team other) {
        if (this == other) return 0;
        return this.teamName.compareTo(other.teamName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;

        Team team = (Team) o;

        return teamName.equals(team.getName());
    }

    @Override
    public String toString() {
        return "Team " + teamName + " coached by " + coachName;
    }
}
