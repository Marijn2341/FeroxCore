package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.listeners.ArrowShootListener;
import be.marijn2341.feroxcore.listeners.DeathListener;
import be.marijn2341.feroxcore.utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    public static ArrayList<UUID> TEAMRED = new ArrayList();
    public static ArrayList<UUID> TEAMBLUE = new ArrayList();
    public static ArrayList<UUID> WINNERS = new ArrayList();
    public static ArrayList<UUID> LOSERS = new ArrayList();
    public static ArrayList<UUID> SPECTATORS = new ArrayList<>();
    public static ArrayList<UUID> PLAYERS = new ArrayList<>();
    public static HashMap<String, Location> SPAWNPOINTS = new HashMap<>();

    public static void addToTeam(String team, Player player) {
        if (team.equalsIgnoreCase("red")) {
            TEAMRED.add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("blue")) {
            TEAMBLUE.add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("spectator")) {
            SPECTATORS.add(player.getUniqueId());
        }

        PLAYERS.add(player.getUniqueId());
        ScoreboardManager.getGameScoreboard(player);
     }

    public static void clearTeams() {
            TEAMRED.clear();
            TEAMBLUE.clear();
            SPECTATORS.clear();
    }

    public static void addToRandomTeam(Player player) {
        if (TeamManager.TEAMBLUE.size() > TeamManager.TEAMRED.size()) {
            addToTeam("red", player);
            processToTeam("red", player);
        } else if (TeamManager.TEAMRED.size() > TeamManager.TEAMBLUE.size()) {
            addToTeam("blue", player);
            processToTeam("blue", player);
        } else {
            if (Math.random() < 0.5) {
                addToTeam("red", player);
                processToTeam("red", player);
            } else {
                addToTeam("blue", player);
                processToTeam("blue", player);
            }
        }
    }

    public static int countOnlinePlayers() {
        int online = Bukkit.getOnlinePlayers().size();
        return online;
    }

    public static boolean checkTeamsUnbalanced(String team) {
        if (team.equalsIgnoreCase("blue")) {
            if (TeamManager.TEAMBLUE.size() > TeamManager.TEAMRED.size()) {
                return true;
            }
        } else if (team.equalsIgnoreCase("red")) {
            if (TeamManager.TEAMRED.size() > TeamManager.TEAMBLUE.size()) {
                return true;
            }
        }
        return false;
    }

    public static void processToTeam(String team, Player player) {
        // TELEPORT TO TEAM SPAWN
        player.teleport(SPAWNPOINTS.get(team));

        if (!(DeathListener.DEATHS.containsKey(player.getUniqueId()))) {
            DeathListener.DEATHS.put(player.getUniqueId(), 0);
        }
        if (!(DeathListener.KILLS.containsKey(player.getUniqueId()))) {
            DeathListener.KILLS.put(player.getUniqueId(), 0);
        }
        if (!(DeathListener.ARROWSHIT.containsKey(player.getUniqueId()))) {
            DeathListener.ARROWSHIT.put(player.getUniqueId(), 0);
        }
        if (!(ArrowShootListener.SHOT.containsKey(player.getUniqueId()))) {
            ArrowShootListener.SHOT.put(player.getUniqueId(), 0);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setFoodLevel(20);
        player.setHealth(20);
        MapManager.collectItems(player);
        if (TEAMBLUE.contains(player.getUniqueId())) {
            MapManager.collectArmor(player, Color.BLUE);
            NametagEdit.getApi().setPrefix(player, "&2&9");
        } else if (TEAMRED.contains(player.getUniqueId())) {
            MapManager.collectArmor(player, Color.RED);
            NametagEdit.getApi().setPrefix(player, "&c");
        }
    }

    public static boolean AllreadyInTeam(Player player) {
        if (TEAMRED.contains(player.getUniqueId()) || TEAMBLUE.contains(player.getUniqueId()) || SPECTATORS.contains(player.getUniqueId())) {
            return true;
        }
        return false;
    }

    public static void ClearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.updateInventory();
    }

    public static String GetTeamColor(Player player) {
        if (TEAMRED.contains(player.getUniqueId())) {
            return "c";
        } else if (TEAMBLUE.contains(player.getUniqueId())) {
            return "9";
        }
        return "0";
    }

    public static void SetAsSpectator(Player player) {
        Location red = SPAWNPOINTS.get("red");
        Location blue = SPAWNPOINTS.get("blue");

        List<Location> spawn = Arrays.asList(red, blue);
        Random rand = new Random();

        player.teleport(spawn.get(rand.nextInt(spawn.size())));
        SPECTATORS.add(player.getUniqueId());
        player.sendTitle(Utils.color("&9&lSpectator"), Utils.color("&7You can get back with /lobby."));
        player.setGameMode(GameMode.SPECTATOR);
    }
}
