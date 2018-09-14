package com.teamtreehouse.compareUtils;

import com.teamtreehouse.model.Player;

import java.util.Comparator;

public class PlayerExperienceComparator implements Comparator<Player> {

    @Override
    public int compare(Player o1, Player o2) {
        int results = 0;

        results = Boolean.compare(o1.isPreviousExperience(), o2.isPreviousExperience());

        if (results == 0) { results = o1.compareTo(o2); }

        return results;
    }
}
