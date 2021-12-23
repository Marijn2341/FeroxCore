package be.marijn2341.feroxcore.Manager;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Listeners.BlockPlaceListener;
import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Main;
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
    private static Date GameStarted = null;
    public static boolean GameActive = false;
    public static HashMap<String, String> currentmap = new HashMap<>();
    public static HashMap<String, Location> areas = new HashMap<>();


    public static void LoadMaps() {
        for (String map : Main.getInstance().getWorldsConfig().getConfigurationSection("worlds").getKeys(false)) {
            Maps.add(map);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + Maps.toString() + " Loaded");
    }

    public static void TeleportToSpawn(Player player) {

        player.setGameMode(GameMode.SURVIVAL);

        FileConfiguration config = Main.getInstance().getWorldsConfig();
        World lobby = Bukkit.getWorld(config.getString("lobby.world"));
        double x = config.getDouble("lobby.x");
        double y = config.getDouble("lobby.y");
        double z = config.getDouble("lobby.z");
        float yaw = (float) config.getDouble("lobby.yaw");
        float pitch = (float) config.getDouble("lobby.pitch");
        Location loc = new Location(lobby, x, y, z, yaw, pitch);
        player.teleport(loc);
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

            BlockPlaceListener.blocks.put("placed", 0);
            BlockPlaceListener.blocks.put("broken", 0);
            NexusManager.CountNexuses();

            DeathListener.Kills.clear();
            DeathListener.Deaths.clear();
            
            areas.put("redarea1", getSpawnArea1("red"));
            areas.put("redarea2", getSpawnArea2("red"));
            areas.put("bluearea1", getSpawnArea1("blue"));
            areas.put("bluearea2", getSpawnArea2("blue"));

            Bukkit.broadcastMessage(Utils.color("&9----"));
            Bukkit.broadcastMessage(Utils.color("&9New map started: &f" + newworld + "&9."));
            Bukkit.broadcastMessage(Utils.color("&9----"));

            GameActive = true;
            GameStarted = new Date();
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
                    plr.sendTitle(Utils.color("&6&lVictory"), Utils.color("&7Your team won the game."));

                    // ADD WIN TO DATABASE
                    Database.SetWins(plr.getUniqueId(), 1);

                    // SEND STATS TO PLAYERS
                    plr.sendMessage(Utils.color("&8-----"));
                    plr.sendMessage(Utils.color("&7Total blocks placed (global): &f" + BlockPlaceListener.blocks.get("placed")));
                    plr.sendMessage(Utils.color("&7Total blocks broken (global): &f" + BlockPlaceListener.blocks.get("broken")));
                    plr.sendMessage(Utils.color("&7Matchtime: &f" + matchtijd));
                    plr.sendMessage(Utils.color("&7Your kills: &f" + DeathListener.Kills.get(plr.getUniqueId())));
                    plr.sendMessage(Utils.color("&7Your deaths: &f" + DeathListener.Deaths.get(plr.getUniqueId())));
                    plr.sendMessage(Utils.color("&8-----"));
                });

                TeamManager.Losers.forEach(uuid -> {
                    Player plr = Bukkit.getPlayer(uuid);
                    plr.setGameMode(GameMode.SPECTATOR);
                    plr.sendTitle(Utils.color("&4&lLost"), Utils.color("&7Your team has lost the game."));

                    // ADD LOSE TO DATABASE
                    Database.SetLoses(plr.getUniqueId(), 1);

                    // SEND STATS TO PLAYERS
                    plr.sendMessage(Utils.color("&8-----"));
                    plr.sendMessage(Utils.color("&7Total blocks placed (global): &f" + BlockPlaceListener.blocks.get("placed")));
                    plr.sendMessage(Utils.color("&7Total blocks broken (global): &f" + BlockPlaceListener.blocks.get("broken")));
                    plr.sendMessage(Utils.color("&7Matchtime: &f" + matchtijd));
                    plr.sendMessage(Utils.color("&7Your kills: &f" + DeathListener.Kills.get(plr.getUniqueId())));
                    plr.sendMessage(Utils.color("&7Your deaths: &f " + DeathListener.Deaths.get(plr.getUniqueId())));
                    plr.sendMessage(Utils.color("&8-----"));
                });

                for (UUID uuid : DeathListener.Kills.keySet()) {
                    int kills = DeathListener.Kills.get(uuid);
                    Database.UpdateKillsDB(uuid, kills);
                }

                for (UUID uuid : DeathListener.Deaths.keySet()) {
                    int deaths = DeathListener.Deaths.get(uuid);
                    Database.UpdateDeathsDB(uuid, deaths);
                }

                if (!(winner.equalsIgnoreCase("null"))) {
                    TeamManager.Spectator.forEach(uuid -> {
                        Player plr = Bukkit.getPlayer(uuid);
                        plr.sendTitle(Utils.color("&6" + winner + " &7won the game!"), "");
                    });
                }

                GameActive = false;
                TeamManager.ToSpawn.addAll(TeamManager.Losers);
                TeamManager.ToSpawn.addAll(TeamManager.Winners);
                TeamManager.ToSpawn.addAll(TeamManager.Spectator);
                TeamManager.Losers.clear();
                TeamManager.Winners.clear();
                TeamManager.clearTeams();

                NexusManager.RedNexusesLoc.clear();
                NexusManager.BlueNexusesLoc.clear();
                NexusManager.RedNexusesLocTOTAL.clear();
                NexusManager.BlueNexusesLocTOTAL.clear();

                GameStarted = null;

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (UUID uuid : TeamManager.ToSpawn) {
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
                                    TeamManager.ToSpawn.clear();
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
            String map = Maps.get(new Random().nextInt(Maps.size()));
            Maps.add(PreviousMap.get(0));
            return map;
        }
        String map = Maps.get(new Random().nextInt(Maps.size()));
        Maps.remove(map);
        return map;
    }

    public static String GetMatchTime() {
        long milliseconds = new Date().getTime() - GameStarted.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd =  hours + "h, " + minutes + "min";
        return tijd;
    }

    public static void CollectItems(Player player) {
        Inventory inv = player.getInventory();
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemStack axe = new ItemStack(Material.STONE_AXE);
        ItemStack arrows = new ItemStack(Material.ARROW, 32);
        ItemStack logs = new ItemStack(Material.LOG, 64);
        ItemStack glass = new ItemStack(Material.GLASS, 64);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE);
        ItemStack steak = new ItemStack(Material.COOKED_BEEF, 64);

        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);

        inv.setItem(0, sword);
        inv.setItem(1, bow);
        inv.setItem(2, pickaxe);
        inv.setItem(3, axe);
        inv.setItem(4, arrows);
        inv.setItem(5, logs);
        inv.setItem(6, glass);
        inv.setItem(7, gapple);
        inv.setItem(8, steak);

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

    public static void KickOnReload(Player player) {
            player.kickPlayer("Reloading server, Rejoin when ready!");
    }
}
