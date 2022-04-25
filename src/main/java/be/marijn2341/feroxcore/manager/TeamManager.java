package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TeamManager {

    private final Main main = Main.getInstance();

    public void addToTeam(String team, Player player) {
        if (team.equalsIgnoreCase("red")) {
            main.getDataManager().getTeamRed().add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("blue")) {
            main.getDataManager().getTeamBlue().add(player.getUniqueId());
        } else if (team.equalsIgnoreCase("spectator")) {
            main.getDataManager().getSpectators().add(player.getUniqueId());
        }

        main.getDataManager().getPlayers().add(player.getUniqueId());
        main.getPlayerManager().setInventory(player);
        ScoreboardManager sbmanager = new ScoreboardManager();
        sbmanager.getGameScoreboard(player);
    }

    public void clearTeams() {
        main.getDataManager().getTeamRed().clear();
        main.getDataManager().getTeamBlue().clear();
        main.getDataManager().getSpectators().clear();
    }

    public void addToRandomTeam(Player player) {
        if (main.getDataManager().getTeamBlue().size() > main.getDataManager().getTeamRed().size()) {
            addToTeam("red", player);
            processToTeam("red", player);
        } else if (main.getDataManager().getTeamRed().size() > main.getDataManager().getTeamBlue().size()) {
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

    public int countOnlinePlayers() {
        int online = Bukkit.getOnlinePlayers().size();
        return online;
    }

    public boolean checkTeamsUnbalanced(String team) {
        if (team.equalsIgnoreCase("blue")) {
            return main.getDataManager().getTeamBlue().size() > main.getDataManager().getTeamRed().size();
        } else if (team.equalsIgnoreCase("red")) {
            return main.getDataManager().getTeamRed().size() > main.getDataManager().getTeamBlue().size();
        }
        return false;
    }

    public void processToTeam(String team, Player player) {
        // TELEPORT TO TEAM SPAWN
        player.teleport(main.getDataManager().getSpawnPoints().get(team));

        if (!(main.getDataManager().getDeaths().containsKey(player.getUniqueId()))) {
            main.getDataManager().getDeaths().put(player.getUniqueId(), 0);
        }
        if (!(main.getDataManager().getKills().containsKey(player.getUniqueId()))) {
            main.getDataManager().getKills().put(player.getUniqueId(), 0);
        }
        if (!(main.getDataManager().getArrowsHit().containsKey(player.getUniqueId()))) {
            main.getDataManager().getArrowsHit().put(player.getUniqueId(), 0);
        }
        if (!(main.getDataManager().getArrowsShot().containsKey(player.getUniqueId()))) {
            main.getDataManager().getArrowsShot().put(player.getUniqueId(), 0);
        }

        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlying(false);
        player.setFoodLevel(20);
        player.setHealth(20);
        main.getMapManager().collectItems(player);
        if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
            main.getMapManager().collectArmor(player, Color.BLUE);
            NametagEdit.getApi().setPrefix(player, "&2&9");
        } else if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
            main.getMapManager().collectArmor(player, Color.RED);
            NametagEdit.getApi().setPrefix(player, "&c");
        }
    }

    public boolean AllreadyInTeam(Player player) {
        return main.getDataManager().getTeamRed().contains(player.getUniqueId()) || main.getDataManager().getTeamBlue().contains(player.getUniqueId()) || main.getDataManager().getSpectators().contains(player.getUniqueId());
    }

    public void ClearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.updateInventory();
    }

    public String GetTeamColor(Player player) {
        if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
            return "c";
        } else if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
            return "9";
        }
        return "0";
    }

    public void SetAsSpectator(Player player) {
        Location red = main.getDataManager().getSpawnPoints().get("red");
        Location blue = main.getDataManager().getSpawnPoints().get("blue");

        List<Location> spawn = Arrays.asList(red, blue);
        Random rand = new Random();

        player.teleport(spawn.get(rand.nextInt(spawn.size())));
        main.getDataManager().getSpectators().add(player.getUniqueId());
        player.sendTitle(Utils.color("&9&lSpectator"), Utils.color("&7You can get back with /lobby."));
        player.setGameMode(GameMode.SPECTATOR);
    }
}
