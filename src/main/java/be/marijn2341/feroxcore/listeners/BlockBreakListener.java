package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.manager.NexusManager;
import be.marijn2341.feroxcore.manager.ScoreboardManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class BlockBreakListener implements Listener {


    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = (Player) e.getPlayer();

        if (e.getBlock().getWorld().getName().equals(MapManager.LOBBY.get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
                return;
            }
        }

        if (!(e.getBlock().getType() == Material.OBSIDIAN)) {
            BlockPlaceListener.BLOCKS.put("broken", BlockPlaceListener.BLOCKS.get("broken") + 1);
        }

        if (e.getBlock().getType() == Material.OBSIDIAN) {
            for (String key : NexusManager.BLUENEXUSESLOC.keySet()) {
                if (NexusManager.BLUENEXUSESLOC.get(key).equals(e.getBlock().getLocation())) {
                    if (TeamManager.TEAMBLUE.contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    NexusManager.BLUENEXUSESLOC.remove(key);
                    Bukkit.broadcastMessage(Utils.color("&c&l" + player.getName() + " &7has broken a &9&lblue &7nexus."));
                    Database.setBrokenNexuses(player.getUniqueId(), 1);
                    ScoreboardManager.updateScoreboardINGAME();

                    for (UUID uuid : TeamManager.PLAYERS) {
                        Player plr = Bukkit.getPlayer(uuid);
                        plr.playSound(plr.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 1.0f);
                    }

                    if (NexusManager.BLUENEXUSESLOC.size() == 0) {
                        TeamManager.WINNERS.addAll(TeamManager.TEAMRED);
                        TeamManager.LOSERS.addAll(TeamManager.TEAMBLUE);
                        MapManager.EndGame("Red");
                        break;
                    }
                    break;
                }
            }
            for (String key : NexusManager.REDNEXUSESLOC.keySet()) {
                if (NexusManager.REDNEXUSESLOC.get(key).equals(e.getBlock().getLocation())) {
                    if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    NexusManager.REDNEXUSESLOC.remove(key);
                    Bukkit.broadcastMessage(Utils.color("&9&l" + player.getName() + " &7has broken a &c&lred &7nexus."));
                    Database.setBrokenNexuses(player.getUniqueId(), 1);
                    ScoreboardManager.updateScoreboardINGAME();

                    for (UUID uuid : TeamManager.PLAYERS) {
                        Player plr = Bukkit.getPlayer(uuid);
                        plr.playSound(plr.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 1.0f);
                    }

                    if (NexusManager.REDNEXUSESLOC.size() == 0) {
                        TeamManager.WINNERS.addAll(TeamManager.TEAMBLUE);
                        TeamManager.LOSERS.addAll(TeamManager.TEAMRED);
                        MapManager.EndGame("Blue");
                        break;
                    }
                    break;
                }
            }
        }
    }
}
