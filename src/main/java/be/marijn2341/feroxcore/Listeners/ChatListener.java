package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player player = (Player) e.getPlayer();
        if (TeamManager.TeamRed.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&cRED&7] &c" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (TeamManager.TeamBlue.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&9BLUE&7] &9" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }

        if (TeamManager.Spectator.contains(player.getUniqueId())) {
            Bukkit.broadcastMessage(Utils.color("&7[&8SPECTATOR&7] &8" + player.getName() + "&7: &f" + e.getMessage()));
            e.setCancelled(true);
            return;
        }
            Bukkit.broadcastMessage(Utils.color("&7" + player.getName() + ": &f" + e.getMessage()));
            e.setCancelled(true);
    }
}
