package be.marijn2341.feroxcore.commands.staff;

import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartGameCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ferox.admin")) {
                MapManager.startGame();
                return true;
            } else {
                Utils.noPermission(player);
            }
        }
        return false;
    }
}
