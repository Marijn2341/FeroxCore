package be.marijn2341.feroxcore.Manager;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Listeners.ArrowShootListener;
import be.marijn2341.feroxcore.Listeners.BlockPlaceListener;
import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.InventorySettings.ItemStackSerializer;
import be.marijn2341.feroxcore.Manager.Statistics.GameStatistics;
import be.marijn2341.feroxcore.Manager.Statistics.PlayerStatistics;
import be.marijn2341.feroxcore.Utils.ItemStacks;
import be.marijn2341.feroxcore.Utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MapManager {

    public static ArrayList<String> Maps = new ArrayList<>();
    public static ArrayList<String> PreviousMap = new ArrayList<>();
    private static Date GameStartedAt = null;
    public static boolean GameActive = false;
    public static HashMap<String, String> currentmap = new HashMap<>();
    public static HashMap<String, Location> areas = new HashMap<>();
    public static HashMap<String, Location> lobby = new HashMap<>();

    public static int kills = 0;
    public static int deaths = 0;
    public static int arrowsshot = 0;
    public static int arrowshit = 0;


    public static void LoadMaps() {
        Maps.clear();
        if (Main.getInstance().getWorldsConfig().get("worlds") == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "No maps to load.");
            return;
        }
        for (String map : Main.getInstance().getWorldsConfig().getConfigurationSection("worlds").getKeys(false)) {
            Maps.add(map);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Maps.toString() + " Loaded");
    }

    public static void TeleportToSpawn(Player player) {

        player.setGameMode(GameMode.ADVENTURE);

        player.teleport(lobby.get("lobby"));
        player.setHealth(20);
        player.setFoodLevel(20);

        NametagEdit.getApi().setPrefix(player, "&3&7");

        // FIX SCOREBOARD
        ScoreboardManager.GetLobbyScoreboard(player);

        // FIX PLAYER INVENTORY
        TeamManager.ClearInventory(player);
        ItemStacks is = new ItemStacks();
        player.getInventory().setItem(4, is.compass());
        player.getInventory().setItem(8, is.PlayerSkull(player));
    }

    // CHECK IF THERE IS A CURRENT GAME ACTIVE
    public static boolean IsGameActive() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                return true;
            }
        }
        return false;
    }

    public static void StartGame() {
        if (!(IsGameActive())) {
            TeamManager.clearTeams();
            String newworld = GetRandomWorld();
            Main.wm.cloneWorld(newworld, newworld + "_activegame");
            Main.wm.getMVWorld(newworld + "_activegame").setAlias(newworld + "_activegame");
            currentmap.put("current", newworld);

            loadSpawnPoints("red");
            loadSpawnPoints("blue");

            NexusManager.CountNexuses();

            DeathListener.Kills.clear();
            DeathListener.Deaths.clear();
            DeathListener.arrowshit.clear();
            ArrowShootListener.shot.clear();
            BlockPlaceListener.blocks.put("placed", 0);
            BlockPlaceListener.blocks.put("broken", 0);

            kills = 0;
            deaths = 0;
            arrowshit = 0;
            arrowsshot = 0;

            areas.put("redarea1", getSpawnArea1("red"));
            areas.put("redarea2", getSpawnArea2("red"));
            areas.put("bluearea1", getSpawnArea1("blue"));
            areas.put("bluearea2", getSpawnArea2("blue"));

            Bukkit.broadcastMessage(Utils.color("&9--- &9&lFerox&f&lMC &9---"));
            Bukkit.broadcastMessage(Utils.color("&9New map started: &f" + newworld + "&9."));
            Bukkit.broadcastMessage(Utils.color("&9----------------"));

            GameActive = true;
            GameStartedAt = new Date();
        }
    }

    public static void ForceDeleteGame() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                PreviousMap.add(w.getName());
                Main.wm.deleteWorld(w.getName());
            }
        }
    }

    public static void EndGame(String winner) {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {

                String matchtijd = GetMatchTime();

                TeamManager.Winners.forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&6&lVictory", "&7Your team won the game.", 5, 100, 5);

                    // ADD WIN TO DATABASE
                    Database.SetWins(uuid, 1);
                    PlayerStatistics.updateStats(uuid);

                    kills = kills + DeathListener.Kills.get(plr.getUniqueId());
                    deaths = deaths + DeathListener.Deaths.get(plr.getUniqueId());
                    arrowsshot = arrowsshot + ArrowShootListener.shot.get(plr.getUniqueId());
                    arrowshit = arrowshit + DeathListener.arrowshit.get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    GameStatistics.sendGameStats(plr, matchtijd);
                });

                TeamManager.Losers.forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&c&lLost", "&7Your team lost the game.", 5, 100, 5);

                    // ADD LOSE TO DATABASE
                    Database.SetLoses(uuid, 1);
                    // ADD PLAYER STATS TO DATABASE
                    PlayerStatistics.updateStats(uuid);

                    kills = kills + DeathListener.Kills.get(plr.getUniqueId());
                    deaths = deaths + DeathListener.Deaths.get(plr.getUniqueId());
                    arrowsshot = arrowsshot + ArrowShootListener.shot.get(plr.getUniqueId());
                    arrowshit = arrowshit + DeathListener.arrowshit.get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    GameStatistics.sendGameStats(plr, matchtijd);
                });

                if (!(winner.equalsIgnoreCase("null"))) {
                    TeamManager.Spectator.forEach(uuid -> {
                        Player plr = Bukkit.getPlayer(uuid);
                        Utils.sendTitle(plr, "&6" + winner + " &7won the game!", "", 5, 80, 25);
                    });
                }

                // ADD GAME STATS TO DATABASE
                Database.InsertMapStats(currentmap.get("current"), BlockPlaceListener.blocks.get("placed"), BlockPlaceListener.blocks.get("broken"),
                        GetMatchTimeMi(), winner, kills, deaths, arrowsshot, arrowshit);

                GameActive = false;

                TeamManager.Losers.clear();
                TeamManager.Winners.clear();
                TeamManager.clearTeams();
                NexusManager.RedNexusesLoc.clear();
                NexusManager.BlueNexusesLoc.clear();
                NexusManager.RedNexusesLocTOTAL.clear();
                NexusManager.BlueNexusesLocTOTAL.clear();
                TeamManager.Spawnpoints.clear();

                GameStartedAt = null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (UUID uuid : TeamManager.Players) {
                            Player plr = Bukkit.getPlayer(uuid);
                            TeleportToSpawn(plr);
                            PreviousMap.clear();
                            String wereld = w.getName().substring(0, w.getName().length()-11);
                            PreviousMap.add(wereld);
                            Main.wm.deleteWorld(w.getName());

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    StartGame();
                                    TeamManager.Players.clear();
                                }
                            }.runTaskLater(Main.getInstance(), 40);
                        }
                    }
                }.runTaskLater(Main.getInstance(), 300);
            }
        }
    }

    public static String GetRandomWorld() {
        if (Maps.isEmpty()) {
            LoadMaps();
        }
        if (!(PreviousMap.isEmpty())) {
            Maps.remove(PreviousMap.get(0));
            if (Maps.isEmpty()) {
                LoadMaps();
                Maps.remove(PreviousMap.get(0));
            }
            Maps.remove(PreviousMap.get(0));
            String map = Maps.get(new Random().nextInt(Maps.size()));
            return map;
        }
        String map = Maps.get(new Random().nextInt(Maps.size()));
        Maps.remove(map);
        return map;
    }

    public static String GetMatchTime() {
        long milliseconds = new Date().getTime() - GameStartedAt.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd =  hours + "h, " + minutes + "min";
        return tijd;
    }

    public static long GetMatchTimeMi() {
        long milliseconds = new Date().getTime() - GameStartedAt.getTime();
        return milliseconds;
    }

    public static void CollectItems(Player player) {
        String inv = Database.GetInventory(player);
        ItemStack[] is = ItemStackSerializer.deserialize(inv);
        player.getInventory().setContents(is);
        player.updateInventory();
    }

    public static void CollectArmor(Player player, Color c) {

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
        World world = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");
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
        Location loc = new Location(world, x, y, z, yaw, pitch);

        TeamManager.Spawnpoints.put(team, loc);
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
        World game = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
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
        World game = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".spawnpoints." +
                team + ".z");

        // SET AREA 2
        double area2x = x+2;
        double area2y = y+4;
        double area2z = z+2;

        Location area2 = new Location(game, area2x, area2y, area2z);
        return area2;
    }

    public static void LoadLobby() {
        if (!(lobby.isEmpty())) {
            lobby.clear();
        }
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(config.getString("lobby.world"));
        double x = config.getDouble("lobby.x");
        double y = config.getDouble("lobby.y");
        double z = config.getDouble("lobby.z");
        float yaw = (float) config.getDouble("lobby.yaw");
        float pitch = (float) config.getDouble("lobby.pitch");
        Location loc = new Location(lobby, x, y, z, yaw, pitch);
        MapManager.lobby.put("lobby", loc);
    }
}