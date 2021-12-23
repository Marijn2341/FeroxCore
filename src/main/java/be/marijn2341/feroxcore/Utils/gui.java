package be.marijn2341.feroxcore.Utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class gui {

    public static void CreateTeamSelectorMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory teamselector = Bukkit.createInventory((InventoryHolder)player, 9, Utils.color("&6Team Selector"));
        teamselector.setItem(2, is.blue_wool_selector());
        teamselector.setItem(6, is.red_wool_selector());
        teamselector.setItem(4, is.random_team_selector());
        teamselector.setItem(8, is.spectate_selector());
        player.openInventory(teamselector);
    }

    public static void CreateStaticsMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory statistics = Bukkit.createInventory((InventoryHolder)player, 45, Utils.color("&6Statistics"));
        statistics.setItem(13, is.PlayerOnlineTime(player));
        statistics.setItem(21, is.BlocksWalked(player));
        statistics.setItem(22, is.BlocksSprinted(player));
        statistics.setItem(23, is.TimesJumped(player));
        statistics.setItem(29, is.PlayerKills(player));
        statistics.setItem(30, is.PlayerDeaths(player));
        statistics.setItem(31, is.PlayerLoses(player));
        statistics.setItem(32, is.PlayerWins(player));
        statistics.setItem(33, is.NexusesBroken(player));
        player.openInventory(statistics);
    }
}
