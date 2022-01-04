package be.marijn2341.feroxcore.commands;

import be.marijn2341.feroxcore.database.RegistrationDatabase;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VerifyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (RegistrationDatabase.allreadyRegistered(player.getUniqueId())) {
                player.sendMessage(Utils.color("&cYou are already verified!"));
                return false;
            }
            if (RegistrationDatabase.playerExists(player.getUniqueId())) {
                player.sendMessage(Utils.color("&cYou already have a verification code, Use the /link confirm <code> command on the discord."));
                player.sendMessage(Utils.color("&cYour code: &7" + RegistrationDatabase.getRegistrationCode(player.getUniqueId())));
                return true;
            } else {
                String code = Utils.getAlphaNumericString(10);
                if (RegistrationDatabase.checkIfCodeExists(code)) {
                    code = Utils.getAlphaNumericString(10);
                }
                RegistrationDatabase.insertCode(player.getUniqueId(), code);
                player.sendMessage(Utils.color("&a------"));
                player.sendMessage(Utils.color("&aYou can confirm your verification on discord with the command:"));
                player.sendMessage(Utils.color("&7/link confirm " + code));
                player.sendMessage(Utils.color("&a------"));
                return true;
            }
        }
        return false;
    }
}
