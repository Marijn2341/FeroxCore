package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class BlockPlaceListener implements Listener {

    public static HashMap<String, Integer> blocks = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = (Player) e.getPlayer();
        if (MapManager.GameActive) {
            if (TeamManager.TeamRed.contains(player.getUniqueId()) || TeamManager.TeamBlue.contains(player.getUniqueId())) {
                if (MapManager.iswithin(e.getBlock().getLocation(), MapManager.areas.get("redarea1"), MapManager.areas.get("redarea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                } else if (MapManager.iswithin(e.getBlock().getLocation(), MapManager.areas.get("bluearea1"), MapManager.areas.get("bluearea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                }
                blocks.put("placed", blocks.get("placed") + 1);
            }
        }

        if (e.getBlock().getWorld().getName().equals(MapManager.lobby.get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
            }
        }
    }
}
