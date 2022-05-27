package be.marijn2341.feroxcore.database;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.manager.DataManager;
import be.marijn2341.feroxcore.manager.PlayerManager;
import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

    private static HikariDataSource dataSource;
    private Main main = Main.getInstance();

    public void initialize() {

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

    public HikariDataSource getHikari(){
        return dataSource;
    }



    public void updateKillsDB(UUID uuid, int kills) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET kills = kills + ? WHERE uuid=?")) {
                ps.setInt(1, kills);
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public int getKillsDB(UUID uuid) {
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

    public void updateDeathsDB(UUID uuid, int deaths) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET deaths = deaths + ? WHERE uuid=?")) {
            ps.setInt(1, deaths);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getDeathsDB(UUID uuid) {
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

    public void setPlaytimeDB(UUID uuid) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET playtime=? WHERE uuid=?")) {
                ps.setLong(1, main.getPlayerManager().getOnlineTimeMi(uuid));
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void setBrokenNexuses(UUID uuid, int aantal) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("UPDATE Stats SET nexusesbroken = nexusesbroken + ? WHERE uuid=?")) {
                ps.setInt(1, aantal);
                ps.setString(2, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public int getBrokenNexuses(UUID uuid) {
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

    public void setWins(UUID uuid, int aantal) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET wins = wins + ? WHERE uuid=?")) {
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getWins(UUID uuid) {
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

    public void setLoses(UUID uuid, int aantal) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET loses = loses + ? WHERE uuid=?")) {
            ps.setInt(1, aantal);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLoses(UUID uuid) {
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

    public boolean checkIfUserExists(UUID uuid) {
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

    public void insertNewUser(UUID uuid) {
            try (Connection con = getHikari().getConnection();
                 PreparedStatement ps = con.prepareStatement("INSERT INTO Stats SET uuid=?")) {
                ps.setString(1, uuid.toString());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void updateArrowsShotDB(UUID uuid, int shot) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET arrowsshot = arrowsshot + ? WHERE uuid=?")) {
            ps.setInt(1, shot);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateArrowsHitDB(UUID uuid, int hit) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET arrowshit = arrowshit + ? WHERE uuid=?")) {
            ps.setInt(1, hit);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertMapStats(String map, int blocksplaced, int blockbroken, long duration, String winner, int kills, int deaths, int arrowsshot, int arrowshit) {
        String redm = new Gson().toJson(main.getDataManager().getTeamRed());
        String bluem = new Gson().toJson(main.getDataManager().getTeamBlue());
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

    public boolean inventoryPlayerExists(Player player) {
        try (Connection con = getHikari().getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT * FROM InventorySettings WHERE uuid=?")) {
            ps.setString(1, String.valueOf(player.getUniqueId()));
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

    public String getInventory(Player player) {

        String inv = null;
        try (Connection con = getHikari().getConnection();
        PreparedStatement ps = con.prepareStatement("SELECT inventory FROM InventorySettings WHERE uuid=?")) {
        ps.setString(1, String.valueOf(player.getUniqueId()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                inv = rs.getString("inventory");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inv;
    }

    public void updateInventory(Player player, String obj) {
        try (Connection con = getHikari().getConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE InventorySettings SET inventory=? WHERE uuid=?")) {
            ps.setString(1, obj);
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setInventory(Player player, String obj) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO InventorySettings SET inventory=?, uuid=?")) {
            ps.setString(1, obj);
            ps.setString(2, String.valueOf(player.getUniqueId()));
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLevel(UUID uuid, int level) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET level=? WHERE uuid=?")) {
            ps.setInt(1, level);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLevel(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT level FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("level");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public void setXP(UUID uuid, int xp) {
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE Stats SET xp=? WHERE uuid=?")) {
            ps.setInt(1, xp);
            ps.setString(2, uuid.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getXP(UUID uuid) {
        int i = 0;
        try (Connection con = getHikari().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT xp FROM Stats WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt("xp");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
}
