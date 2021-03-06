package be.marijn2341.feroxcore.database;

import be.marijn2341.feroxcore.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegistrationDatabase {

    private final Main main = Main.getInstance();

    public boolean playerExists(UUID uuid) {
        try (Connection con = main.getDb().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT * FROM RegistrationCodes WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                results.close();
                //PLAYER FOUND
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean allreadyRegistered(UUID uuid) {
        try (Connection con = main.getDb().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT discordid FROM Stats WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                if (results.getString("discordid") == null) {
                    results.close();
                    return false;
                } else {
                    results.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getRegistrationCode(UUID uuid) {
        try (Connection con = main.getDb().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT code FROM RegistrationCodes WHERE uuid=?")) {
            statement.setString(1, uuid.toString());
            ResultSet results = statement.executeQuery();
            String string = null;
            if (results.next()) {
                string = results.getString("code");
            }
            results.close();
            return string;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkIfCodeExists(String code) {
        try (Connection con = main.getDb().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("SELECT 1 FROM RegistrationCodes WHERE code=?")) {
            statement.setString(1, code);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                results.close();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertCode(UUID uuid, String code) {
        try (Connection con = main.getDb().getHikari().getConnection();
             PreparedStatement statement = con.prepareStatement("INSERT INTO RegistrationCodes SET uuid=?, code=?")) {
            statement.setString(1, uuid.toString());
            statement.setString(2, code);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
