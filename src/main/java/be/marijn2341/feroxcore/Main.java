package be.marijn2341.feroxcore;

import be.marijn2341.feroxcore.commands.LobbyCommand;
import be.marijn2341.feroxcore.commands.staff.*;
import be.marijn2341.feroxcore.commands.StatisticsCommand;
import be.marijn2341.feroxcore.commands.VerifyCommand;
import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.database.RegistrationDatabase;
import be.marijn2341.feroxcore.listeners.*;
import be.marijn2341.feroxcore.manager.*;
import be.marijn2341.feroxcore.manager.inventorysettings.ItemStackSerializer;
import be.marijn2341.feroxcore.manager.inventorysettings.Listeners.InventoryDragListener;
import be.marijn2341.feroxcore.manager.statistics.GameStatistics;
import be.marijn2341.feroxcore.manager.statistics.PlayerStatistics;
import be.marijn2341.feroxcore.utils.Gui;
import be.marijn2341.feroxcore.utils.Utils;
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
    private TeamManager teamManager;
    private ScoreboardManager scoreboardManager;
    private PlayerManager playerManager;
    private NexusManager nexusManager;
    private MapManager mapManager;
    private DataManager dataManager;
    private Database database;
    private RegistrationDatabase registrationDatabase;
    private GameStatistics gameStatistics;
    private PlayerStatistics playerStatistics;
    private Gui gui;
    private ItemStackSerializer serializer;

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

        // MAIN INSTANCE
        instance = this;

        // LOAD CLASSES
        teamManager = new TeamManager();
        scoreboardManager = new ScoreboardManager();
        playerManager = new PlayerManager();
        nexusManager = new NexusManager();
        mapManager = new MapManager();
        dataManager = new DataManager();
        database = new Database();
        registrationDatabase = new RegistrationDatabase();
        gameStatistics = new GameStatistics();
        playerStatistics = new PlayerStatistics();
        gui = new Gui();
        serializer = new ItemStackSerializer();

        // REGISTER EVENTS
        registerEvents();

        // REGISTER COMMANDS
        registerCommands();

        // CONNECT TO DATABASE
        database.initialize();

        // LOAD DEFAULT INVENTORY
        playerManager.loadDefaultItems();

        // LOAD ALL MAPS
        mapManager.loadMaps();

        // IMPLEMENT MULTIVERSE CORE
        MultiverseCore core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        wm = core.getMVWorldManager();

        // DELETE ACTIVE MAP ON EVERY RELOAD / RESTART.
        mapManager.ForceDeleteGame();

        // CHECK IF THERE ARE PLAYERS ONLINE (RELOAD)
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            dataManager.getPreviousMap().clear();
            mapManager.startGame();
            for (Player plr : Bukkit.getOnlinePlayers()) {
                Utils.KickOnReload(plr);
            }
        }

        // LOAD THE LOBBY
        mapManager.loadLobby();
    }

    @Override
    public void onDisable() {
        database.getHikari().close();
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

    public TeamManager getTeamManager() {
        return teamManager;
    }
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
    public PlayerManager getPlayerManager() {
        return playerManager;
    }
    public NexusManager getNexusManager() {
        return nexusManager;
    }
    public MapManager getMapManager() {
        return mapManager;
    }
    public DataManager getDataManager() {
        return dataManager;
    }
    public Database getDb() {
        return database;
    }
    public RegistrationDatabase getRegistrationDatabase() {
        return registrationDatabase;
    }
    public GameStatistics getGameStatistics() {
        return gameStatistics;
    }
    public PlayerStatistics getPlayerStatistics() {
        return playerStatistics;
    }
    public Gui getGui() {
        return gui;
    }
    public ItemStackSerializer getSerializer() {
        return serializer;
    }

    public Main() {
        this.WorldsFile = new File(getDataFolder(), "worlds.yml");
        this.WorldsConfig = (FileConfiguration) YamlConfiguration.loadConfiguration(this.WorldsFile);
    }



    public void registerEvents() {
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
        plm.registerEvents((Listener) new be.marijn2341.feroxcore.manager.inventorysettings.Listeners.InventoryClickListener(), (Plugin) this);
        plm.registerEvents((Listener) new InventoryDragListener(), (Plugin) this);
    }

    public void registerCommands() {
        getCommand("loadmaps").setExecutor((CommandExecutor)new LoadMapsCommand());
        getCommand("skipmap").setExecutor((CommandExecutor)new SkipGameCommand());
        getCommand("lobby").setExecutor((CommandExecutor)new LobbyCommand());
        getCommand("verify").setExecutor((CommandExecutor)new VerifyCommand());
        getCommand("startgame").setExecutor((CommandExecutor)new StartGameCommand());
        getCommand("setup").setExecutor((CommandExecutor)new SetupCommand());
        getCommand("statistics").setExecutor((CommandExecutor) new StatisticsCommand());
    }
}