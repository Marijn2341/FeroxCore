package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class DataManager {

    // ARRAYLISTS
    private final ArrayList<UUID> TEAMRED = new ArrayList();
    private final ArrayList<UUID> TEAMBLUE = new ArrayList();
    private final ArrayList<UUID> WINNERS = new ArrayList();
    private final ArrayList<UUID> LOSERS = new ArrayList();
    private final ArrayList<UUID> SPECTATORS = new ArrayList<>();
    private final ArrayList<UUID> PLAYERS = new ArrayList<>();
    private final ArrayList<ItemStack> DEFAULTITEMS = new ArrayList<ItemStack>();
    private final ArrayList<String> MAPS = new ArrayList<>();
    private final ArrayList<String> PREVIOUSMAP = new ArrayList<>();

    // HASHMAPS
    private final HashMap<String, Location> SPAWNPOINTS = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> INVENTORIES = new HashMap<>();
    private final HashMap<String, Location> REDNEXUSESLOC = new HashMap<>();
    private final HashMap<String, Location> BLUENEXUSESLOC = new HashMap<>();
    private final HashMap<String, Location> REDNEXUSESLOCTOTAL = new HashMap<>();
    private final HashMap<String, Location> BLUENEXUSESLOCTOTAL = new HashMap<>();
    private final HashMap<String, String> CURRENTMAP = new HashMap<>();
    private final HashMap<String, Location> AREAS = new HashMap<>();
    private final HashMap<String, Location> LOBBY = new HashMap<>();
    private final HashMap<String, Integer> BLOCKS = new HashMap<>();
    private final HashMap<UUID, Integer> DEATHS = new HashMap<>();
    private final HashMap<UUID, Integer> KILLS = new HashMap<>();
    private final HashMap<Player, BukkitTask> QUEUE = new HashMap<>();
    private final HashMap<Player, Player> LASTDAMAGER = new HashMap<>();
    private final HashMap<UUID, Integer> ARROWSHIT = new HashMap<>();
    private final HashMap<UUID, Integer> ARROWSSHOT = new HashMap<>();

    private final HashMap<UUID, Integer> LEVEL = new HashMap<>();
    private final HashMap<UUID, Integer> XP = new HashMap<>();

    public ArrayList<UUID> getTeamRed() {
        return TEAMRED;
    }

    public ArrayList<UUID> getTeamBlue() {
        return TEAMBLUE;
    }

    public ArrayList<UUID> getWinners() {
        return WINNERS;
    }

    public ArrayList<UUID> getLosers() {
        return LOSERS;
    }

    public ArrayList<UUID> getSpectators() {
        return SPECTATORS;
    }

    public ArrayList<UUID> getPlayers() {
        return PLAYERS;
    }

    public ArrayList<ItemStack> getDefaultItems() {
        return DEFAULTITEMS;
    }

    public ArrayList<String> getMaps() {
        return MAPS;
    }

    public ArrayList<String> getPreviousMap() {
        return PREVIOUSMAP;
    }

    // HASHMAPS
    public HashMap<String, Location> getSpawnPoints() {
        return SPAWNPOINTS;
    }

    public HashMap<UUID, ItemStack[]> getInventories() {
        return INVENTORIES;
    }

    public HashMap<String, Location> getRedNexusesLoc() {
        return REDNEXUSESLOC;
    }

    public HashMap<String, Location> getRedNexusesLocTotal() {
        return REDNEXUSESLOCTOTAL;
    }

    public HashMap<String, Location> getBlueNexusesLoc() {
        return BLUENEXUSESLOC;
    }

    public HashMap<String, Location> getBlueNexusesLocTotal() {
        return BLUENEXUSESLOCTOTAL;
    }

    public HashMap<String, String> getCurrentMap() {
        return CURRENTMAP;
    }

    public HashMap<String, Location> getAreas() {
        return AREAS;
    }

    public HashMap<String, Location> getLobby() {
        return LOBBY;
    }

    public HashMap<String, Integer> getBlocks() {
        return BLOCKS;
    }

    public HashMap<UUID, Integer> getDeaths() {
        return DEATHS;
    }

    public HashMap<UUID, Integer> getKills() {
        return KILLS;
    }

    public HashMap<Player, BukkitTask> getQueue() {
        return QUEUE;
    }

    public HashMap<Player, Player> getLastDamager() {
        return LASTDAMAGER;
    }

    public HashMap<UUID, Integer> getArrowsHit() {
        return ARROWSHIT;
    }

    public HashMap<UUID, Integer> getArrowsShot() {
        return ARROWSSHOT;
    }

    public HashMap<UUID, Integer> getLevel() { return LEVEL; }

    public HashMap<UUID, Integer> getXP() { return XP; }

    public void clearLists() {
        getLosers().clear();
        getWinners().clear();
        Main.getInstance().getTeamManager().clearTeams();
        getRedNexusesLoc().clear();
        getBlueNexusesLoc().clear();
        getRedNexusesLocTotal().clear();
        getBlueNexusesLocTotal().clear();
        getSpawnPoints().clear();
        getInventories().clear();
    }
}
