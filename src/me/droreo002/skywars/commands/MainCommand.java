package me.droreo002.skywars.commands;

import me.droreo002.skywars.MainPlugin;
import me.droreo002.skywars.manager.Arena;
import me.droreo002.skywars.manager.Cuboid;
import me.droreo002.skywars.manager.util.ArenaStatus;
import me.droreo002.skywars.manager.util.PlayerStatus;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import sun.management.Sensor;

import java.util.HashMap;

public class MainCommand implements CommandExecutor {

    private MainPlugin main;

    public MainCommand(MainPlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getPrefix() + "Player command only. Console command is not available right now. Sorry");
            return false;
        }

        Player player = (Player) sender;

        if (args.length > 0) {
            if (args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "help":
                        player.sendMessage(main.getPrefix() + "This is help command!");
                        return false;
                    case "finish":
                        if (main.on_setup.containsKey(player.getUniqueId())) {
                            player.sendMessage(main.getPrefix() + "You're now leaved the Setup Mode!");
                            main.on_setup.remove(player.getUniqueId());
                            return false;
                        }
                        return false;
                        /*
                    case "setup":
                        if (!player.hasPermission("osw.admin")) {
                            return false;
                        }
                        player.sendMessage(main.getPrefix() + "You're now creating an Arena. Please use /osw helpsetup to see available commands!");
                        if (!main.on_setup.contains(player.getUniqueId())) {
                            main.on_setup.add(player.getUniqueId());
                        } else {
                            player.sendMessage(main.getPrefix() + "Already entered the setup mode.");
                            return false;
                        }
                        return false;
                        */
                    case "pos1":
                        if (!player.hasPermission("osw.admin")) {
                            return false;
                        }
                        if (main.pos1.containsKey(player.getUniqueId())) {
                            player.sendMessage(main.getPrefix() + "Pos1 already setted!");
                            return false;
                        }
                        main.pos1.put(player.getUniqueId(), player.getLocation());
                        player.sendMessage(main.getPrefix() + "Pos1 set on : " + player.getLocation().toString());
                        return false;
                    case "pos2":
                        if (!player.hasPermission("osw.admin")) {
                            return false;
                        }
                        if (main.pos2.containsKey(player.getUniqueId())) {
                            player.sendMessage(main.getPrefix() + "Pos2 already setted!");
                            return false;
                        }
                        main.pos2.put(player.getUniqueId(), player.getLocation());
                        player.sendMessage(main.getPrefix() + "Pos2 set on : " + player.getLocation().toString());
                        return false;
                    default:
                        player.sendMessage(main.getPrefix() + "Cannot find that command. Is it right?");
                        return false;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (!player.hasPermission("osw.admin")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    if (!main.pos1.containsKey(player.getUniqueId()) || !main.pos2.containsKey(player.getUniqueId())) {
                        player.sendMessage(main.getPrefix() + "Please select the corner first!");
                        return false;
                    }
                    if (main.on_setup.containsKey(player.getUniqueId())) {
                        player.sendMessage(main.getPrefix() + "Please complete this arena first.");
                        return false;
                    }
                    String name = args[1];
                    ConfigurationSection cs = main.getConfig().getConfigurationSection("ArenaData");
                    if (cs == null) {
                        player.sendMessage(main.getPrefix() + "Created an arena with the name : " + name + ". Now entering Edit Mode!");
                        Arena arena = new Arena(name, main.pos1.get(player.getUniqueId()), main.pos2.get(player.getUniqueId()), main, ArenaStatus.WAITING, 2);
                        main.on_setup.put(player.getUniqueId(), arena);
                        main.getGameManager().addArena(name, arena);
                        return false;
                    }
                    if (cs.getKeys(false).contains(name) && !cs.getKeys(false).isEmpty()) {
                        player.sendMessage(main.getPrefix() + "That name already exist!");
                        return false;
                    }
                    player.sendMessage(main.getPrefix() + "Created an arena with the name : " + name + ". Now entering Edit Mode!");
                    Arena arena = new Arena(name, main.pos1.get(player.getUniqueId()), main.pos2.get(player.getUniqueId()), main, ArenaStatus.WAITING, 2);
                    main.getGameManager().addArena(name, arena);
                    main.on_setup.put(player.getUniqueId(), arena);
                    return false;
                }
                if (args[0].equalsIgnoreCase("setspawn")) {
                    if (!player.hasPermission("osw.admin")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    if (main.on_setup.containsKey( player.getUniqueId())) {
                        player.sendMessage(main.getPrefix() + "Please complete this arena first.");
                        return false;
                    }
                    int number = Integer.parseInt(args[1]);
                    Arena arena = main.on_setup.get(player.getUniqueId());
                    if (arena == null) {
                        player.sendMessage(main.getPrefix() + "Arena is NULL!!");
                        return false;
                    }
                    if (!arena.hasThisPlayer(player)) {
                        player.sendMessage(main.getPrefix() + "Please stand inside the Arena's Cuboid!");
                        return false;
                    }
                    arena.saveSpawn(number, player.getLocation());
                    player.sendMessage(main.getPrefix() + "Saved the spawn location number : " + number + ". To your location!");
                    return false;
                }
                if (args[0].equalsIgnoreCase("edit")) {
                    if (!player.hasPermission("osw.admin")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    if (main.on_setup.containsKey(player.getUniqueId())) {
                        Arena arena = main.on_setup.get(player.getUniqueId());
                        player.sendMessage(main.getPrefix() + "You're still on the edit mode on : " + arena.getName() + ". Arena!, please use /osw finish before using this command again!");
                        return false;
                    }
                    String name = args[1];
                    Arena arena = main.getGameManager().getArena(name);
                    main.on_setup.put(player.getUniqueId(), arena);
                    player.sendMessage(main.getPrefix() + "Now editing : " + arena.getName());
                    return false;
                }
                if (args[0].equalsIgnoreCase("join")) {
                    if (!player.hasPermission("osw.player.join")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    if (main.on_setup.containsKey(player.getUniqueId())) {
                        player.sendMessage(main.getPrefix() + "You cannot enter an arena while on Setup mode!");
                        return false;
                    }
                    String name = args[1];

                }
                if (args[0].equalsIgnoreCase("teleport")) {
                    if (!player.hasPermission("osw.admin")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    String name = args[1];
                    Arena arena = main.getGameManager().getArena(name);
                    if (arena == null) {
                        player.sendMessage(main.getPrefix() + "Error. cannot find that arena!");
                        return false;
                    }
                    Location loc = arena.getCuboid().getCenter();
                    player.teleport(loc);
                    player.sendMessage(main.getPrefix() + "Teleported you to the center of : " +arena.getName()+". Arena!");
                    return false;
                }
                if (args[0].equalsIgnoreCase("border")) {
                    if (!player.hasPermission("osw.admin")) {
                        player.sendMessage(main.getPrefix() + "No perms");
                        return false;
                    }
                    String name = args[1];
                    Arena arena = main.getGameManager().getArena(name);
                    if (arena == null) {
                        player.sendMessage(main.getPrefix() + "Error. cannot find that arena!");
                        return false;
                    }
                    Cuboid cub = arena.getCuboid();
                    main.getEditorManager().showCorner(cub, player);
                    return false;
                }
            }

        } else {
            player.sendMessage(main.getPrefix() + "Plugin made by DrOreo002");
            return false;
        }
        return false;
    }
}
