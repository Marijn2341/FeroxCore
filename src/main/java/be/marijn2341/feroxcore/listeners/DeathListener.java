package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.MapManager;
import be.marijn2341.feroxcore.manager.TeamManager;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class DeathListener implements Listener {

    public static HashMap<UUID, Integer> DEATHS = new HashMap<>();
    public static HashMap<UUID, Integer> KILLS = new HashMap<>();
    public static HashMap<Player, BukkitTask> QUEUE = new HashMap<>();
    public static HashMap<Player, Player> LASTDAMAGER = new HashMap<>();
    public static HashMap<UUID, Integer> ARROWSHIT = new HashMap<>();

    @EventHandler
    public void ondeath(PlayerDeathEvent e) {

        if (e.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player deathplayer = (Player) e.getEntity().getPlayer();

        // ------------------------------
        // REMOVE ITEMS WHEN PLAYER DIED (INGAME)
        // ------------------------------

        List<ItemStack> list = e.getDrops();
        Iterator<ItemStack> i = list.iterator();
        if (TeamManager.AllreadyInTeam(deathplayer)) {
            while (i.hasNext()) {
                ItemStack drop = i.next();
                if (drop.getType() == Material.LEATHER_BOOTS ||
                        drop.getType() == Material.STONE_SWORD ||
                        drop.getType() == Material.BOW ||
                        drop.getType() == Material.DIAMOND_PICKAXE ||
                        drop.getType() == Material.STONE_AXE ||
                        drop.getType() == Material.LEATHER_LEGGINGS ||
                        drop.getType() == Material.CHAINMAIL_CHESTPLATE ||
                        drop.getType() == Material.LEATHER_HELMET ||
                        drop.getType() == Material.GLASS ||
                        drop.getType() == Material.WOOD ||
                        drop.getType() == Material.LOG) {
                    i.remove();
                }
            }
        }

        // ------------------------------
        // REMOVE ITEMS WHEN PLAYER DIED (SPAWN)
        // ------------------------------

        if (!(TeamManager.AllreadyInTeam(deathplayer))) {
            while (i.hasNext()) {
                ItemStack drop = i.next();
                if (drop.getType() == Material.COMPASS ||
                        drop.getType() == Material.SKULL_ITEM ||
                        drop.getType() == Material.SKULL) {
                    i.remove();
                }
            }
            e.setDeathMessage("");
            return;
        }

        DEATHS.put(deathplayer.getUniqueId(), DEATHS.get(deathplayer.getUniqueId()) + 1);


        // ------------------------------
        // CHECK IF PLAYER DIED BY A CACTUS
        // ------------------------------

        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent cause = (EntityDamageByBlockEvent) e.getEntity().getLastDamageCause();
            if (cause.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                if (LASTDAMAGER.containsKey(deathplayer)) {
                    Player killer = LASTDAMAGER.get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked into a cactus by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD KILL TO KILLER
                    KILLS.put(killer.getUniqueId(), KILLS.get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    LASTDAMAGER.remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got rekt by a cactus!"));
                    return;
                }
            }
        }

        // ------------------------------
        // CHECK IF PLAYER DIED BY FALLING
        // ------------------------------

            EntityDamageEvent cause2 = e.getEntity().getLastDamageCause();
            if (cause2.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (LASTDAMAGER.containsKey(deathplayer)) {
                    Player killer = LASTDAMAGER.get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked to the ground by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD KILL TO KILLER
                    KILLS.put(killer.getUniqueId(), KILLS.get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    LASTDAMAGER.remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7wanted to kiss the ground!"));
                    return;
                }
            }

            // ------------------------------
            // CHECK IF PLAYER DIED IN VOID
            // ------------------------------


            if (e.getEntity().getLastDamageCause() instanceof EntityDamageByBlockEvent) {
                EntityDamageByBlockEvent cause = (EntityDamageByBlockEvent) e.getEntity().getLastDamageCause();
                if (cause.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    if (LASTDAMAGER.containsKey(deathplayer)) {
                        Player killer = LASTDAMAGER.get(deathplayer);
                        e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                                deathplayer.getName() + " &7got knocked into the void by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                                "&7."));
                        // ADD KILL TO KILLER
                        KILLS.put(killer.getUniqueId(), KILLS.get(killer.getUniqueId()) + 1);
                        // REMOVE PLAYER FROM HASHMAP
                        LASTDAMAGER.remove(deathplayer);
                        return;
                    } else {
                        e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) + deathplayer.getName() + " &7died in the void."));
                        return;
                    }
                }
            }

            // ------------------------------
            // KILLER KILLS PLAYER (BOW AND ARROW)
            // ------------------------------

            if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent cause = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
                if (cause.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
                    if (cause.getDamager() instanceof Arrow) {
                        Arrow arrow = (Arrow) cause.getDamager();
                        if (arrow.getShooter() instanceof Player) {
                            Player shooter = (Player) arrow.getShooter();
                            e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) + deathplayer.getName() + " &7got shot by &" +
                                    TeamManager.GetTeamColor(shooter) + shooter.getName() + "&7."));
                            KILLS.put(shooter.getUniqueId(), KILLS.get(shooter.getUniqueId()) + 1);
                            return;
                        }
                    }
                }
            }

            // ------------------------------
            // KILLER KILLS PLAYER (NORMAL)
            // ------------------------------

            if (deathplayer.getKiller() != null) {
                Player killer = deathplayer.getKiller();
                if (TeamManager.TEAMBLUE.contains(killer.getUniqueId()) || TeamManager.TEAMRED.contains(killer.getUniqueId())) {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) + deathplayer.getName() + " &7got killed by &" + TeamManager.GetTeamColor(killer) + killer.getName() + "&7."));
                    // ADD KILL TO KILLER
                    KILLS.put(killer.getUniqueId(), KILLS.get(killer.getUniqueId()) + 1);

                    if (LASTDAMAGER.containsKey(deathplayer)) {
                        LASTDAMAGER.remove(deathplayer);
                    }
                    return;
                }
            }
            //e.setDeathMessage(e.getDeathMessage().replace(e.getEntity().getPlayer().getDisplayName(), Utils.color("&" + TeamManager.GetTeamColor(e.getEntity()) + e.getEntity().getPlayer().getDisplayName() + "&7")));
    }

    // ------------------------------
    // ADD HITTER TO HASHMAP FROM HITTED PLAYER
    // ------------------------------

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && (e.getDamager() instanceof Player || e.getDamager() instanceof Arrow)) {
            Player hitted = (Player) e.getEntity();

            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                Player hitter = (Player) arrow.getShooter();
                if (TeamManager.AllreadyInTeam(hitter) && TeamManager.AllreadyInTeam(hitted)) {
                    if (TeamManager.TEAMRED.contains(hitter.getUniqueId()) == TeamManager.TEAMRED.contains(hitted.getUniqueId())) {
                        return;
                    }
                    if (TeamManager.TEAMBLUE.contains(hitter.getUniqueId()) == TeamManager.TEAMBLUE.contains(hitted.getUniqueId())) {
                        return;
                    }
                    ARROWSHIT.put(hitter.getUniqueId(), ARROWSHIT.get(hitter.getUniqueId()) + 1);
                    LASTDAMAGER.put(hitted, hitter);
                }
            } else {
                Player hitter = (Player) e.getDamager();
                if (TeamManager.AllreadyInTeam(hitter) && TeamManager.AllreadyInTeam(hitted)) {
                    if (TeamManager.TEAMRED.contains(hitter.getUniqueId()) == TeamManager.TEAMRED.contains(hitted.getUniqueId())) {
                        return;
                    }
                    if (TeamManager.TEAMBLUE.contains(hitter.getUniqueId()) == TeamManager.TEAMBLUE.contains(hitted.getUniqueId())) {
                        return;
                    }
                    LASTDAMAGER.put(hitted, hitter);
                }
            }


            if(QUEUE.get(hitted) != null){
                QUEUE.get(hitted).cancel();
            }
            BukkitTask br = new BukkitRunnable() {
                public void run() {LASTDAMAGER.remove(hitted);
                }
            }.runTaskLater(Main.getInstance(), 200);
                QUEUE.put(hitted, br);
            }
    }

    @EventHandler
    public void onVoidDamage(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player damaged = (Player) e.getEntity();
            if (damaged.getWorld().getName().equals(MapManager.LOBBY.get("lobby").getWorld().getName())) {
                MapManager.teleportToSpawn(damaged);
                e.setCancelled(true);
                return;
            }
            damaged.setHealth(0.5);
        }
    }
}