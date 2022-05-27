package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.manager.LevelManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private final Main main = Main.getInstance();
    LevelManager levelmanager = new LevelManager();

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(Utils.color("&8[&4&l-&8] &7" + player.getName()));
        if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
            main.getDataManager().getTeamBlue().remove(player.getUniqueId());
        } else if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
            main.getDataManager().getTeamRed().remove(player.getUniqueId());
        } else main.getDataManager().getSpectators().remove(player.getUniqueId());

        main.getDataManager().getPlayers().remove(player.getUniqueId());

        if (SetupCommand.SETUPMODE.containsKey(player.getUniqueId())) {
            if (Main.getInstance().getWorldsConfig().contains("worlds." + SetupCommand.SETUPMODE.get(player.getUniqueId()).getName())) {
                Main.getInstance().getWorldsConfig().set("worlds." + SetupCommand.SETUPMODE.get(player.getUniqueId()).getName(), null);
                SetupCommand.SETUPMODE.remove(player.getUniqueId());
                return;
            } else {
                SetupCommand.SETUPMODE.remove(player.getUniqueId());
                return;
            }
        }

        if (Bukkit.getOnlinePlayers().size() == 1) {
            if (main.getMapManager().isGameActive()) {
                main.getMapManager().ForceDeleteGame();
            }
        }

        main.getDb().setPlaytimeDB(player.getUniqueId());

        main.getDb().setLevel(player.getUniqueId(), levelmanager.getPlayerLevel(player.getUniqueId()));
        main.getDb().setXP(player.getUniqueId(), levelmanager.getPlayerXP(player.getUniqueId()));
    }
}
