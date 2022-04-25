package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class NexusManager {

    private final Main main = Main.getInstance();

    public void countNexuses() {

        FileConfiguration config = Main.getInstance().getWorldsConfig();
        for (String team : Main.getInstance().getWorldsConfig().getConfigurationSection("worlds." + main.getDataManager().getCurrentMap().get("current") + ".nexuses").getKeys(false)) {
            for (String nexus : config.getConfigurationSection("worlds." + main.getDataManager().getCurrentMap().get("current") +
                    ".nexuses." + team).getKeys(false)) {

                World game = Bukkit.getWorld(main.getDataManager().getCurrentMap().get("current") + "_activegame");
                double x = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".nexuses." + team + "." + nexus + ".x");
                double y = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".nexuses." + team + "." + nexus + ".y");
                double z = config.getDouble("worlds." + main.getDataManager().getCurrentMap().get("current") + ".nexuses." + team + "." + nexus + ".z");

                Location loc = new Location(game, x, y, z);

                if (team.equalsIgnoreCase("red")) {
                    main.getDataManager().getRedNexusesLoc().put(nexus, loc);
                } else if (team.equalsIgnoreCase("blue")) {
                    main.getDataManager().getBlueNexusesLoc().put(nexus, loc);
                }
            }
        }
        main.getDataManager().getRedNexusesLocTotal().putAll(main.getDataManager().getRedNexusesLoc());
        main.getDataManager().getBlueNexusesLocTotal().putAll(main.getDataManager().getBlueNexusesLoc());
    }

    public boolean nexusAlive(String team, String nexus) {

        if (team.equalsIgnoreCase("red")) {
            return main.getDataManager().getRedNexusesLoc().containsKey(nexus);
        }

        if (team.equalsIgnoreCase("blue")) {
            return main.getDataManager().getBlueNexusesLoc().containsKey(nexus);
        }

        return false;
    }
}
