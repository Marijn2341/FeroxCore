package be.marijn2341.feroxcore.commands.staff;

import be.marijn2341.feroxcore.Main;
import be.marijn2341.feroxcore.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class SetupCommand implements CommandExecutor {

    public static HashMap<UUID, World> SETUPMODE = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DecimalFormat df = new DecimalFormat("#.##");

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("ferox.setup")) {
                if (args.length == 0) {
                    player.sendMessage(Utils.color("&6--- Ferox Setup ---"));
                    player.sendMessage(Utils.color("&6/setup lobby setspawn &7| NO SETUP MODE NEEDED!"));
                    player.sendMessage(Utils.color("&6/setup enable &7| Enable the setup function in your current world."));
                    player.sendMessage(Utils.color("&6/setup nexus remove <team> <nexusname> &7| Remove a nexus."));
                    player.sendMessage(Utils.color("&6/setup nexus add <team> <nexusname> &7| Look at the nexus you want to add."));
                    player.sendMessage(Utils.color("&6/setup setspawn <team> &7| Choose between the teams red or blue."));
                    player.sendMessage(Utils.color("&6/setup save &7| Save all the changes, setup mode will be turned off after saving."));
                    player.sendMessage(Utils.color("&6/setup exit &7| Leaving the setup without saving can delete your setup!"));
                    player.sendMessage(Utils.color("&6-------------------"));
                    return false;
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("enable")) {
                        if (!(SETUPMODE.containsKey(player.getUniqueId()))) {
                            SETUPMODE.put(player.getUniqueId(), player.getLocation().getWorld());
                            player.sendMessage(Utils.color("&6You enabled setup mode for the world &7" + player.getLocation().getWorld().getName() + "&6."));
                        } else {
                            player.sendMessage(Utils.color("&cYou are already in setup mode."));
                            return false;
                        }
                    }
                }

                if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("lobby")) {
                        if (args[1].equalsIgnoreCase("setspawn")) {
                            Location loc = player.getLocation();

                            Main.getInstance().getWorldsConfig().set("lobby.world", loc.getWorld().getName());
                            Main.getInstance().getWorldsConfig().set("lobby.x", Double.valueOf(df.format(loc.getX())));
                            Main.getInstance().getWorldsConfig().set("lobby.y", Double.valueOf(df.format(loc.getY())));
                            Main.getInstance().getWorldsConfig().set("lobby.z", Double.valueOf(df.format(loc.getZ())));
                            Main.getInstance().getWorldsConfig().set("lobby.yaw", Double.valueOf(df.format(loc.getYaw())));
                            Main.getInstance().getWorldsConfig().set("lobby.pitch", Double.valueOf(df.format(loc.getPitch())));
                            try {
                                Main.getInstance().getWorldsConfig().save(Main.getInstance().getWorldsFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                                player.sendMessage(Utils.color("&cSomething went wrong while saving, Contact the developer."));
                                return false;
                            }
                            player.sendMessage(Utils.color("&6Lobby spawnpoint: &7" + loc.getWorld().getName() + ": " + df.format(loc.getX()) + "x, " + df.format(loc.getY()) + "y, " +
                                    df.format(loc.getZ()) + "z, " + df.format(loc.getYaw()) + " yaw, " + df.format(loc.getPitch()) + " pitch"));
                            return true;
                        }
                    }
                }
                    if (SETUPMODE.containsKey(player.getUniqueId())) {
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase("save")) {
                                if (SETUPMODE.containsKey(player.getUniqueId())) {
                                    try {
                                        Main.getInstance().getWorldsConfig().save(Main.getInstance().getWorldsFile());
                                        SETUPMODE.remove(player.getUniqueId());
                                        player.sendMessage(Utils.color("&6You successfully saved the setup, You can load the map with /loadmaps."));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        player.sendMessage(Utils.color("&cSomething went wrong while saving, Contact the developer."));
                                    }
                                } else {
                                    player.sendMessage(Utils.color("&cYou are not in setup mode."));
                                    return false;
                                }
                            } else if (args[0].equalsIgnoreCase("exit")) {
                                if (SETUPMODE.containsKey(player.getUniqueId())) {
                                    if (Main.getInstance().getWorldsConfig().contains("worlds." + SETUPMODE.get(player.getUniqueId()).getName())) {
                                        Main.getInstance().getWorldsConfig().set("worlds." + SETUPMODE.get(player.getUniqueId()).getName(), null);
                                        SETUPMODE.remove(player.getUniqueId());
                                        player.sendMessage(Utils.color("&6Successfully exited setup mode."));
                                        return true;
                                    } else {
                                        SETUPMODE.remove(player.getUniqueId());
                                        player.sendMessage(Utils.color("&6Successfully exited setup mode."));
                                        return true;
                                    }
                                }
                            }
                        }
                    if (args.length == 2) {
                        if (args[0].equalsIgnoreCase("setspawn")) {
                            String wereld = SETUPMODE.get(player.getUniqueId()).getName();
                            Location loc = player.getLocation();
                            if (args[1].equalsIgnoreCase("red")) {
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.red.x", Double.valueOf(df.format(loc.getX())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.red.y", Double.valueOf(df.format(loc.getY())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.red.z", Double.valueOf(df.format(loc.getZ())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.red.yaw", Double.valueOf(df.format(loc.getYaw())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.red.pitch", Double.valueOf(df.format(loc.getPitch())));;

                                player.sendMessage(Utils.color("&6Red spawnpoint: &7" + df.format(loc.getX()) + "x, " + df.format(loc.getY()) + "y, " +
                                        df.format(loc.getZ()) + "z, " + df.format(loc.getYaw()) + " yaw, " + df.format(loc.getPitch()) + " pitch"));

                            } else if (args[1].equalsIgnoreCase("blue")) {
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.blue.x", Double.valueOf(df.format(loc.getX())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.blue.y", Double.valueOf(df.format(loc.getY())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.blue.z", Double.valueOf(df.format(loc.getZ())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.blue.yaw", Double.valueOf(df.format(loc.getYaw())));
                                Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".spawnpoints.blue.pitch", Double.valueOf(df.format(loc.getPitch())));

                                player.sendMessage(Utils.color("&6Blue spawnpoint: &7" + df.format(loc.getX()) + "x, " + df.format(loc.getY()) + "y, " +
                                        df.format(loc.getZ()) + "z, " + df.format(loc.getYaw()) + " yaw, " + df.format(loc.getPitch()) + " pitch"));
                            } else {
                                player.sendMessage(Utils.color("&cYou can only choose between red or blue."));
                            }
                        }
                    }

                    if (args.length == 4) {
                        if (args[0].equalsIgnoreCase("nexus")) {
                            if (args[1].equalsIgnoreCase("add")) {
                                if (args[2] != null && args[3] !=null) {
                                    Block block = player.getTargetBlock((Set<Material>) null, 5);
                                    if (block.getType() == Material.OBSIDIAN) {
                                        String team = args[2].toLowerCase(Locale.ROOT);
                                        if (!(team.equalsIgnoreCase("red") || team.equalsIgnoreCase("blue"))) {
                                            player.sendMessage(Utils.color("&cYou need to provide a valid team. Red or Blue."));
                                            return false;
                                        }
                                        String nexus = args[3];
                                        String wereld = SETUPMODE.get(player.getUniqueId()).getName();
                                        Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".nexuses." + team + "." +  nexus + ".x", block.getX());
                                        Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".nexuses." + team + "." + nexus + ".y", block.getY());
                                        Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".nexuses." + team + "." + nexus + ".z", block.getZ());
                                        player.sendMessage(Utils.color("&6Nexus &7" + nexus + " &6added for team &7" + team + "&6: &7" + block.getX() + "x, " + block.getY() + "y, " + block.getZ() + "z."));
                                        return true;
                                    } else {
                                        player.sendMessage(Utils.color("&cA nexus need to be a obsidian block!"));
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(Utils.color("&cNo nexus name given...."));
                                    return false;
                                }
                            } else if (args[1].equalsIgnoreCase("remove")) {
                                if (args[2] != null && args[3] != null) {
                                    String team = args[2].toLowerCase(Locale.ROOT);
                                    if (!(team.equalsIgnoreCase("red") || team.equalsIgnoreCase("blue"))) {
                                        player.sendMessage(Utils.color("&cYou need to provide a valid team. Red or Blue."));
                                        return false;
                                    }
                                    String wereld = SETUPMODE.get(player.getUniqueId()).getName();
                                    if (Main.getInstance().getWorldsConfig().contains("worlds." + wereld + ".nexuses." + team + "." + args[3])) {
                                        Main.getInstance().getWorldsConfig().set("worlds." + wereld + ".nexuses." + team + "." + args[3], null);
                                        player.sendMessage(Utils.color("&6Deleted " + args[3] + ", use /setup save to save this modification."));
                                        return true;
                                    }
                                } else {
                                    player.sendMessage(Utils.color("&cNo nexus name given...."));
                                    return false;
                                }
                            }
                        }
                    }
                } else {
                    player.sendMessage(Utils.color("&cYou are not in setup mode."));
                    return false;
                }
            } else {
                Utils.noPermission(player);
            }
        }
        return false;
    }
}
