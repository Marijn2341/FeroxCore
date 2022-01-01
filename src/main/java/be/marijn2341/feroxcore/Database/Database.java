package be.marijn2341.feroxcore.Database;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import be.marijn2341.feroxcore.Manager.TeamManager;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.*;
import java.util.UUID;

public class Database {

    private static String host;
    private static String database;
    private static String username;
    private static String password;
    private static int port;

    private static HikariDataSource dataSource;

    public static void initialize() {

        host = Main.getInstance().getConfig().getString("MySQL.Host");
        port = Main.getInstance().getConfig().getInt("MySQL.Port");
        database = Main.getInstance().getConfig().getString("MySQL.Database");
        username = Main.getInstance().getConfig().getString("MySQL.Username");
        password = Main.getInstance().getConfig().getString("MySQL.Password");


        HikariConfig config = new HikariConfig();;
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/"
                + database + "?autoReconnect=true");
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MySQL Connected");
    }

    public static HikariDataSource getHikari(){
        return dataSource;
    }



    public static void UpdateKillsDB(UUID uuid, int kills) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET kills = kills + ? WHERE uuid=?")) {
                ps.setInt(1, kills);
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static int GetKillsDB(UUID uuid) {
            int i = 0;
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT kills FROM Stats WHERE uuid=?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    i = rs.getInt("kills");
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return i;
        }

    public static void UpdateDeathsDB(UUID uuid, int deaths) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET deaths = deaths + ? WHERE uuid=?")) {
            ps.setInt(1, deaths);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetDeathsDB(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT deaths FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("deaths");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static void SetPlaytimeDB(UUID uuid) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET playtime=? WHERE uuid=?")) {
                ps.setLong(1, PlayerManager.GetOnlineTimeMi(uuid));
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void SetBrokenNexuses(UUID uuid, int aantal) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET nexusesbroken = nexusesbroken + ? WHERE uuid=?")) {
                ps.setInt(1, aantal);
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static int GetBrokenNexuses(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT nexusesbroken FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("nexusesbroken");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static void SetWins(UUID uuid, int aantal) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET wins = wins + ? WHERE uuid=?")) {
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetWins(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT wins FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("wins");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static void SetLoses(UUID uuid, int aantal) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET loses = loses + ? WHERE uuid=?")) {
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetLoses(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT loses FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("loses");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public static boolean CheckIfUserExists(UUID uuid) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM Stats WHERE uuid=?")) {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    rs.close();
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }

    public static void InsertNewUser(UUID uuid) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO Stats SET uuid=?")) {
                ps.setString(1, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void UpdateArrowsShotDB(UUID uuid, int shot) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET arrowsshot = arrowsshot + ? WHERE uuid=?")) {
            ps.setInt(1, shot);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void UpdateArrowsHitDB(UUID uuid, int hit) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET arrowshit = arrowshit + ? WHERE uuid=?")) {
            ps.setInt(1, hit);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void InsertMapStats(String map, int blocksplaced, int blockbroken, long duration, String winner, int kills, int deaths, int arrowsshot, int arrowshit) {
        String redm = new Gson().toJson(TeamManager.TeamRed);
        String bluem = new Gson().toJson(TeamManager.TeamBlue);
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO GameStats SET gamemap=?, totalkills=?, totaldeaths=?, totalarrowsshot=?, totalarrowshit=?, blocksplaced=?, blocksbroken=?, matchduration=?, winner=?, teambluemembers=?, teamredmembers=?")) {
            ps.setString(1, map);
            ps.setInt(2, kills);
            ps.setInt(3, deaths);
            ps.setInt(4, arrowsshot);
            ps.setInt(5, arrowshit);
            ps.setInt(6, blocksplaced);
            ps.setInt(7, blockbroken);
            ps.setLong(8, duration);
            ps.setString(9, winner);
            ps.setString(10, bluem);
            ps.setString(11, redm);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
