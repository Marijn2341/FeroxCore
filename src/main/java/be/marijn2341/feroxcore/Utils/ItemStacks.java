package be.marijn2341.feroxcore.Utils;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemStacks {

    public ItemStack compass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(Utils.color("&6Team Selector"));
        compass.setItemMeta(compassMeta);
        return compass;
    }

    public ItemStack blue_wool_selector() {
        ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&9Blue Team"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack red_wool_selector() {
        ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&4Red Team"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack random_team_selector() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(Utils.color("&8Random Team"));
        book.setItemMeta(bookMeta);
        return book;
    }

    public ItemStack spectate_selector() {
        ItemStack wool = new ItemStack(Material.STAINED_GLASS, 1, (short) 0);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&8Spectate"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack PlayerSkull(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(player.getName());
        skullMeta.setDisplayName(Utils.color("&6Statistics"));
        skull.setItemMeta(skullMeta);
        return skull;
    }

    // Statistics
    public ItemStack PlayerKills(Player player) {
        int kills = Database.GetKillsDB(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You killed &8" + kills + " &7other players."));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(Utils.color("&6Kills: &7" + kills));
        swordMeta.setLore(lore);
        sword.setItemMeta(swordMeta);
        return sword;
    }

    public ItemStack PlayerDeaths(Player player) {
        int deaths = Database.GetDeathsDB(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You died &8" + deaths + " &7times."));

        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Deaths: &7" + deaths));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack PlayerWins(Player player) {
        int wins = Database.GetWins(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You won &8" + wins + " &7times."));

        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Wins: &7" + wins));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack PlayerLoses(Player player) {
        int loses = Database.GetLoses(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You lost &8" + loses + " &7times."));

        ItemStack item = new ItemStack(Material.YELLOW_FLOWER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Loses: &7" + loses));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack PlayerOnlineTime(Player player) {
        String time = PlayerManager.GetOnlineTime(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You played &8" + time + "&7 on this server."));

        ItemStack item = new ItemStack(Material.WATCH);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Playtime: &7" + time));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack BlocksWalked(Player player) {
        long walked = PlayerManager.GetWalkedBlocksPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've walked &8" + walked + " &7blocks."));

        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6BLocks walked: &7" + walked));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack BlocksSprinted(Player player) {
        long sprinted = PlayerManager.GetSprintedBlocksPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've sprinted &8" + sprinted + " &7blocks."));

        ItemStack item = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6BLocks sprinted: &7" + sprinted));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack TimesJumped(Player player) {
        long jumped = PlayerManager.GetJumpsPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've jumped &8" + jumped + "&7 times"));

        ItemStack item = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Jumped: &7" + jumped));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack NexusesBroken(Player player) {
        int nexuses = Database.GetBrokenNexuses(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You broke &8" + nexuses + " &7nexuses."));

        ItemStack item = new ItemStack(Material.OBSIDIAN);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Broke nexuses: &7" + nexuses));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }



}
