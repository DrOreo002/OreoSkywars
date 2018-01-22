package me.droreo002.skywars.manager;

import me.droreo002.skywars.MainPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class EditorManager {

    private HashMap<UUID, Arena> on_setup = new HashMap<>();
    private HashMap<String, HashMap<Location, Material>> pBlocks = new HashMap<>();
    private MainPlugin main;
    private static EditorManager instance;

    private EditorManager(MainPlugin main) {
        this.main = main;
    }

    public static EditorManager getInstance(MainPlugin main) {
        if (instance == null) {
            instance = new EditorManager(main);
            return instance;
        }
        return instance;
    }

    public void addOnSetup(Player player, Arena arena) {
        if (!on_setup.containsKey(player.getUniqueId())) {
            on_setup.put(player.getUniqueId(), arena);
            return;
        }
        player.sendMessage(main.getPrefix() + "Already entered setup mode on : " + arena.getName() + ". Arena!");
    }

    public boolean isOnSetup(Player player) {
        return (on_setup.containsKey(player.getUniqueId()));
    }

    public void showCorner(Cuboid cub, Player player) {
        HashSet<Location> locs = getHollowCube(cub, cub.getLoc1(), cub.getLoc2(), 0.2);
        for (Location l : locs) {
            cub.getWorld().spawnParticle(Particle.CLOUD, l, 4);
        }
        /*
        Bukkit.getScheduler().runTaskLater(main, () -> {

            player.sendMessage(main.getPrefix() + "Removing all of the particles!");
        }, 100L);
        */
    }

    private HashSet<Location> getHollowCube(Cuboid cub, Location corner1, Location corner2, double particleDistance) {
        HashSet<Location> result = new HashSet<>();
        World world = cub.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x+=particleDistance) {
            for (double y = minY; y <= maxY; y+=particleDistance) {
                for (double z = minZ; z <= maxZ; z+=particleDistance) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        result.add(new Location(world, x, y, z));
                    }
                }
            }
        }
        return result;
    }
}
