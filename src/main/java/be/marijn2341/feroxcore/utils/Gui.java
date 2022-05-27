package be.marijn2341.feroxcore.utils;

import be.marijn2341.feroxcore.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Gui {

    private final Main main = Main.getInstance();

    public void createTeamSelectorMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory teamselector = Bukkit.createInventory(player, 9, Utils.color("&6Team Selector"));
        teamselector.setItem(2, is.blueWoolSelector());
        teamselector.setItem(6, is.redWoolSelector());
        teamselector.setItem(4, is.randomTeamSelector());
        teamselector.setItem(8, is.spectateSelector());
        player.openInventory(teamselector);
    }

    public void createStaticsMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory statistics = Bukkit.createInventory(player, 45, Utils.color("&6Statistics"));
        statistics.setItem(13, is.playerOnlineTime(player));
        statistics.setItem(21, is.blocksWalked(player));
        statistics.setItem(22, is.blocksSprinted(player));
        statistics.setItem(23, is.timesJumped(player));
        statistics.setItem(29, is.playerKills(player));
        statistics.setItem(30, is.playerDeaths(player));
        statistics.setItem(31, is.playerLoses(player));
        statistics.setItem(32, is.playerWins(player));
        statistics.setItem(33, is.nexusesBroken(player));
        player.openInventory(statistics);
    }

    public void createPlayerMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory(player, 27, Utils.color("&6" + player.getName()));
        inv.setItem(12, is.playerStatistics(player));
        inv.setItem(14, is.playerSettings());
        player.openInventory(inv);
    }

    public void createSettingsMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory(player, 27, Utils.color("&6Settings"));
        inv.setItem(13, is.editInventory());
        player.openInventory(inv);
    }

    public void editInventory(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory(player, 36, Utils.color("&6Edit Inventory"));
        ItemStack[] items = main.getSerializer().deserialize(main.getDb().getInventory(player));
        inv.setContents(items);

        for (int i = 18; i <= 26; i++) {
            inv.setItem(i, is.blueStainedGlass());
        }

        inv.setItem(27, is.redStainedGlass());
        inv.setItem(28, is.redStainedGlass());
        inv.setItem(29, is.redStainedGlass());
        inv.setItem(30, is.redStainedGlass());
        inv.setItem(31, is.blueStainedGlass());
        inv.setItem(32, is.greenStainedGlass());
        inv.setItem(33, is.greenStainedGlass());
        inv.setItem(34, is.greenStainedGlass());
        inv.setItem(35, is.greenStainedGlass());
        player.openInventory(inv);
    }
}
