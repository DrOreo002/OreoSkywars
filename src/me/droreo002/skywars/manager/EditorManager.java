package me.droreo002.skywars.manager;

import me.droreo002.skywars.MainPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class EditorManager {

    private HashMap<UUID, Arena> on_setup = new HashMap<>();
    private HashMap<Cuboid, List<Block>> showed_corners = new HashMap<>();
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
        List<Block> blocks = cub.corners();
        List<Material> saved = new ArrayList<>();
        if (!showed_corners.containsKey(cub)) {
            showed_corners.put(cub, blocks);
        }
        for (Block b : blocks) {
            player.sendMessage(b.toString());
            saved.add(b.getType());
        }
        for (Block b : blocks) {
            player.sendMessage(main.getPrefix() + "Replaced corner with diamond block!. Block Material : " + b.getType().toString());
            b.setType(Material.DIAMOND_BLOCK);
        }
        Bukkit.getScheduler().runTaskLater(main, () -> {
            for (Material mat : saved) {
                for (Block b : blocks) {
                    b.setType(mat);
                }
            }
            player.sendMessage(main.getPrefix() + "Replaced back the showed corner!");
        }, 100L);
    }
}
