package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
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

    private Main main = Main.getInstance();

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
        if (main.getTeamManager().AllreadyInTeam(deathplayer)) {
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

        if (!(main.getTeamManager().AllreadyInTeam(deathplayer))) {
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

        main.getDataManager().getDeaths().put(deathplayer.getUniqueId(), main.getDataManager().getDeaths().get(deathplayer.getUniqueId()) + 1);


        // ------------------------------
        // CHECK IF PLAYER DIED BY A CACTUS
        // ------------------------------

        if (e.getEntity().getLastDamageCause() instanceof EntityDamageByBlockEvent) {
            EntityDamageByBlockEvent cause = (EntityDamageByBlockEvent) e.getEntity().getLastDamageCause();
            if (cause.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                if (main.getDataManager().getLastDamager().containsKey(deathplayer)) {
                    Player killer = main.getDataManager().getLastDamager().get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked into a cactus by &" + main.getTeamManager().GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD KILL TO KILLER
                    main.getDataManager().getKills().put(killer.getUniqueId(), main.getDataManager().getKills().get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    main.getDataManager().getLastDamager().remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) +
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
                if (main.getDataManager().getLastDamager().containsKey(deathplayer)) {
                    Player killer = main.getDataManager().getLastDamager().get(deathplayer);
                    e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) +
                            deathplayer.getName() + " &7got knocked to the ground by &" + main.getTeamManager().GetTeamColor(killer) + killer.getName() +
                            "&7."));
                    // ADD KILL TO KILLER
                    main.getDataManager().getKills().put(killer.getUniqueId(), main.getDataManager().getKills().get(killer.getUniqueId()) + 1);
                    // REMOVE PLAYER FROM HASHMAP
                    main.getDataManager().getLastDamager().remove(deathplayer);
                    return;
                } else {
                    e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) +
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
                    if (main.getDataManager().getLastDamager().containsKey(deathplayer)) {
                        Player killer = main.getDataManager().getLastDamager().get(deathplayer);
                        e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) +
                                deathplayer.getName() + " &7got knocked into the void by &" + main.getTeamManager().GetTeamColor(killer) + killer.getName() +
                                "&7."));
                        // ADD KILL TO KILLER
                        main.getDataManager().getKills().put(killer.getUniqueId(), main.getDataManager().getKills().get(killer.getUniqueId()) + 1);
                        // REMOVE PLAYER FROM HASHMAP
                        main.getDataManager().getLastDamager().remove(deathplayer);
                        return;
                    } else {
                        e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) + deathplayer.getName() + " &7died in the void."));
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
                            e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) + deathplayer.getName() + " &7got shot by &" +
                                    main.getTeamManager().GetTeamColor(shooter) + shooter.getName() + "&7."));
                            main.getDataManager().getKills().put(shooter.getUniqueId(), main.getDataManager().getKills().get(shooter.getUniqueId()) + 1);
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
                if (main.getDataManager().getTeamBlue().contains(killer.getUniqueId()) || main.getDataManager().getTeamRed().contains(killer.getUniqueId())) {
                    e.setDeathMessage(Utils.color("&" + main.getTeamManager().GetTeamColor(deathplayer) + deathplayer.getName() + " &7got killed by &" + main.getTeamManager().GetTeamColor(killer) + killer.getName() + "&7."));
                    // ADD KILL TO KILLER
                    main.getDataManager().getKills().put(killer.getUniqueId(), main.getDataManager().getKills().get(killer.getUniqueId()) + 1);

                    if (main.getDataManager().getLastDamager().containsKey(deathplayer)) {
                        main.getDataManager().getLastDamager().remove(deathplayer);
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
                if (main.getTeamManager().AllreadyInTeam(hitter) && main.getTeamManager().AllreadyInTeam(hitted)) {
                    if (main.getDataManager().getTeamRed().contains(hitter.getUniqueId()) == main.getDataManager().getTeamRed().contains(hitted.getUniqueId())) {
                        return;
                    }
                    if (main.getDataManager().getTeamBlue().contains(hitter.getUniqueId()) == main.getDataManager().getTeamBlue().contains(hitted.getUniqueId())) {
                        return;
                    }
                    main.getDataManager().getArrowsHit().put(hitter.getUniqueId(), main.getDataManager().getArrowsHit().get(hitter.getUniqueId()) + 1);
                    main.getDataManager().getLastDamager().put(hitted, hitter);
                }
            } else {
                Player hitter = (Player) e.getDamager();
                if (main.getTeamManager().AllreadyInTeam(hitter) && main.getTeamManager().AllreadyInTeam(hitted)) {
                    if (main.getDataManager().getTeamRed().contains(hitter.getUniqueId()) == main.getDataManager().getTeamRed().contains(hitted.getUniqueId())) {
                        return;
                    }
                    if (main.getDataManager().getTeamBlue().contains(hitter.getUniqueId()) == main.getDataManager().getTeamBlue().contains(hitted.getUniqueId())) {
                        return;
                    }
                    main.getDataManager().getLastDamager().put(hitted, hitter);
                }
            }


            if(main.getDataManager().getQueue().get(hitted) != null){
                main.getDataManager().getQueue().get(hitted).cancel();
            }
            BukkitTask br = new BukkitRunnable() {
                public void run() {main.getDataManager().getLastDamager().remove(hitted);
                }
            }.runTaskLater(Main.getInstance(), 200);
            main.getDataManager().getQueue().put(hitted, br);
            }
    }

    @EventHandler
    public void onVoidDamage(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.VOID) {
            Player damaged = (Player) e.getEntity();
            if (damaged.getWorld().getName().equals(main.getDataManager().getLobby().get("lobby").getWorld().getName())) {
                main.getMapManager().teleportToSpawn(damaged);
                e.setCancelled(true);
                return;
            }
            damaged.setHealth(0.5);
        }
    }
}