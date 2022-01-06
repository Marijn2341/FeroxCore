package be.marijn2341.feroxcore.commands.staff;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkipGameCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (player.hasPermission("ferox.admin")) {
            main.getDataManager().getLosers().addAll(main.getDataManager().getTeamBlue());
            main.getDataManager().getLosers().addAll(main.getDataManager().getTeamRed());
            main.getMapManager().EndGame("Null");
            return true;
        } else {
            Utils.noPermission(player);
        }
        return false;
    }
}
