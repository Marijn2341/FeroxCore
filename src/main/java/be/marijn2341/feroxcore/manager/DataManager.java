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
    private ArrayList<UUID> TEAMRED = new ArrayList();
    private ArrayList<UUID> TEAMBLUE = new ArrayList();
    private ArrayList<UUID> WINNERS = new ArrayList();
    private ArrayList<UUID> LOSERS = new ArrayList();
    private ArrayList<UUID> SPECTATORS = new ArrayList<>();
    private ArrayList<UUID> PLAYERS = new ArrayList<>();
    private ArrayList<ItemStack> DEFAULTITEMS = new ArrayList<ItemStack>();
    private ArrayList<String> MAPS = new ArrayList<>();
    private ArrayList<String> PREVIOUSMAP = new ArrayList<>();

    // HASHMAPS
    private HashMap<String, Location> SPAWNPOINTS = new HashMap<>();
    private HashMap<UUID, ItemStack[]> INVENTORIES = new HashMap<>();
    private HashMap<String, Location> REDNEXUSESLOC = new HashMap<>();
    private HashMap<String, Location> BLUENEXUSESLOC = new HashMap<>();
    private HashMap<String, Location> REDNEXUSESLOCTOTAL = new HashMap<>();
    private HashMap<String, Location> BLUENEXUSESLOCTOTAL = new HashMap<>();
    private HashMap<String, String> CURRENTMAP = new HashMap<>();
    private HashMap<String, Location> AREAS = new HashMap<>();
    private HashMap<String, Location> LOBBY = new HashMap<>();
    private HashMap<String, Integer> BLOCKS = new HashMap<>();
    private HashMap<UUID, Integer> DEATHS = new HashMap<>();
    private HashMap<UUID, Integer> KILLS = new HashMap<>();
    private HashMap<Player, BukkitTask> QUEUE = new HashMap<>();
    private HashMap<Player, Player> LASTDAMAGER = new HashMap<>();
    private HashMap<UUID, Integer> ARROWSHIT = new HashMap<>();
    private HashMap<UUID, Integer> ARROWSSHOT = new HashMap<>();

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
}
