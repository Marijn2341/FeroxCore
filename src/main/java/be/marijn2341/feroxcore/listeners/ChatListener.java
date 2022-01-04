package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = (Player) e.getPlayer();
        if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&cRED&7] &c" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&9BLUE&7] &9" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (TeamManager.SPECTATORS.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&8SPECTATOR&7] &8" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }
            Bukkit.broadcastMessage(Utils.color("&7" + player.getName() + ": &f" + e.getMessage()));
            e.setCancelled(true);
    }
}
