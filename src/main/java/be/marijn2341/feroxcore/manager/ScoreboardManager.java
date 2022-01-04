package be.marijn2341.feroxcore.manager;

import be.marijn2341.feroxcore.database.Database;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardManager {

    public static void getGameScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager sb = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sb.getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("game", "dummy");
        obj.setDisplayName(Utils.color("&9&lFerox&f&lMC"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);


            if (NexusManager.REDNEXUSESLOCTOTAL.size() >= 4) {
                obj.getScore(Utils.color("&cRed:")).setScore(4);
                obj.getScore(Utils.color("&7" + NexusManager.REDNEXUSESLOC.size() + "&8/&7" + NexusManager.REDNEXUSESLOCTOTAL.size())).setScore(3);
            }
            if (NexusManager.BLUENEXUSESLOCTOTAL.size() >= 4) {
                obj.getScore(Utils.color("&9Blue:")).setScore(6);
                obj.getScore(Utils.color("&7" + NexusManager.REDNEXUSESLOC.size() + "&8/&7" + NexusManager.REDNEXUSESLOCTOTAL.size())).setScore(5);
            }

            int i = 3;
            int ib = 3;

        if (NexusManager.REDNEXUSESLOCTOTAL.size() < 4 && NexusManager.BLUENEXUSESLOCTOTAL.size() < 4) {
            //SCOREBOARD DAT TEAM ROOD ZIET
            if (TeamManager.TEAMRED.contains(player.getUniqueId())) {
                for (String nexus : NexusManager.REDNEXUSESLOCTOTAL.keySet()) {
                    obj.getScore(Utils.color("&7" + nexus + ": " + (NexusManager.nexusAlive("red", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(ib);
                    ib++;
                }

                obj.getScore(Utils.color("&c&lRed:")).setScore(ib);
                ib++;
                obj.getScore(Utils.color("&r")).setScore(ib);
                ib++;

                for (String nexus : NexusManager.BLUENEXUSESLOCTOTAL.keySet()) {
                    if (nexus.equalsIgnoreCase("left")) {
                        String nieuw = nexus.replace("Left", "Right");
                        obj.getScore(Utils.color("&r&7" + nieuw + ": " + (NexusManager.nexusAlive("blue", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(ib);
                    } else if (nexus.equalsIgnoreCase("right")) {
                        String nieuw = nexus.replace("Right", "Left");
                        obj.getScore(Utils.color("&r&7" + nieuw + ": " + (NexusManager.nexusAlive("blue", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(ib);
                    } else {
                        obj.getScore(Utils.color("&r&7" + nexus + ": " + (NexusManager.nexusAlive("blue", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(ib);
                    }
                    ib++;
                }

                obj.getScore(Utils.color("&9&lBlue:")).setScore(ib);
                ib++;
                obj.getScore(Utils.color("&6")).setScore(ib);

                ib = 3;
            } else {
                // SCOREBOARD DAT TEAM BLAUW ZIET.
                for (String nexus : NexusManager.REDNEXUSESLOCTOTAL.keySet()) {
                    if (nexus.equalsIgnoreCase("left")) {
                        String nieuw = nexus.replace("Left", "Right");
                        obj.getScore(Utils.color("&r&7" + nieuw + ": " + (NexusManager.nexusAlive("red", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(i);
                    } else if (nexus.equalsIgnoreCase("right")) {
                        String nieuw = nexus.replace("Right", "Left");
                        obj.getScore(Utils.color("&r&7" + nieuw + ": " + (NexusManager.nexusAlive("red", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(i);
                    } else {
                        obj.getScore(Utils.color("&r&7" + nexus + ": " + (NexusManager.nexusAlive("red", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(i);
                    }
                    i++;
                }

                obj.getScore(Utils.color("&c&lRed:")).setScore(i);
                i++;
                obj.getScore(Utils.color("&r &l")).setScore(i);
                i++;

                for (String nexus : NexusManager.BLUENEXUSESLOCTOTAL.keySet()) {
                    obj.getScore(Utils.color("&7" + nexus + ": " + (NexusManager.nexusAlive("blue", nexus) ? Utils.color("&a✔") : Utils.color("&c✘")))).setScore(i);
                    i++;
                }

                obj.getScore(Utils.color("&9&lBlue:")).setScore(i);
                i++;
                obj.getScore(Utils.color("&6")).setScore(i);

                i = 3;
            }
        }

        obj.getScore("").setScore(2);
        obj.getScore(Utils.color("&7pvp.feroxhosting.nl")).setScore(1);

        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboardINGAME() {

        TeamManager.TEAMRED.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            getGameScoreboard(player);
        });
        TeamManager.TEAMBLUE.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            getGameScoreboard(player);
        });
        TeamManager.SPECTATORS.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            getGameScoreboard(player);
        });

    }

    public static void getLobbyScoreboard(Player player) {

        org.bukkit.scoreboard.ScoreboardManager sb = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = sb.getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("lobby", "dummy");
        obj.setDisplayName(Utils.color("&9&lFerox&f&lMC"));
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore(" ").setScore(7);
        obj.getScore(Utils.color("&9Player: &f" + player.getName())).setScore(6);
        obj.getScore(Utils.color("&9Kills: &f" + Database.getKillsDB(player.getUniqueId()))).setScore(5);
        obj.getScore(Utils.color("&9Deaths: &f") + Database.getDeathsDB(player.getUniqueId())).setScore(4);
        obj.getScore(Utils.color("&9Playtime: &f" + PlayerManager.getOnlineTime(player))).setScore(3);
        obj.getScore("").setScore(2);
        obj.getScore(Utils.color("&7pvp.feroxhosting.nl")).setScore(1);


        player.setScoreboard(scoreboard);
    }
}
