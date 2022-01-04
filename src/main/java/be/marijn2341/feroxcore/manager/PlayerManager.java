package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    public static ArrayList<ItemStack> DEFAULTITEMS = new ArrayList<ItemStack>();

    public static String getOnlineTime(Player player) {

        int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd = Utils.color(hours + "h, " + minutes + "min");
        return tijd;
    }

    public static long getOnlineTimeMi(UUID uuid) {
        Player plr = Bukkit.getPlayer(uuid);
        int time = plr.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long milliseconds = TimeUnit.SECONDS.toMillis(seconds);
        return milliseconds;
    }

    public static long getWalkedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.WALK_ONE_CM) / 100);
    }

    public static long getSprintedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.SPRINT_ONE_CM) / 100);
    }

    public static long getJumpsPlayer(Player player) {
        return (player.getStatistic(Statistic.JUMP));
    }

    public static void loadDefaultItems() {

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);

        DEFAULTITEMS.add(new ItemStack(Material.STONE_SWORD));
        DEFAULTITEMS.add(new ItemStack(Material.BOW));
        DEFAULTITEMS.add(pickaxe);
        DEFAULTITEMS.add(new ItemStack(Material.STONE_AXE));
        DEFAULTITEMS.add(new ItemStack(Material.AIR));
        DEFAULTITEMS.add(new ItemStack(Material.LOG, 64));
        DEFAULTITEMS.add(new ItemStack(Material.GLASS, 64));
        DEFAULTITEMS.add(new ItemStack(Material.GOLDEN_APPLE));
        DEFAULTITEMS.add(new ItemStack(Material.COOKED_BEEF, 16));
        DEFAULTITEMS.add(new ItemStack(Material.ARROW, 16));

    }
}
