package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();
        if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&cRED&7] &c" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&9BLUE&7] &9" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (main.getDataManager().getSpectators().contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&8SPECTATOR&7] &8" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }
        Bukkit.broadcastMessage(Utils.color("&7" + player.getName() + ": &f" + e.getMessage()));
        e.setCancelled(true);
    }
}
