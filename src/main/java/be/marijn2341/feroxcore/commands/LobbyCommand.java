package be.marijn2341.feroxcore.commands;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CommandExecutor {

    private final Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        // CHECK IF PLAYER IS IN SETUP MODE
        if (SetupCommand.SETUPMODE.containsKey(player.getUniqueId())) {
            player.sendMessage(Utils.color("&cYou are currently in setup mode, exit with /setup exit or /setup save."));
            return false;
        }

        if (main.getTeamManager().AllreadyInTeam(player)) {
            if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
                main.getDataManager().getTeamBlue().remove(player.getUniqueId());
            } else if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
                main.getDataManager().getTeamRed().remove(player.getUniqueId());
            } else main.getDataManager().getSpectators().remove(player.getUniqueId());
        }
        main.getMapManager().teleportToSpawn(player);
        return false;
    }
}
