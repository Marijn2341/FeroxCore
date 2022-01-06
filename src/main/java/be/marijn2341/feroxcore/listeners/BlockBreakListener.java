package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.manager.*;
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

    private Main main = Main.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = (Player) e.getPlayer();
        ScoreboardManager sbmanager = new ScoreboardManager();

        if (e.getBlock().getWorld().getName().equals(main.getDataManager().getLobby().get("lobby").getWorld().getName())) {
            if (!(player.hasPermission("ferox.lobby.build"))) {
                e.setCancelled(true);
                return;
            }
        }

        if (!(e.getBlock().getType() == Material.OBSIDIAN)) {
            main.getDataManager().getBlocks().put("broken", main.getDataManager().getBlocks().get("broken") + 1);
        }

        if (e.getBlock().getType() == Material.OBSIDIAN) {
            for (String key : main.getDataManager().getBlueNexusesLoc().keySet()) {
                if (main.getDataManager().getBlueNexusesLoc().get(key).equals(e.getBlock().getLocation())) {
                    if (main.getDataManager().getTeamBlue().contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    main.getDataManager().getBlueNexusesLoc().remove(key);
                    Bukkit.broadcastMessage(Utils.color("&c&l" + player.getName() + " &7has broken a &9&lblue &7nexus."));
                    main.getDb().setBrokenNexuses(player.getUniqueId(), 1);
                    sbmanager.updateScoreboardINGAME();

                    for (UUID uuid : main.getDataManager().getPlayers()) {
                        Player plr = Bukkit.getPlayer(uuid);
                        plr.playSound(plr.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 1.0f);
                    }

                    if (main.getDataManager().getBlueNexusesLoc().size() == 0) {
                        main.getDataManager().getWinners().addAll(main.getDataManager().getTeamRed());
                        main.getDataManager().getLosers().addAll(main.getDataManager().getTeamBlue());
                        main.getMapManager().EndGame("Red");
                        break;
                    }
                    break;
                }
            }
            for (String key : main.getDataManager().getRedNexusesLoc().keySet()) {
                if (main.getDataManager().getRedNexusesLoc().get(key).equals(e.getBlock().getLocation())) {
                    if (main.getDataManager().getTeamRed().contains(player.getUniqueId())) {
                        player.sendMessage(Utils.color("&cYou can't break your own nexus."));
                        e.setCancelled(true);
                        break;
                    }

                    e.setCancelled(true);
                    e.getBlock().setType(Material.AIR);
                    main.getDataManager().getRedNexusesLoc().remove(key);
                    Bukkit.broadcastMessage(Utils.color("&9&l" + player.getName() + " &7has broken a &c&lred &7nexus."));
                    main.getDb().setBrokenNexuses(player.getUniqueId(), 1);
                    sbmanager.updateScoreboardINGAME();

                    for (UUID uuid : main.getDataManager().getPlayers()) {
                        Player plr = Bukkit.getPlayer(uuid);
                        plr.playSound(plr.getLocation(), Sound.AMBIENCE_THUNDER, 2.0f, 1.0f);
                    }

                    if (main.getDataManager().getRedNexusesLoc().size() == 0) {
                        main.getDataManager().getWinners().addAll(main.getDataManager().getTeamBlue());
                        main.getDataManager().getLosers().addAll(main.getDataManager().getTeamRed());
                        main.getMapManager().EndGame("Blue");
                        break;
                    }
                    break;
                }
            }
        }
    }
}
