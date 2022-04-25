package be.marijn2341.feroxcore.listeners;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class JoinListener implements Listener {

    private final Main main = Main.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        Player player = e.getPlayer();

        // JOIN MESSAGE
        e.setJoinMessage(Utils.color("&8[&a&l+&8] &7" + player.getName()));

        NametagEdit.getApi().setPrefix(player, "&3&7");

        // CHECK IF USER IS IN DATABASE, IF NOT, INSERT USER
        if (!(main.getDb().checkIfUserExists(player.getUniqueId()))) {
            main.getDb().insertNewUser(player.getUniqueId());
        }

        if (!(main.getDb().inventoryPlayerExists(player))) {
            String inv = main.getSerializer().serialize(main.getDataManager().getDefaultItems().toArray(new ItemStack[0]));
            main.getDb().setInventory(player, inv);
        }

        // TELEPORT PLAYER TO SPAWN
        main.getMapManager().teleportToSpawn(player);
        player.sendMessage(Utils.color("&9--- &9&lFerox&f&lMC &9---"));
        player.sendMessage(Utils.color("&7Welcome on &9Ferox&fMC&9."));
        player.sendMessage(Utils.color("&7If you want to know how this game works, &9click here&7. (does not work yet)"));
        player.sendMessage(Utils.color("&7Enjoy the game!"));
        player.sendMessage(Utils.color("&9----------------"));

        // START A GAME WHEN THERE IS MIN. 1 PLAYER ON THE SERVER
        if (main.getTeamManager().countOnlinePlayers() == 1) {
            main.getMapManager().startGame();
        }
    }
}
