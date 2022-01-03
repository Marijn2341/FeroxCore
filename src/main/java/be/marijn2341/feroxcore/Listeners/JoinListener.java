package be.marijn2341.feroxcore.Listeners;

import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Manager.InventorySettings.ItemStackSerializer;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import be.marijn2341.feroxcore.Utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        // JOIN MESSAGE
        e.setJoinMessage(Utils.color("&8[&a&l+&8] &7" + player.getName()));

        NametagEdit.getApi().setPrefix(player, "&3&7");

        // CHECK IF USER IS IN DATABASE, IF NOT, INSERT USER
        if (!(Database.CheckIfUserExists(player.getUniqueId()))) {
            Database.InsertNewUser(player.getUniqueId());
        }

        if (!(Database.InventoryPlayerExists(player))) {
            String inv = ItemStackSerializer.serialize(PlayerManager.defaultitems.toArray(new ItemStack[0]));
            Database.SetInventory(player, inv);
        }

        // TELEPORT PLAYER TO SPAWN
        MapManager.TeleportToSpawn(player);
        player.sendMessage(Utils.color("&9--- &9&lFerox&f&lMC &9---"));
        player.sendMessage(Utils.color("&7Welcome on &9Ferox&fMC&9."));
        player.sendMessage(Utils.color("&7If you want to know how this game works, &9click here&7. (does not work yet)"));
        player.sendMessage(Utils.color("&7Enjoy the game!"));
        player.sendMessage(Utils.color("&9----------------"));

        // START A GAME WHEN THERE IS MIN. 1 PLAYER ON THE SERVER
        if (TeamManager.CountOnlinePlayers() == 1) {
            MapManager.StartGame();
        }
    }
}
