package be.marijn2341.feroxcore.Commands;

import be.marijn2341.feroxcore.Utils.gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatisticsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        gui.CreateStaticsMenu(player);
        return false;
    }
}
