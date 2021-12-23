package be.marijn2341.feroxcore.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {
    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void noPermission(Player player) {
        player.sendMessage(Utils.color("&cYou don't have the permission to perform this command."));
    }

    public static String getAlphaNumericString(int n) {

        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            int index = (int)(AlphaNumericString.length() * Math.random());

            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
