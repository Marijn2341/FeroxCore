package be.marijn2341.feroxcore.commands;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Gui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatisticsCommand implements CommandExecutor {

    private Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        main.getGui().createStaticsMenu(player);
        return false;
    }
}
