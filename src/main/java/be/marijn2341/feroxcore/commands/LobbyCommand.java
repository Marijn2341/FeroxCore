package be.marijn2341.feroxcore.commands;

import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        // CHECK IF PLAYER IS IN SETUP MODE
        if (SetupCommand.SETUPMODE.containsKey(player.getUniqueId())) {
            player.sendMessage(Utils.color("&cYou are currently in setup mode, exit with /setup exit or /setup save."));
            return false;
        }

        if (TeamManager.AllreadyInTeam(player)) {
            if (TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
                TeamManager.TEAMBLUE.remove(player.getUniqueId());
            } else if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
                TeamManager.TEAMRED.remove(player.getUniqueId());
            } else if (TeamManager.SPECTATORS.contains(player.getUniqueId())) {
                TeamManager.SPECTATORS.remove(player.getUniqueId());
            }
        }
        MapManager.teleportToSpawn(player);
        return false;
    }
}
