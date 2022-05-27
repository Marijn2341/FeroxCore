package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;

import java.util.UUID;

public class LevelManager {

    private final Main main = Main.getInstance();
    public int getPlayerLevel(UUID uuid) {
        return main.getDataManager().getLevel().get(uuid);
    }
    public void setPlayerLevel(UUID uuid, int level) {
        main.getDataManager().getLevel().put(uuid, level);
    }

    public int getPlayerXP(UUID uuid) {
        return main.getDataManager().getXP().get(uuid);
    }
    public void setPlayerXP(UUID uuid, int xp) {
        main.getDataManager().getXP().put(uuid, xp);
    }

    public void checkPlayer(UUID uuid) {
        int xpneeded = getPlayerLevel(uuid) * 50;
        int xp = getPlayerXP(uuid);
        if (xp >= xpneeded) {
            playerLevelUp(uuid);
        }
    }

    public void addPlayerXP(UUID uuid, int xp) {
        int new_xp = getPlayerXP(uuid) + xp;
        setPlayerXP(uuid, new_xp);
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Bukkit.getPlayer(uuid).sendMessage(Utils.color("&6+" + xp + "xp"));
        }
        checkPlayer(uuid);
    }

    public void playerLevelUp(UUID uuid) {
        int current = getPlayerLevel(uuid);
        int level = current + 1;
        setPlayerLevel(uuid, level);
        setPlayerXP(uuid,0);
        main.getDb().setLevel(uuid, level);
        main.getDb().setXP(uuid, 0);
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Utils.sendTitle(Bukkit.getPlayer(uuid), "&6&lLevel Up", "&fJe bent nu level &3" + level + "&f!", 5, 100, 5);
        }
    }
}
