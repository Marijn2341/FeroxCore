package be.marijn2341.feroxcore.Commands.Staff;

import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Manager.NexusManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class test implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Bukkit.broadcastMessage(DeathListener.Deaths.toString() + " | " + DeathListener.Kills.toString());
        return true;
    }
}
