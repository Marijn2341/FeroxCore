package be.marijn2341.feroxcore.utils;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class ItemStacks {

    private final Main main = Main.getInstance();

    public ItemStack compass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();
        compassMeta.setDisplayName(Utils.color("&6Team Selector"));
        compass.setItemMeta(compassMeta);
        return compass;
    }

    public ItemStack blueWoolSelector() {
        ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&9Blue Team"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack blueStainedGlass() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&7"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack redStainedGlass() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&c&lCancel"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack greenStainedGlass() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (short) 5);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&a&lSave"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack redWoolSelector() {
        ItemStack wool = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&4Red Team"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack randomTeamSelector() {
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName(Utils.color("&8Random Team"));
        book.setItemMeta(bookMeta);
        return book;
    }

    public ItemStack spectateSelector() {
        ItemStack wool = new ItemStack(Material.STAINED_GLASS, 1, (short) 0);
        ItemMeta woolMeta = wool.getItemMeta();
        woolMeta.setDisplayName(Utils.color("&8Spectate"));
        wool.setItemMeta(woolMeta);
        return wool;
    }

    public ItemStack playerSkull(Player player) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner(player.getName());
        skullMeta.setDisplayName(Utils.color("&6" + player.getName()));
        skull.setItemMeta(skullMeta);
        return skull;
    }

    // Statistics
    public ItemStack playerKills(Player player) {
        int kills = main.getDb().getKillsDB(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You killed &8" + kills + " &7other players."));

        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setDisplayName(Utils.color("&6Kills: &7" + kills));
        swordMeta.setLore(lore);
        sword.setItemMeta(swordMeta);
        return sword;
    }

    public ItemStack playerDeaths(Player player) {
        int deaths = main.getDb().getDeathsDB(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You died &8" + deaths + " &7times."));

        ItemStack item = new ItemStack(Material.REDSTONE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Deaths: &7" + deaths));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack playerWins(Player player) {
        int wins = main.getDb().getWins(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You won &8" + wins + " &7times."));

        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Wins: &7" + wins));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack playerLoses(Player player) {
        int loses = main.getDb().getLoses(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You lost &8" + loses + " &7times."));

        ItemStack item = new ItemStack(Material.YELLOW_FLOWER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Loses: &7" + loses));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack playerOnlineTime(Player player) {
        String time = main.getPlayerManager().getOnlineTime(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You played &8" + time + "&7 on this server."));

        ItemStack item = new ItemStack(Material.WATCH);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Playtime: &7" + time));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack blocksWalked(Player player) {
        long walked = main.getPlayerManager().getWalkedBlocksPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've walked &8" + walked + " &7blocks."));

        ItemStack item = new ItemStack(Material.LEATHER_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6BLocks walked: &7" + walked));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack blocksSprinted(Player player) {
        long sprinted = main.getPlayerManager().getSprintedBlocksPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've sprinted &8" + sprinted + " &7blocks."));

        ItemStack item = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6BLocks sprinted: &7" + sprinted));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack timesJumped(Player player) {
        long jumped = main.getPlayerManager().getJumpsPlayer(player);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You've jumped &8" + jumped + "&7 times"));

        ItemStack item = new ItemStack(Material.RABBIT_FOOT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Jumped: &7" + jumped));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack nexusesBroken(Player player) {
        int nexuses = main.getDb().getBrokenNexuses(player.getUniqueId());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7You broke &8" + nexuses + " &7nexuses."));

        ItemStack item = new ItemStack(Material.OBSIDIAN);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Broke nexuses: &7" + nexuses));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack playerStatistics(Player player) {
        int kills = main.getDb().getKillsDB(player.getUniqueId());
        int deaths = main.getDb().getDeathsDB(player.getUniqueId());
        int nexusesbroken = main.getDb().getBrokenNexuses(player.getUniqueId());
        int wins = main.getDb().getWins(player.getUniqueId());
        int loses = main.getDb().getLoses(player.getUniqueId());
        String time = main.getPlayerManager().getOnlineTime(player);


        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7Kills: &f" + kills));
        lore.add(Utils.color("&7Deaths: &f" + deaths));
        lore.add(Utils.color("&7Wins: &f" + wins));
        lore.add(Utils.color("&7Loses: &f" + loses));
        lore.add(Utils.color("&7Nexuses broken: &f" + nexusesbroken));
        lore.add(Utils.color("&7Time: &f" + time));

        ItemStack item = new ItemStack(Material.RED_ROSE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(Utils.color("&6Player Statistics"));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack playerSettings() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7Edit your settings."));

        ItemStack item = new ItemStack(Material.ANVIL);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Settings"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack editInventory() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(Utils.color("&7Click here to change"));
        lore.add(Utils.color("&7your ingame inventory."));

        ItemStack item = new ItemStack(Material.STONE_SWORD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.color("&6Edit Inventory"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }
}