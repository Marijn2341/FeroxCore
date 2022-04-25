package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerManager {

    private final Main main = Main.getInstance();

    public String getOnlineTime(Player player) {

        int time = player.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long hours = TimeUnit.SECONDS.toHours(seconds);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds) - (hours * 60);
        String tijd = Utils.color(hours + "h, " + minutes + "min");
        return tijd;
    }

    public long getOnlineTimeMi(UUID uuid) {
        Player plr = Bukkit.getPlayer(uuid);
        int time = plr.getStatistic(Statistic.PLAY_ONE_TICK);
        int seconds = time / 20;
        long milliseconds = TimeUnit.SECONDS.toMillis(seconds);
        return milliseconds;
    }

    public long getWalkedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.WALK_ONE_CM) / 100);
    }

    public long getSprintedBlocksPlayer(Player player) {
        return (player.getStatistic(Statistic.SPRINT_ONE_CM) / 100);
    }

    public long getJumpsPlayer(Player player) {
        return (player.getStatistic(Statistic.JUMP));
    }

    public void loadDefaultItems() {

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 1);

        main.getDataManager().getDefaultItems().add(new ItemStack(Material.STONE_SWORD));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.BOW));
        main.getDataManager().getDefaultItems().add(pickaxe);
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.STONE_AXE));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.AIR));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.LOG, 64));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.GLASS, 64));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.GOLDEN_APPLE));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.COOKED_BEEF, 16));
        main.getDataManager().getDefaultItems().add(new ItemStack(Material.ARROW, 16));

    }

    public void setInventory(Player player) {
        String inv = main.getDb().getInventory(player);
        ItemStack[] is = main.getSerializer().deserialize(inv);
        main.getDataManager().getInventories().put(player.getUniqueId(), is);
    }
}
