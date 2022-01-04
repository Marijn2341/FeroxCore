package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;

public class BlockPlaceListener implements Listener {

    public static HashMap<String, Integer> BLOCKS = new HashMap<>();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = (Player) e.getPlayer();
        if (MapManager.GAMEACTIVE) {
            if (TeamManager.TEAMRED.contains(player.getUniqueId()) || TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
                if (MapManager.iswithin(e.getBlock().getLocation(), MapManager.AREAS.get("redarea1"), MapManager.AREAS.get("redarea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                } else if (MapManager.iswithin(e.getBlock().getLocation(), MapManager.AREAS.get("bluearea1"), MapManager.AREAS.get("bluearea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                }
                BLOCKS.put("placed", BLOCKS.get("placed") + 1);
            }
        }

        if (e.getBlock().getWorld().getName().equals(MapManager.LOBBY.get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
            }
        }
    }
}
