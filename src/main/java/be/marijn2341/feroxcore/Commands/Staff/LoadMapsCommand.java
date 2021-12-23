package be.marijn2341.feroxcore.Commands.Staff;

import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadMapsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("ferox.admin")) {
            MapManager.LoadMaps();
        } else {
            Utils.noPermission(player);
        }
        return false;
    }
}
