package be.marijn2341.feroxcore.Manager;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

public class NexusManager {

    public static HashMap<String, Location> RedNexusesLoc = new HashMap<>();
    public static HashMap<String, Location> BlueNexusesLoc = new HashMap<>();
    public static HashMap<String, Location> RedNexusesLocTOTAL = new HashMap<>();
    public static HashMap<String, Location> BlueNexusesLocTOTAL = new HashMap<>();

    public static void CountNexuses() {

        FileConfiguration config = Main.getInstance().getWorldsConfig();

        for (String team : config.getConfigurationSection("worlds." + MapManager.currentmap.get("current") +
                ".nexuses").getKeys(false)) {
            for (String nexus : config.getConfigurationSection("worlds." + MapManager.currentmap.get("current") +
                    ".nexuses." + team).getKeys(false)) {

                World game = Bukkit.getWorld(MapManager.currentmap.get("current") + "_activegame");
                double x = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".nexuses." + team + "." + nexus + ".x");
                double y = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".nexuses." + team + "." + nexus + ".y");
                double z = config.getDouble("worlds." + MapManager.currentmap.get("current") + ".nexuses." + team + "." + nexus + ".z");

                Location loc = new Location(game, x, y, z);

                if (team.equalsIgnoreCase("red")) {
                    RedNexusesLoc.put(nexus, loc);
                } else if (team.equalsIgnoreCase("blue")) {
                    BlueNexusesLoc.put(nexus, loc);
                }
            }
        }
        RedNexusesLocTOTAL.putAll(RedNexusesLoc);
        BlueNexusesLocTOTAL.putAll(BlueNexusesLoc);
    }

    public static boolean NexusAlive(String team, String nexus) {

        if (team.equalsIgnoreCase("red")) {
            if (RedNexusesLoc.containsKey(nexus)) {
                return true;
            } else {
                return false;
            }
        }

        if (team.equalsIgnoreCase("blue")) {
            if (BlueNexusesLoc.containsKey(nexus)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
