package me.droreo002.skywars.manager.util;

import me.droreo002.skywars.MainPlugin;
import me.droreo002.skywars.manager.ConfigManager;
import me.droreo002.skywars.manager.Cuboid;

import java.lang.management.BufferPoolMXBean;
import java.util.HashMap;
import java.util.Map;

public class CuboidData {

    private Cuboid cub;
    private MainPlugin main;
    private static MainPlugin STATIC_MAIN;
    private String name;

    public CuboidData(Cuboid cub, MainPlugin main, String name) {
        this.name = name;
        this.main = main;
        this.cub = cub;
        save();
    }

    private void save() {
        ConfigManager con = main.getConfigManager();
        con.getConfig().set("Arenas." + name, cub);
        con.saveConfig();
        main.getLogger().info("Saved an arena with a name : " + name);
    }

    public static Cuboid loadData(String name) {
        return (Cuboid) STATIC_MAIN.getConfigManager().getConfig().get("Arenas." + name);
    }

}
