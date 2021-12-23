package be.marijn2341.feroxcore.Database;

import be.marijn2341.feroxcore.Main;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegistrationDatabase {

    public static boolean playerExists(UUID uuid) {
        try {
            PreparedStatement statement = Main.getInstance().SQL.getConnection().prepareStatement("SELECT * FROM RegistrationCodes WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                //PLAYER FOUND
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean AllreadyRegistered(UUID uuid) {
        try {
            PreparedStatement statement = Main.getInstance().SQL.getConnection().prepareStatement("SELECT discordid FROM Stats WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                if (results.getString("discordid") == null) {
                    return false;
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String GetRegistrationCode(UUID uuid) {
        try {
            PreparedStatement statement = Main.getInstance().SQL.getConnection().prepareStatement("SELECT code FROM RegistrationCodes WHERE uuid=?");
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return results.getString("code");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean CheckIfCodeExists(String code) {
        try {
            PreparedStatement statement = Main.getInstance().SQL.getConnection().prepareStatement("SELECT 1 FROM Stats WHERE code=?");
            statement.setString(1, code);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void insertCode(UUID uuid, String code) {
        try {
            PreparedStatement statement = Main.getInstance().SQL.getConnection().prepareStatement("INSERT INTO RegistrationCodes SET uuid=?, code=?");
            statement.setString(1, uuid.toString());
            statement.setString(2, code);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
