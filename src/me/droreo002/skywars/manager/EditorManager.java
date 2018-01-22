package me.droreo002.skywars.manager;

import me.droreo002.skywars.MainPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class EditorManager {

    private HashMap<UUID, Arena> on_setup = new HashMap<>();
    private HashMap<UUID, Integer> taskId = new HashMap<>();
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
        HashSet<Location> locs = getHollowCube(cub, cub.getLoc1(), cub.getLoc2());
        if (taskId.containsKey(player.getUniqueId())) {
            Bukkit.getScheduler().cancelTask(taskId.get(player.getUniqueId()));
            player.sendMessage(main.getPrefix() + "Now clearing all of the particles");
            taskId.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(main.getPrefix() + "Showed the border with particles. Run this command again to stop it!");
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
            for (Location l : locs) {
                cub.getWorld().spawnParticle(Particle.SMOKE_LARGE, l, 0);
            }
        }, 0L, 20L);
        if (!taskId.containsKey(player.getUniqueId())) {
            taskId.put(player.getUniqueId(), id);
        }
    }

    private HashSet<Location> getHollowCube(Cuboid cub, Location corner1, Location corner2) {
        HashSet<Location> result = new HashSet<>();
        World world = cub.getWorld();
        int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
        int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
        int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
        int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
        int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
        int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());

        // 2 areas
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                result.add(new Location(world, x, minY, z));
                result.add(new Location(world, x, maxY, z));
            }
        }

        // 2 sides (front & back)
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                result.add(new Location(world, x, y, minZ));
                result.add(new Location(world, x, y, maxZ));
            }
        }

        // 2 sides (left & right)
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                result.add(new Location(world, minX, y, z));
                result.add(new Location(world, maxX, y, z));
            }
        }

        return result;
    }
}
