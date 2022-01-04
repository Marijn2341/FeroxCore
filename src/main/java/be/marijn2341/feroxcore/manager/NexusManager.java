package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class NexusManager {

    public static HashMap<String, Location> REDNEXUSESLOC = new HashMap<>();
    public static HashMap<String, Location> BLUENEXUSESLOC = new HashMap<>();
    public static HashMap<String, Location> REDNEXUSESLOCTOTAL = new HashMap<>();
    public static HashMap<String, Location> BLUENEXUSESLOCTOTAL = new HashMap<>();

    public static void countNexuses() {

        FileConfiguration config = Main.getInstance().getWorldsConfig();

        for (String team : config.getConfigurationSection("worlds." + MapManager.CURRENTMAP.get("current") +
                ".nexuses").getKeys(false)) {
            for (String nexus : config.getConfigurationSection("worlds." + MapManager.CURRENTMAP.get("current") +
                    ".nexuses." + team).getKeys(false)) {

                World game = Bukkit.getWorld(MapManager.CURRENTMAP.get("current") + "_activegame");
                double x = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".nexuses." + team + "." + nexus + ".x");
                double y = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".nexuses." + team + "." + nexus + ".y");
                double z = config.getDouble("worlds." + MapManager.CURRENTMAP.get("current") + ".nexuses." + team + "." + nexus + ".z");

                Location loc = new Location(game, x, y, z);

                if (team.equalsIgnoreCase("red")) {
                    REDNEXUSESLOC.put(nexus, loc);
                } else if (team.equalsIgnoreCase("blue")) {
                    BLUENEXUSESLOC.put(nexus, loc);
                }
            }
        }
        REDNEXUSESLOCTOTAL.putAll(REDNEXUSESLOC);
        BLUENEXUSESLOCTOTAL.putAll(BLUENEXUSESLOC);
    }

    public static boolean nexusAlive(String team, String nexus) {

        if (team.equalsIgnoreCase("red")) {
            if (REDNEXUSESLOC.containsKey(nexus)) {
                return true;
            } else {
                return false;
            }
        }

        if (team.equalsIgnoreCase("blue")) {
            if (BLUENEXUSESLOC.containsKey(nexus)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }
}
