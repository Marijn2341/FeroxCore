package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.NexusManager;
import be.marijn2341.feroxcore.Manager.ScoreboardManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;
import java.util.UUID;

public class BlockBreakListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = (Player) e.getPlayer();

        if (e.getBlock().getWorld().getName().equals(MapManager.lobby.get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
                return;
            }
        }

        if (!(e.getBlock().getType() == Material.OBSIDIAN)) {
            BlockPlaceListener.blocks.put("broken", BlockPlaceListener.blocks.get("broken") + 1);
        }

        if (e.getBlock().getType() == Material.OBSIDIAN) {
            for (String key : NexusManager.BlueNexusesLoc.keySet()) {
                if (NexusManager.BlueNexusesLoc.get(key).equals(e.getBlock().getLocation())) {
                    if (TeamManager.TeamBlue.contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    NexusManager.BlueNexusesLoc.remove(key);
                    Bukkit.broadcastMessage(Utils.color("&c&l" + player.getName() + " &7has broken a &9&lblue &7nexus. "));
                    Database.SetBrokenNexuses(player.getUniqueId(), 1);
                    ScoreboardManager.UpdateScoreboardINGAME();

                    if (NexusManager.BlueNexusesLoc.size() == 0) {
                        TeamManager.Winners.addAll(TeamManager.TeamRed);
                        TeamManager.Losers.addAll(TeamManager.TeamBlue);
                        MapManager.EndGame("Red");
                        break;
                    }
                    break;
                }
            }
            for (String key : NexusManager.RedNexusesLoc.keySet()) {
                if (NexusManager.RedNexusesLoc.get(key).equals(e.getBlock().getLocation())) {
                    if (TeamManager.TeamRed.contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    NexusManager.RedNexusesLoc.remove(key);
                    Bukkit.broadcastMessage(Utils.color("&9&l" + player.getName() + " &7has broken a &c&lred &7nexus. "));
                    Database.SetBrokenNexuses(player.getUniqueId(), 1);
                    ScoreboardManager.UpdateScoreboardINGAME();

                    if (NexusManager.RedNexusesLoc.size() == 0) {
                        TeamManager.Winners.addAll(TeamManager.TeamBlue);
                        TeamManager.Losers.addAll(TeamManager.TeamRed);
                        MapManager.EndGame("Blue");
                        break;
                    }
                    break;
                }
            }
        }
    }
}
