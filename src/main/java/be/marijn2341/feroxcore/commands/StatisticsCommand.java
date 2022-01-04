package be.marijn2341.feroxcore.commands;

import be.marijn2341.feroxcore.utils.Gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatisticsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Gui.createStaticsMenu(player);
        return false;
    }
}
