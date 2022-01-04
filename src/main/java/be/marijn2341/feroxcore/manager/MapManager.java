package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.listeners.ArrowShootListener;
import be.marijn2341.feroxcore.listeners.BlockPlaceListener;
import be.marijn2341.feroxcore.listeners.DeathListener;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.inventorysettings.ItemStackSerializer;
import be.marijn2341.feroxcore.manager.statistics.GameStatistics;
import be.marijn2341.feroxcore.manager.statistics.PlayerStatistics;
import be.marijn2341.feroxcore.utils.ItemStacks;
import be.marijn2341.feroxcore.utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MapManager {

    public static ArrayList<String> MAPS = new ArrayList<>();
    public static ArrayList<String> PREVIOUSMAP = new ArrayList<>();
    private static Date GAMESTARTEDAT = null;
    public static boolean GAMEACTIVE = false;
    public static HashMap<String, String> CURRENTMAP = new HashMap<>();
    public static HashMap<String, Location> AREAS = new HashMap<>();
    public static HashMap<String, Location> LOBBY = new HashMap<>();

    public static int KILLS = 0;
    public static int DEATHS = 0;
    public static int ARROWSSHOT = 0;
    public static int ARROWSHIT = 0;


    public static void loadMaps() {
        MAPS.clear();
        if (Main.getInstance().getWorldsConfig().get("worlds") == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "No maps to load.");
            return;
        }
        for (String map : Main.getInstance().getWorldsConfig().getConfigurationSection("worlds").getKeys(false)) {
            MAPS.add(map);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + MAPS.toString() + " Loaded");
    }

    public static void teleportToSpawn(Player player) {

        player.setGameMode(GameMode.ADVENTURE);

        player.teleport(LOBBY.get("lobby"));
        player.setHealth(20);
        player.setFoodLevel(20);

        NametagEdit.getApi().setPrefix(player, "&3&7");

        // FIX SCOREBOARD
        ScoreboardManager.getLobbyScoreboard(player);

        // FIX PLAYER INVENTORY
        TeamManager.ClearInventory(player);
        ItemStacks is = new ItemStacks();
        player.getInventory().setItem(4, is.compass());
        player.getInventory().setItem(8, is.playerSkull(player));
    }

    // CHECK IF THERE IS A CURRENT GAME ACTIVE
    public static boolean isGameActive() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                return true;
            }
        }
        return false;
    }

    public static void startGame() {
        if (!(isGameActive())) {
            TeamManager.clearTeams();
            String newworld = getRandomWorld();
            Main.wm.cloneWorld(newworld, newworld + "_activegame");
            Main.wm.getMVWorld(newworld + "_activegame").setAlias(newworld + "_activegame");
            CURRENTMAP.put("current", newworld);

            loadSpawnPoints("red");
            loadSpawnPoints("blue");

            NexusManager.countNexuses();

            DeathListener.KILLS.clear();
            DeathListener.DEATHS.clear();
            DeathListener.ARROWSHIT.clear();
            ArrowShootListener.SHOT.clear();
            BlockPlaceListener.BLOCKS.put("placed", 0);
            BlockPlaceListener.BLOCKS.put("broken", 0);

            KILLS = 0;
            DEATHS = 0;
            ARROWSHIT = 0;
            ARROWSSHOT = 0;

            AREAS.put("redarea1", getSpawnArea1("red"));
            AREAS.put("redarea2", getSpawnArea2("red"));
            AREAS.put("bluearea1", getSpawnArea1("blue"));
            AREAS.put("bluearea2", getSpawnArea2("blue"));

            Bukkit.broadcastMessage(Utils.color("&9--- &9&lFerox&f&lMC &9---"));
            Bukkit.broadcastMessage(Utils.color("&9New map started: &f" + newworld + "&9."));
            Bukkit.broadcastMessage(Utils.color("&9----------------"));

            GAMEACTIVE = true;
            GAMESTARTEDAT = new Date();
        }
    }

    public static void ForceDeleteGame() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                PREVIOUSMAP.add(w.getName());
                Main.wm.deleteWorld(w.getName());
            }
        }
    }

    public static void EndGame(String winner) {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {

                String matchtijd = getMatchTime();

                TeamManager.WINNERS.forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&6&lVictory", "&7Your team won the game.", 5, 100, 5);

                    // ADD WIN TO DATABASE
                    Database.setWins(uuid, 1);
                    PlayerStatistics.updateStats(uuid);

                    KILLS = KILLS + DeathListener.KILLS.get(plr.getUniqueId());
                    DEATHS = DEATHS + DeathListener.DEATHS.get(plr.getUniqueId());
                    ARROWSSHOT = ARROWSSHOT + ArrowShootListener.SHOT.get(plr.getUniqueId());
                    ARROWSHIT = ARROWSHIT + DeathListener.ARROWSHIT.get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    GameStatistics.sendGameStats(plr, matchtijd);
                });

                TeamManager.LOSERS.forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&c&lLost", "&7Your team lost the game.", 5, 100, 5);

                    // ADD LOSE TO DATABASE
                    Database.setLoses(uuid, 1);
                    // ADD PLAYER STATS TO DATABASE
                    PlayerStatistics.updateStats(uuid);

                    KILLS = KILLS + DeathListener.KILLS.get(plr.getUniqueId());
                    DEATHS = DEATHS + DeathListener.DEATHS.get(plr.getUniqueId());
                    ARROWSSHOT = ARROWSSHOT + ArrowShootListener.SHOT.get(plr.getUniqueId());
                    ARROWSHIT = ARROWSHIT + DeathListener.ARROWSHIT.get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    GameStatistics.sendGameStats(plr, matchtijd);
                });

                if (!(winner.equalsIgnoreCase("null"))) {
                    TeamManager.SPECTATORS.forEach(uuid -> {
                        Player plr = Bukkit.getPlayer(uuid);
                        Utils.sendTitle(plr, "&6" + winner + " &7won the game!", "", 5, 80, 25);
                    });
                }

                // ADD GAME STATS TO DATABASE
                Database.insertMapStats(CURRENTMAP.get("current"), BlockPlaceListener.BLOCKS.get("placed"), BlockPlaceListener.BLOCKS.get("broken"),
                        getMatchTimeMi(), winner, KILLS, DEATHS, ARROWSSHOT, ARROWSHIT);

                GAMEACTIVE = false;

                TeamManager.LOSERS.clear();
                TeamManager.WINNERS.clear();
                TeamManager.clearTeams();
                NexusManager.REDNEXUSESLOC.clear();
                NexusManager.BLUENEXUSESLOC.clear();
                NexusManager.REDNEXUSESLOCTOTAL.clear();
                NexusManager.BLUENEXUSESLOCTOTAL.clear();
                TeamManager.SPAWNPOINTS.clear();

                GAMESTARTEDAT = null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (UUID uuid : TeamManager.PLAYERS) {
                            Player plr = Bukkit.getPlayer(uuid);
                            teleportToSpawn(plr);
                            PREVIOUSMAP.clear();
                            String wereld = w.getName().substring(0, w.getName().length()-11);
                            PREVIOUSMAP.add(wereld);
                            Main.wm.deleteWorld(w.getName());

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    startGame();
                                    TeamManager.PLAYERS.clear();
                                }
                            }.runTaskLater(Main.getInstance(), 40);
                        }
                    }
                }.runTaskLater(Main.getInstance(), 300);
            }
        }
    }

    public static String getRandomWorld() {
        if (MAPS.isEmpty()) {
            loadMaps();
        }
        if (!(PREVIOUSMAP.isEmpty())) {
            MAPS.remove(PREVIOUSMAP.get(0));
            if (MAPS.isEmpty()) {
                loadMaps();
                MAPS.remove(PREVIOUSMAP.get(0));
            }
            MAPS.remove(PREVIOUSMAP.get(0));
            String map = MAPS.get(new Random().nextInt(MAPS.size()));
            return map;
        }
        String map = MAPS.get(new Random().nextInt(MAPS.size()));
        MAPS.remove(map);
        return map;
    }

    public static String getMatchTime() {
        long milliseconds = new Date().getTime() - GAMESTARTEDAT.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd =  hours + "h, " + minutes + "min";
        return tijd;
    }

    public static long getMatchTimeMi() {
        long milliseconds = new Date().getTime() - GAMESTARTEDAT.getTime();
        return milliseconds;
    }

    public static void collectItems(Player player) {
        String inv = Database.getInventory(player);
        ItemStack[] is = ItemStackSerializer.deserialize(inv);
        player.getInventory().setContents(is);
        player.updateInventory();
    }

    public static void collectArmor(Player player, Color c) {

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setColor(c);
        helmet.setItemMeta(meta);
        leggings.setItemMeta(meta);
        boots.setItemMeta(meta);

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chest);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);

        player.updateInventory();
    }

    // ONLY LOADS WHEN A MAP STARTS.
    public static void loadSpawnPoints(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World world = Bukkit.getWorld(MapManager.CURRENTMAP.get("current") + "_activegame");
        double x = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".z");
        float yaw = (float) config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".yaw");
        float pitch = (float) config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".pitch");
        Location loc = new Location(world, x, y, z, yaw, pitch);

        TeamManager.SPAWNPOINTS.put(team, loc);
    }

    // LETS TRUST LUC CODE ;)
    public static Boolean iswithin(Location original, Location loc1, Location loc2) {
        double xMin, yMin, zMin;
        double xMax, yMax, zMax;
        if (loc1.getX() > loc2.getX()) {
            xMax = loc1.getX();
            xMin = loc2.getX();
        } else {
            xMax = loc2.getX();
            xMin = loc1.getX();
        }
        if (loc1.getY() > loc2.getY()) {
            yMax = loc1.getY();
            yMin = loc2.getY();
        } else {
            yMax = loc2.getY();
            yMin = loc1.getY();
        }
        if (loc1.getZ() > loc2.getZ()) {
            zMax = loc1.getZ();
            zMin = loc2.getZ();
        } else {
            zMax = loc2.getZ();
            zMin = loc1.getZ();
        }
        if (original.getX() < xMax && original.getX() > xMin) {
            if (original.getY() < yMax && original.getY() > yMin) {
                if (original.getZ() < zMax && original.getZ() > zMin) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Location getSpawnArea1(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World game = Bukkit.getWorld(MapManager.CURRENTMAP.get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".z");

        // SET AREA 1

        double area1x = x-2;
        double area1y = y-5;
        double area1z = z-2;

        Location area1 = new Location(game, area1x, area1y, area1z);
        return area1;
    }

    public static Location getSpawnArea2(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World game = Bukkit.getWorld(MapManager.CURRENTMAP.get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".spawnpoints." +
                team + ".z");

        // SET AREA 2
        double area2x = x+2;
        double area2y = y+4;
        double area2z = z+2;

        Location area2 = new Location(game, area2x, area2y, area2z);
        return area2;
    }

    public static void loadLobby() {
        if (!(LOBBY.isEmpty())) {
            LOBBY.clear();
        }
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(config.getString("lobby.world"));
        double x = config.getDouble("lobby.x");
        double y = config.getDouble("lobby.y");
        double z = config.getDouble("lobby.z");
        float yaw = (float) config.getDouble("lobby.yaw");
        float pitch = (float) config.getDouble("lobby.pitch");
        Location loc = new Location(lobby, x, y, z, yaw, pitch);
        MapManager.LOBBY.put("lobby", loc);
    }
}