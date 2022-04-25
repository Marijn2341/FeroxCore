package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (MapManager.GAMEACTIVE) {
            if (main.getDataManager().getTeamRed().contains(player.getUniqueId()) || main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
                if (main.getMapManager().iswithin(e.getBlock().getLocation(), main.getDataManager().getAreas().get("redarea1"), main.getDataManager().getAreas().get("redarea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                } else if (main.getMapManager().iswithin(e.getBlock().getLocation(), main.getDataManager().getAreas().get("bluearea1"), main.getDataManager().getAreas().get("bluearea2"))) {
                    e.setCancelled(true);
                    player.sendMessage(Utils.color("&cYou can't build in a spawn."));
                    return;
                }
                main.getDataManager().getBlocks().put("placed", main.getDataManager().getBlocks().get("placed") + 1);
            }
        }

        if (e.getBlock().getWorld().getName().equals(main.getDataManager().getLobby().get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
            }
        }
    }
}
