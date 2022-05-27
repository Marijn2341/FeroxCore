package be.marijn2341.feroxcore;

import be.marijn2341.feroxcore.commands.LobbyCommand;
import be.marijn2341.feroxcore.commands.StatisticsCommand;
import be.marijn2341.feroxcore.commands.VerifyCommand;
import be.marijn2341.feroxcore.commands.staff.LoadMapsCommand;
import be.marijn2341.feroxcore.commands.staff.SetupCommand;
import be.marijn2341.feroxcore.commands.staff.SkipGameCommand;
import be.marijn2341.feroxcore.commands.staff.StartGameCommand;
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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static MVWorldManager wm;
    private static Main instance;
    private final FileConfiguration WorldsConfig;
    private final File WorldsFile;
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

    public Main() {
        this.WorldsFile = new File(getDataFolder(), "worlds.yml");
        this.WorldsConfig = YamlConfiguration.loadConfiguration(this.WorldsFile);
    }

    public static Main getInstance() {
        return instance;
    }

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

        getConfig().addDefault("Ranks.Noob", 0);
        getConfig().addDefault("Ranks.Beginner", 5);
        getConfig().addDefault("Ranks.Pro", 20);
        getConfig().addDefault("Ranks.Master", 30);
        getConfig().addDefault("Ranks.God", 50);

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

    public void registerEvents() {
        PluginManager plm = getServer().getPluginManager();
        plm.registerEvents(new JoinListener(), this);
        plm.registerEvents(new FoodListener(), this);
        plm.registerEvents(new EntityDamageListener(), this);
        plm.registerEvents(new LeafListener(), this);
        plm.registerEvents(new PlayerInteractListener(), this);
        plm.registerEvents(new QuitListener(), this);
        plm.registerEvents(new InventoryClickListener(), this);
        plm.registerEvents(new DeathListener(), this);
        plm.registerEvents(new RespawnListener(), this);
        plm.registerEvents(new BlockBreakListener(), this);
        plm.registerEvents(new BlockPlaceListener(), this);
        plm.registerEvents(new FriendlyFire(), this);
        plm.registerEvents(new ChatListener(), this);
        plm.registerEvents(new WeatherListener(), this);
        plm.registerEvents(new ArrowShootListener(), this);
        plm.registerEvents(new be.marijn2341.feroxcore.manager.inventorysettings.Listeners.InventoryClickListener(), this);
        plm.registerEvents(new InventoryDragListener(), this);
    }

    public void registerCommands() {
        getCommand("loadmaps").setExecutor(new LoadMapsCommand());
        getCommand("skipmap").setExecutor(new SkipGameCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("verify").setExecutor(new VerifyCommand());
        getCommand("startgame").setExecutor(new StartGameCommand());
        getCommand("setup").setExecutor(new SetupCommand());
        getCommand("statistics").setExecutor(new StatisticsCommand());
    }
}