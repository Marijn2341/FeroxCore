package be.marijn2341.feroxcore.Utils;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Manager.InventorySettings.ItemStackSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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

    public static void CreatePlayerMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory((InventoryHolder) player, 27, Utils.color("&6" + player.getName()));
        inv.setItem(12, is.PlayerStatistics(player));
        inv.setItem(14, is.PlayerSettings());
        player.openInventory(inv);
    }

    public static void CreateSettingsMenu(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory((InventoryHolder) player, 27, Utils.color("&6Settings"));
        inv.setItem(13, is.EditInventory());
        player.openInventory(inv);
    }

    public static void EditInventory(Player player) {
        ItemStacks is = new ItemStacks();
        Inventory inv = Bukkit.createInventory((InventoryHolder) player, 36, Utils.color("&6Edit Inventory"));
        ItemStack[] items = ItemStackSerializer.deserialize(Database.GetInventory(player));
        inv.setContents(items);

        for (int i = 18; i <= 26; i++) {
            inv.setItem(i, is.blue_stained_glass());
        }

        inv.setItem(27, is.red_stained_glass());
        inv.setItem(28, is.red_stained_glass());
        inv.setItem(29, is.red_stained_glass());
        inv.setItem(30, is.red_stained_glass());
        inv.setItem(31, is.blue_stained_glass());
        inv.setItem(32, is.green_stained_glass());
        inv.setItem(33, is.green_stained_glass());
        inv.setItem(34, is.green_stained_glass());
        inv.setItem(35, is.green_stained_glass());
        player.openInventory(inv);
    }
}
