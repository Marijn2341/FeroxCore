package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
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

import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MapManager {

    public static boolean GAMEACTIVE = false;
    public static int KILLS = 0;
    public static int DEATHS = 0;
    public static int ARROWSSHOT = 0;
    public static int ARROWSHIT = 0;
    private static Date GAMESTARTEDAT = null;
    private final Main main = Main.getInstance();


    public void loadMaps() {
        main.getDataManager().getMaps().clear();
        if (Main.getInstance().getWorldsConfig().get("worlds") == null) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "No maps to load.");
            return;
        }
        for (String map : Main.getInstance().getWorldsConfig().getConfigurationSection("worlds").getKeys(false)) {
            main.getDataManager().getMaps().add(map);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + main.getDataManager().getMaps().toString() + " Loaded");
    }

    public void teleportToSpawn(Player player) {

        player.setGameMode(GameMode.ADVENTURE);

        player.teleport(main.getDataManager().getLobby().get("lobby"));
        player.setHealth(20);
        player.setFoodLevel(20);

        NametagEdit.getApi().setPrefix(player, "&3&7");

        // FIX SCOREBOARD
        ScoreboardManager sbmanager = new ScoreboardManager();
        sbmanager.getLobbyScoreboard(player);

        // FIX PLAYER INVENTORY
        main.getTeamManager().ClearInventory(player);
        ItemStacks is = new ItemStacks();
        player.getInventory().setItem(4, is.compass());
        player.getInventory().setItem(8, is.playerSkull(player));
    }

    // CHECK IF THERE IS A CURRENT GAME ACTIVE
    public boolean isGameActive() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                return true;
            }
        }
        return false;
    }

    public void startGame() {
        if (!(isGameActive())) {
            main.getTeamManager().clearTeams();
            String newworld = getRandomWorld();
            Main.wm.cloneWorld(newworld, newworld + "_activegame");
            Main.wm.getMVWorld(newworld + "_activegame").setAlias(newworld + "_activegame");
            main.getDataManager().getCurrentMap().put("current", newworld);

            loadSpawnPoints("red");
            loadSpawnPoints("blue");

            main.getNexusManager().countNexuses();

            main.getDataManager().getKills().clear();
            main.getDataManager().getDeaths().clear();
            main.getDataManager().getArrowsHit().clear();
            main.getDataManager().getArrowsShot().clear();
            main.getDataManager().getBlocks().put("placed", 0);
            main.getDataManager().getBlocks().put("broken", 0);

            KILLS = 0;
            DEATHS = 0;
            ARROWSHIT = 0;
            ARROWSSHOT = 0;

            main.getDataManager().getAreas().put("redarea1", getSpawnArea1("red"));
            main.getDataManager().getAreas().put("redarea2", getSpawnArea2("red"));
            main.getDataManager().getAreas().put("bluearea1", getSpawnArea1("blue"));
            main.getDataManager().getAreas().put("bluearea2", getSpawnArea2("blue"));

            Bukkit.broadcastMessage(Utils.color("&9--- &9&lFerox&f&lMC &9---"));
            Bukkit.broadcastMessage(Utils.color("&9New map started: &f" + newworld + "&9."));
            Bukkit.broadcastMessage(Utils.color("&9----------------"));

            GAMEACTIVE = true;
            GAMESTARTEDAT = new Date();
        }
    }

    public void ForceDeleteGame() {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                main.getDataManager().getPreviousMap().add(w.getName());
                Main.wm.deleteWorld(w.getName());
                main.getDataManager().clearLists();
                GAMEACTIVE = false;
                GAMESTARTEDAT = null;
            }
        }
    }

    public void EndGame(String winner) {
        for (World w : Bukkit.getWorlds()) {
            if (w.getName().contains("_activegame")) {
                PlayerStatistics playerstats = new PlayerStatistics();
                GameStatistics gamestats = new GameStatistics();

                String matchtijd = getMatchTime();

                main.getDataManager().getWinners().forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&6&lVictory", "&7Your team won the game.", 5, 100, 5);

                    // ADD WIN TO DATABASE
                    main.getDb().setWins(uuid, 1);
                    playerstats.updateStats(uuid);

                    KILLS = KILLS + main.getDataManager().getKills().get(plr.getUniqueId());
                    DEATHS = DEATHS + main.getDataManager().getDeaths().get(plr.getUniqueId());
                    ARROWSSHOT = ARROWSSHOT + main.getDataManager().getArrowsShot().get(plr.getUniqueId());
                    ARROWSHIT = ARROWSHIT + main.getDataManager().getArrowsHit().get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    gamestats.sendGameStats(plr, matchtijd);
                });

                main.getDataManager().getLosers().forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    Utils.sendTitle(plr, "&c&lLost", "&7Your team lost the game.", 5, 100, 5);

                    // ADD LOSE TO DATABASE
                    main.getDb().setLoses(uuid, 1);
                    // ADD PLAYER STATS TO DATABASE
                    playerstats.updateStats(uuid);

                    KILLS = KILLS + main.getDataManager().getKills().get(plr.getUniqueId());
                    DEATHS = DEATHS + main.getDataManager().getDeaths().get(plr.getUniqueId());
                    ARROWSSHOT = ARROWSSHOT + main.getDataManager().getArrowsShot().get(plr.getUniqueId());
                    ARROWSHIT = ARROWSHIT + main.getDataManager().getArrowsHit().get(plr.getUniqueId());

                    // SEND STATS TO PLAYERS
                    gamestats.sendGameStats(plr, matchtijd);
                });

                if (!(winner.equalsIgnoreCase("null"))) {
                    main.getDataManager().getSpectators().forEach(uuid -> {
                        Player plr = Bukkit.getPlayer(uuid);
                        Utils.sendTitle(plr, "&6" + winner + " &7won the game!", "", 5, 80, 25);
                    });
                }

                // ADD GAME STATS TO DATABASE
                main.getDb().insertMapStats(main.getDataManager().getCurrentMap().get("current"), main.getDataManager().getBlocks().get("placed"), main.getDataManager().getBlocks().get("broken"),
                        getMatchTimeMi(), winner, KILLS, DEATHS, ARROWSSHOT, ARROWSHIT);

                GAMEACTIVE = false;

                main.getDataManager().clearLists();

                GAMESTARTEDAT = null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (UUID uuid : main.getDataManager().getPlayers()) {
                            Player plr = Bukkit.getPlayer(uuid);
                            teleportToSpawn(plr);
                            main.getDataManager().getPreviousMap().clear();
                            String wereld = w.getName().substring(0, w.getName().length() - 11);
                            main.getDataManager().getPreviousMap().add(wereld);
                            Main.wm.deleteWorld(w.getName());

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    startGame();
                                    main.getDataManager().getPlayers().clear();
                                }
                            }.runTaskLater(Main.getInstance(), 40);
                        }
                    }
                }.runTaskLater(Main.getInstance(), 300);
            }
        }
    }

    public String getRandomWorld() {
        if (main.getDataManager().getMaps().isEmpty()) {
            loadMaps();
        }
        if (!(main.getDataManager().getPreviousMap().isEmpty())) {
            main.getDataManager().getMaps().remove(main.getDataManager().getPreviousMap().get(0));
            if (main.getDataManager().getMaps().isEmpty()) {
                loadMaps();
                main.getDataManager().getMaps().remove(main.getDataManager().getPreviousMap().get(0));
            }
            main.getDataManager().getMaps().remove(main.getDataManager().getPreviousMap().get(0));
            String map = main.getDataManager().getMaps().get(new Random().nextInt(main.getDataManager().getMaps().size()));
            return map;
        }
        String map = main.getDataManager().getMaps().get(new Random().nextInt(main.getDataManager().getMaps().size()));
        main.getDataManager().getMaps().remove(map);
        return map;
    }

    public String getMatchTime() {
        long milliseconds = new Date().getTime() - GAMESTARTEDAT.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd = hours + "h, " + minutes + "min";
        return tijd;
    }

    public long getMatchTimeMi() {
        long milliseconds = new Date().getTime() - GAMESTARTEDAT.getTime();
        return milliseconds;
    }

    public void collectItems(Player player) {
        ItemStack[] is = main.getDataManager().getInventories().get(player.getUniqueId());
        player.getInventory().setContents(is);
        player.updateInventory();
    }

    public void collectArmor(Player player, Color c) {

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
    public void loadSpawnPoints(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World world = Bukkit.getWorld(main.getDataManager().getCurrentMap().get("current") + "_activegame");
        double x = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".z");
        float yaw = (float) config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".yaw");
        float pitch = (float) config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".pitch");
        Location loc = new Location(world, x, y, z, yaw, pitch);

        main.getDataManager().getSpawnPoints().put(team, loc);
    }

    // LETS TRUST LUC CODE ;)
    public Boolean iswithin(Location original, Location loc1, Location loc2) {
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
                return original.getZ() < zMax && original.getZ() > zMin;
            }
        }
        return false;
    }

    public Location getSpawnArea1(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World game = Bukkit.getWorld(main.getDataManager().getCurrentMap().get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".z");

        // SET AREA 1

        double area1x = x - 2;
        double area1y = y - 5;
        double area1z = z - 2;

        Location area1 = new Location(game, area1x, area1y, area1z);
        return area1;
    }

    public Location getSpawnArea2(String team) {
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World game = Bukkit.getWorld(main.getDataManager().getCurrentMap().get("current") + "_activegame");

        // GET COORDINATES
        double x = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".x");
        double y = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".y");
        double z = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".spawnpoints." +
                team + ".z");

        // SET AREA 2
        double area2x = x + 2;
        double area2y = y + 4;
        double area2z = z + 2;

        Location area2 = new Location(game, area2x, area2y, area2z);
        return area2;
    }

    public void loadLobby() {
        if (!(main.getDataManager().getLobby().isEmpty())) {
            main.getDataManager().getLobby().clear();
        }
        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(config.getString("lobby.world"));
        double x = config.getDouble("lobby.x");
        double y = config.getDouble("lobby.y");
        double z = config.getDouble("lobby.z");
        float yaw = (float) config.getDouble("lobby.yaw");
        float pitch = (float) config.getDouble("lobby.pitch");
        Location loc = new Location(lobby, x, y, z, yaw, pitch);
        main.getDataManager().getLobby().put("lobby", loc);
    }
}