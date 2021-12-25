package be.marijn2341.feroxcore;

import be.marijn2341.feroxcore.Commands.LobbyCommand;
import be.marijn2341.feroxcore.Commands.Staff.*;
import be.marijn2341.feroxcore.Commands.VerifyCommand;
import be.marijn2341.feroxcore.Database.Database;
import be.marijn2341.feroxcore.Listeners.*;
import be.marijn2341.feroxcore.Manager.MapManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    private FileConfiguration WorldsConfig;
    private File WorldsFile;

    private static Main instance;

    public static MVWorldManager wm;

    public Database SQL;

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
        getServer().getPluginManager().registerEvents((Listener) new JoinListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new FoodListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new EntityDamageListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new LeafListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new PlayerInteractListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new QuitListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new InventoryClickListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new DeathListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new RespawnListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new BlockBreakListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new BlockPlaceListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new FriendlyFire(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new ChatListener(), (Plugin) this);
        getServer().getPluginManager().registerEvents((Listener) new WeatherListener(), (Plugin) this);

        // REGISTER COMMANDS
        getCommand("loadmaps").setExecutor((CommandExecutor)new LoadMapsCommand());
        getCommand("skipmap").setExecutor((CommandExecutor)new SkipGameCommand());
        getCommand("lobby").setExecutor((CommandExecutor)new LobbyCommand());
        getCommand("test").setExecutor((CommandExecutor)new test());
        getCommand("verify").setExecutor((CommandExecutor)new VerifyCommand());
        getCommand("startgame").setExecutor((CommandExecutor)new StartGameCommand());
        getCommand("setup").setExecutor((CommandExecutor)new SetupCommand());

        // MAIN INSTANCE
        instance = this;

        // LAAD ALLE MAPPEN IN
        MapManager.LoadMaps();

        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        wm = core.getMVWorldManager();

        // DELETE ACTIVE MAP ON EVERY RELOAD / RESTART.
        MapManager.ForceDeleteGame();

        // IF THERE IS A PLAYER ONLINE, START A GAME.
        if (TeamManager.CountOnlinePlayers() >= 1) {
            MapManager.PreviousMap.clear();
            MapManager.StartGame();
        }

        // CHECK IF THERE ARE PLAYERS ONLINE (RELOAD)
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            for (Player plr : Bukkit.getOnlinePlayers()) {
                MapManager.KickOnReload(plr);
            }
        }

        // START DATABASE
        this.SQL = new Database();
        try {
            SQL.connect();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        MapManager.LoadLobby();
    }

    @Override
    public void onDisable() {
        SQL.disconnect();
    }

    public void loadYaml(File file, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.load(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
}