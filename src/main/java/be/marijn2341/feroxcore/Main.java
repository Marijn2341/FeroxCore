package be.marijn2341.feroxcore;

import be.marijn2341.feroxcore.Commands.LobbyCommand;
import be.marijn2341.feroxcore.Commands.Staff.*;
import be.marijn2341.feroxcore.Commands.StatisticsCommand;
import be.marijn2341.feroxcore.Commands.VerifyCommand;
import be.marijn2341.feroxcore.Commands.testCommand;
import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Listeners.*;
import be.marijn2341.feroxcore.Manager.InventorySettings.Listeners.InventoryDragListener;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import be.marijn2341.feroxcore.Utils.Utils;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    private FileConfiguration WorldsConfig;
    private File WorldsFile;

    private static Main instance;

    public static MVWorldManager wm;

    @Override
    public void onEnable() {
        // CHECK IF WORLDS FILE EXISTS
        if (!this.WorldsFile.exists()) {
            this.saveResource("worlds.yml", true);
        }

        // ON ENABLE CONSOLE LOG
        Bukkit.getLogger().info("Marijn2341 - FeroxHosting || Enabled");

        // DATABASE CREDITS
        getConfig().addDefault("MySQL.Host", "host");
        getConfig().addDefault("MySQL.Port", "3306");
        getConfig().addDefault("MySQL.Database", "db");
        getConfig().addDefault("MySQL.Username", "user");
        getConfig().addDefault("MySQL.Password", "pass");

        // DEFAULT CONFIG
        getConfig().options().copyDefaults(true);
        saveConfig();

        // REGISTER EVENTS
        RegisterEvents();

        // REGISTER COMMANDS
        RegisterCommands();

        // MAIN INSTANCE
        instance = this;

        // LOAD DEFAULT INVENTORY
        PlayerManager.LoadDefaultItems();

        // LOAD ALL MAPS
        MapManager.LoadMaps();

        // IMPLEMENT MULTIVERSE CORE
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        wm = core.getMVWorldManager();

        // DELETE ACTIVE MAP ON EVERY RELOAD / RESTART.
        MapManager.ForceDeleteGame();

        // CHECK IF THERE ARE PLAYERS ONLINE (RELOAD)
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            MapManager.PreviousMap.clear();
            MapManager.StartGame();
            for (Player plr : Bukkit.getOnlinePlayers()) {
                Utils.KickOnReload(plr);
            }
        }

        // CONNECT TO DATABASE
        Database.initialize();

        // LOAD THE LOBBY
        MapManager.LoadLobby();
    }

    @Override
    public void onDisable() {
        Database.getHikari().close();
    }

    public File getWorldsFile() {
        return this.WorldsFile;
    }

    public FileConfiguration getWorldsConfig() {
        return this.WorldsConfig;
    }

    public static Main getInstance() {
        return instance;
    }

    public Main() {
        this.WorldsFile = new File(getDataFolder(), "worlds.yml");
        this.WorldsConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.WorldsFile);
    }

    public void RegisterEvents() {
        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents((Listener) new JoinListener(), (Plugin) this);
        plm.registerEvents((Listener) new FoodListener(), (Plugin) this);
        plm.registerEvents((Listener) new EntityDamageListener(), (Plugin) this);
        plm.registerEvents((Listener) new LeafListener(), (Plugin) this);
        plm.registerEvents((Listener) new PlayerInteractListener(), (Plugin) this);
        plm.registerEvents((Listener) new QuitListener(), (Plugin) this);
        plm.registerEvents((Listener) new InventoryClickListener(), (Plugin) this);
        plm.registerEvents((Listener) new DeathListener(), (Plugin) this);
        plm.registerEvents((Listener) new RespawnListener(), (Plugin) this);
        plm.registerEvents((Listener) new BlockBreakListener(), (Plugin) this);
        plm.registerEvents((Listener) new BlockPlaceListener(), (Plugin) this);
        plm.registerEvents((Listener) new FriendlyFire(), (Plugin) this);
        plm.registerEvents((Listener) new ChatListener(), (Plugin) this);
        plm.registerEvents((Listener) new WeatherListener(), (Plugin) this);
        plm.registerEvents((Listener) new ArrowShootListener(), (Plugin) this);
        plm.registerEvents((Listener) new be.marijn2341.feroxcore.Manager.InventorySettings.Listeners.InventoryClickListener(), (Plugin) this);
        plm.registerEvents((Listener) new InventoryDragListener(), (Plugin) this);
    }

    public void RegisterCommands() {
        getCommand("loadmaps").setExecutor((CommandExecutor)new LoadMapsCommand());
        getCommand("skipmap").setExecutor((CommandExecutor)new SkipGameCommand());
        getCommand("lobby").setExecutor((CommandExecutor)new LobbyCommand());
        getCommand("verify").setExecutor((CommandExecutor)new VerifyCommand());
        getCommand("startgame").setExecutor((CommandExecutor)new StartGameCommand());
        getCommand("setup").setExecutor((CommandExecutor)new SetupCommand());
        getCommand("statistics").setExecutor((CommandExecutor) new StatisticsCommand());
        getCommand("test").setExecutor((CommandExecutor) new testCommand());
    }
}