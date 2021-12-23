package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class DeathListener implements Listener {

    public static HashMap<UUID, Integer> Deaths = new HashMap<>();
    public static HashMap<UUID, Integer> Kills = new HashMap<>();
    public static HashMap<Player, BukkitTask> Queue = new HashMap<>();
    public static HashMap<Player, Player> LastDamager = new HashMap<>();

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


        // ------------------------------
        // CHECK IF PLAYER DIED BY A CACTUS
        // ------------------------------

        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent cause = (EntityDamageByBlockEvent) e.getEntity().getLastDamageCause();
            if (cause.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                if (LastDamager.containsKey(deathplayer)) {
                    Player killer = LastDamager.get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked into a cactus by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD DEATH TO PLAYER
                    Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                    // ADD KILL TO KILLER
                    Kills.put(killer.getUniqueId(), Kills.get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    LastDamager.remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got rekt by a cactus!"));
                    Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                    return;
                }
            }
        }

        // ------------------------------
        // CHECK IF PLAYER DIED BY FALLING
        // ------------------------------

            EntityDamageEvent cause2 = e.getEntity().getLastDamageCause();
            if (cause2.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (LastDamager.containsKey(deathplayer)) {
                    Player killer = LastDamager.get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked to the ground by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD DEATH TO PLAYER
                    Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                    // ADD KILL TO KILLER
                    Kills.put(killer.getUniqueId(), Kills.get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    LastDamager.remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7wanted to kiss the ground!"));
                    Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                    return;
                }
            }

            // ------------------------------
            // CHECK IF PLAYER DIED IN VOID
            // ------------------------------


            if (e.getEntity().getLastDamageCause() instanceof EntityDamageByBlockEvent) {
                EntityDamageByBlockEvent cause = (EntityDamageByBlockEvent) e.getEntity().getLastDamageCause();
                if (cause.getCause() == EntityDamageEvent.DamageCause.VOID) {
                    if (LastDamager.containsKey(deathplayer)) {
                        Player killer = LastDamager.get(deathplayer);
                        e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) +
                                deathplayer.getName() + " &7got knocked into the void by &" + TeamManager.GetTeamColor(killer) + killer.getName() +
                                "&7."));
                        // ADD DEATH TO PLAYER
                        Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                        // ADD KILL TO KILLER
                        Kills.put(killer.getUniqueId(), Kills.get(killer.getUniqueId()) + 1);
                        // REMOVE PLAYER FROM HASHMAP
                        LastDamager.remove(deathplayer);
                        return;
                    } else {
                        e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) + deathplayer.getName() + " &7died in the void."));
                        // ADD DEATH TO PLAYER
                        Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                        return;
                    }
                }
            }

            // ------------------------------
            // KILLER KILLS PLAYER (BOW AND ARROW)
            // ------------------------------

            // ------------------------------
            // KILLER KILLS PLAYER (NORMAL)
            // ------------------------------

            if (deathplayer.getKiller() != null) {
                Player killer = deathplayer.getKiller();
                if (TeamManager.TeamBlue.contains(killer.getUniqueId()) || TeamManager.TeamRed.contains(killer.getUniqueId())) {
                    e.setDeathMessage(Utils.color("&" + TeamManager.GetTeamColor(deathplayer) + deathplayer.getName() + " &7got killed by &" + TeamManager.GetTeamColor(killer) + killer.getName() + "&7."));
                    // ADD DEATH TO PLAYER
                    Deaths.put(deathplayer.getUniqueId(), Deaths.get(deathplayer.getUniqueId()) + 1);
                    // ADD KILL TO KILLER
                    Kills.put(killer.getUniqueId(), Kills.get(killer.getUniqueId()) + 1);

                    if (LastDamager.containsKey(deathplayer)) {
                        LastDamager.remove(deathplayer);
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
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player hitter = (Player) e.getDamager();
            Player hitted = (Player) e.getEntity();

            if (TeamManager.AllreadyInTeam(hitter) && TeamManager.AllreadyInTeam(hitted)) {
                if (TeamManager.TeamRed.contains(hitter.getUniqueId()) == TeamManager.TeamRed.contains(hitted.getUniqueId())) {
                    return;
                }
                if (TeamManager.TeamBlue.contains(hitter.getUniqueId()) == TeamManager.TeamBlue.contains(hitted.getUniqueId())) {
                    return;
                }
                LastDamager.put(hitted, hitter);
                if(Queue.get(hitted) != null){
                    Queue.get(hitted).cancel();
                }
                BukkitTask br = new BukkitRunnable() {
                    public void run() {
                        LastDamager.remove(hitted);
                    }
                }.runTaskLater(Main.getInstance(), 200);
                Queue.put(hitted, br);
            }
        }
    }

    @EventHandler
    public void onVoidDamage(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player damaged = (Player) e.getEntity();
            damaged.setHealth(0.5);
        }
    }
}