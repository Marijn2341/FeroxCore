package be.marijn2341.feroxcore.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
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

    public static void sendTitle(Player player, String texttitle, String textsubtitle, int fadein, int show, int fadeout) {
        PlayerConnection connection = ((CraftPlayer) player.getPlayer()).getHandle().playerConnection;
        IChatBaseComponent Title = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Utils.color(texttitle) + "\"}");
        IChatBaseComponent subTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Utils.color(textsubtitle) + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, Title);
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(fadein, show, fadeout);

        connection.sendPacket(title);
        connection.sendPacket(subtitle);
        connection.sendPacket(length);
    }

    public static void KickOnReload(Player player) {
        player.kickPlayer("Reloading server, Rejoin when ready!");
    }
}
