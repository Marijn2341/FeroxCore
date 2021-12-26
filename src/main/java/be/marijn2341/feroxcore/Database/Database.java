package be.marijn2341.feroxcore.Database;

import be.marijn2341.feroxcore.Listeners.ArrowShootListener;
import be.marijn2341.feroxcore.Listeners.DeathListener;
import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.Manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class Database {

    private String host;
    private String database;
    private String username;
    private String password;
    private int port;

    private Connection connection;

        public boolean isConnected() {
            return (connection == null ? false : true);
        }

        public void connect() throws ClassNotFoundException, SQLException {
            host = Main.getInstance().getConfig().getString("MySQL.Host");
            port = Main.getInstance().getConfig().getInt("MySQL.Port");
            database = Main.getInstance().getConfig().getString("MySQL.Database");
            username = Main.getInstance().getConfig().getString("MySQL.Username");
            password = Main.getInstance().getConfig().getString("MySQL.Password");

            if (!isConnected()) {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                host + ":" + port + "/" + database + "?useSSL=false",
                        username, password);
            }
        }

        public void disconnect() {
            if (isConnected()) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public Connection getConnection() {
            return connection;
        }

        public static void UpdateKillsDB(UUID uuid, int kills) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET kills = kills + ? WHERE uuid=?");
                ps.setInt(1, kills);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public static int GetKillsDB(UUID uuid) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT kills FROM Stats WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("kills");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

    public static void UpdateDeathsDB(UUID uuid, int deaths) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET deaths = deaths + ? WHERE uuid=?");
            ps.setInt(1, deaths);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetDeathsDB(UUID uuid) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT deaths FROM Stats WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("deaths");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void SetPlaytimeDB(UUID uuid) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET playtime=? WHERE uuid=?");
                ps.setLong(1, PlayerManager.GetOnlineTimeMi(uuid));
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void SetBrokenNexuses(UUID uuid, int aantal) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET nexusesbroken = nexusesbroken + ? WHERE uuid=?");
                ps.setInt(1, aantal);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static int GetBrokenNexuses(UUID uuid) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT nexusesbroken FROM Stats WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("nexusesbroken");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void SetWins(UUID uuid, int aantal) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET wins = wins + ? WHERE uuid=?");
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetWins(UUID uuid) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT wins FROM Stats WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("wins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void SetLoses(UUID uuid, int aantal) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET loses = loses + ? WHERE uuid=?");
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int GetLoses(UUID uuid) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT loses FROM Stats WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("loses");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean CheckIfUserExists(UUID uuid) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("SELECT * FROM Stats WHERE uuid=?");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }

    public static void InsertNewUser(UUID uuid) {
            try {
                PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("INSERT INTO Stats SET uuid=?");
                ps.setString(1, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void UpdateArrowsShotDB(UUID uuid, int shot) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET arrowsshot = arrowsshot + ? WHERE uuid=?");
            ps.setInt(1, shot);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void UpdateArrowsHitDB(UUID uuid, int hit) {
        try {
            PreparedStatement ps = Main.getInstance().SQL.getConnection().prepareStatement("UPDATE Stats SET arrowshit = arrowshit + ? WHERE uuid=?");
            ps.setInt(1, hit);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
