import com.teamtreehouse.Menu;
import com.teamtreehouse.model.League;
import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class LeagueManager {

    public static void main(String[] args) {
        Player[] players = Players.load();
        System.out.printf("There are currently %d registered players.%n", players.length);

        League league = new League(players);
        Menu menu = new Menu(league);
        menu.displayMenu();
    }
}
