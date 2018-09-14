package com.teamtreehouse.compareUtils;

import com.teamtreehouse.model.Player;

import java.util.Comparator;

public class PlayerHeightComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        int results = 0;
        if (o1.getHeightInInches() > o2.getHeightInInches()) {
            results = 1;
        } else if (o1.getHeightInInches() < o2.getHeightInInches()) {
            results = -1;
        } else if (o2.getHeightInInches() == o2.getHeightInInches()) {
            results = o1.compareTo(o2);
        }
        return results;
    }
}
