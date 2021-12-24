package be.marijn2341.feroxcore.Manager;

import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    public static ArrayList<UUID> TeamRed = new ArrayList();
    public static ArrayList<UUID> TeamBlue = new ArrayList();
    public static ArrayList<UUID> Winners = new ArrayList();
    public static ArrayList<UUID> Losers = new ArrayList();
    public static ArrayList<UUID> ToSpawn = new ArrayList();
    public static ArrayList<UUID> Spectator = new ArrayList<>();

    public static void addToTeam(String team, Player player) {
        if (team.equalsIgnoreCase("red")) {
            TeamRed.add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("blue")) {
            TeamBlue.add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("spectator")) {
            Spectator.add(player.getUniqueId());
        }
        ScoreboardManager.GetGameScoreboard(player);
     }

    public static void clearTeams() {
            TeamRed.clear();
            TeamBlue.clear();
            Spectator.clear();
    }

    public static void addToRandomTeam(Player player) {
        if (TeamManager.TeamBlue.size() > TeamManager.TeamRed.size()) {
            addToTeam("red", player);
            processToTeam("red", player);
        } else if (TeamManager.TeamRed.size() > TeamManager.TeamBlue.size()) {
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

    public static int CountOnlinePlayers() {
        int online = Bukkit.getOnlinePlayers().size();
        return online;
    }

    public static boolean CheckTeamsUnbalanced(String team) {
        if (team.equalsIgnoreCase("blue")) {
            if (TeamManager.TeamBlue.size() > TeamManager.TeamRed.size()) {
                return true;
            }
        } else if (team.equalsIgnoreCase("red")) {
            if (TeamManager.TeamRed.size() > TeamManager.TeamBlue.size()) {
                return true;
            }
        }
        return false;
    }

    public static void processToTeam(String team, Player player) {

        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");
        double x = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".z");
        float yaw = (float) config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".yaw");
        float pitch = (float) config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".pitch");
        Location loc = new Location(lobby, x, y, z, yaw, pitch);
        player.teleport(loc);

        if (!(DeathListener.Deaths.containsKey(player.getUniqueId()))) {
            DeathListener.Deaths.put(player.getUniqueId(), 0);
        }
        if (!(DeathListener.Kills.containsKey(player.getUniqueId()))) {
            DeathListener.Kills.put(player.getUniqueId(), 0);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setFoodLevel(20);
        player.setHealth(20);
        MapManager.CollectItems(player);
        if (TeamBlue.contains(player.getUniqueId())) {
            MapManager.CollectArmor(player, Color.BLUE);
            NametagEdit.getApi().setPrefix(player, "&2&9");
        } else if (TeamRed.contains(player.getUniqueId())) {
            MapManager.CollectArmor(player, Color.RED);
            NametagEdit.getApi().setPrefix(player, "&c");
        }
    }

    public static boolean AllreadyInTeam(Player player) {
        if (TeamRed.contains(player.getUniqueId()) || TeamBlue.contains(player.getUniqueId()) || Spectator.contains(player.getUniqueId())) {
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
        if (TeamRed.contains(player.getUniqueId())) {
            return "c";
        } else if (TeamBlue.contains(player.getUniqueId())) {
            return "9";
        }
        return "0";
    }

    public static void SetAsSpectator(Player player) {

        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");
        double x = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.red.x");
        double y = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.red.y");
        double z = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.red.z");
        double yaw = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.red.yaw");
        double pitch = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.red.pitch");
        Location red = new Location(lobby, x, y, z, (float) yaw, (float) pitch);

        double x2 = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.blue.x");
        double y2 = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.blue.y");
        double z2 = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.blue.z");
        double yaw2 = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.blue.yaw");
        double pitch2 = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints.blue.pitch");
        Location blue = new Location(lobby, x2, y2, z2, (float) yaw2, (float) pitch2);

        List<Location> spawn = Arrays.asList(red, blue);
        Random rand = new Random();

        player.teleport(spawn.get(rand.nextInt(spawn.size())));
        Spectator.add(player.getUniqueId());
        player.sendTitle(Utils.color("&9&lSpectator"), Utils.color("&7You can get back with /lobby."));
        player.setGameMode(GameMode.SPECTATOR);
    }
}
