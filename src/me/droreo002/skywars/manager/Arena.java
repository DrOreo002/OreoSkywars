package me.droreo002.skywars.manager;

import me.droreo002.skywars.MainPlugin;
import me.droreo002.skywars.manager.util.ArenaStatus;
import me.droreo002.skywars.manager.util.ArenaType;
import me.droreo002.skywars.manager.util.CuboidData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class Arena {

    private Cuboid cub;
    private MainPlugin main;
    private String name;
    private HashMap<Integer, Location> spawns;
    private Location location1;
    private Location location2;
    private ArenaStatus status;
    private int spawnValue;
    private ArenaType type;

    public Arena(String name, Location loc1, Location loc2, MainPlugin main, ArenaStatus status, int spawnValue, String type) {
        switch (type.toLowerCase()) {
            case "solo":
                this.type = ArenaType.SOLO;
                break;
            case "team":
                this.type = ArenaType.TEAM;
                break;
            default:
                this.type = ArenaType.SOLO;
                break;
        }
        this.spawnValue = spawnValue;
        this.location1 = loc1;
        this.location2 = loc2;
        this.name = name;
        this.main = main;
        this.status = status;
        this.cub = new Cuboid(loc1, loc2);
        save();
    }

    public Arena(String name, Location loc1, Location loc2, MainPlugin main, ArenaStatus status, int spawnValue) {
        this.type = ArenaType.SOLO;
        this.spawnValue = spawnValue;
        this.location1 = loc1;
        this.location2 = loc2;
        this.name = name;
        this.main = main;
        this.status = status;
        this.cub = new Cuboid(loc1, loc2);
        save();
    }

    private void save() {
        ConfigManager con = main.getConfigManager();
        con.getConfig().set("ArenaData." + name + ".CuboidName", name);
        con.getConfig().set("ArenaData." + name + ".Location1." + "X", location1.getX());
        con.getConfig().set("ArenaData." + name + ".Location1." + "Y", location1.getY());
        con.getConfig().set("ArenaData." + name + ".Location1." + "Z", location1.getZ());
        con.getConfig().set("ArenaData." + name + ".Location1." + "World", location1.getWorld().getName());

        con.getConfig().set("ArenaData." + name + ".Location2." + "X", location2.getX());
        con.getConfig().set("ArenaData." + name + ".Location2." + "Y", location2.getY());
        con.getConfig().set("ArenaData." + name + ".Location2." + "Z", location2.getZ());
        con.getConfig().set("ArenaData." + name + ".Location2." + "World", location2.getWorld().getName());
        con.getConfig().set("ArenaData." + name + ".MaxSpawn", spawnValue);
        con.saveConfig();
    }

    public void saveSpawn(int number, Location loc) {
        ConfigManager con = main.getConfigManager();
        con.getConfig().set("ArenaData." + name + ".Spawns." + number + ".X", loc.getX());
        con.getConfig().set("ArenaData." + name + ".Spawns." + number + ".Y", loc.getY());
        con.getConfig().set("ArenaData." + name + ".Spawns." + number + ".Z", loc.getZ());
        con.getConfig().set("ArenaData." + name + ".Spawns." + number + ".World", loc.getWorld().getName());
        con.saveConfig();
    }

    public void saveSpawn(Location loc) {
        ConfigManager con = main.getConfigManager();
        ConfigurationSection cs = con.getConfig().getConfigurationSection("ArenaData." + name + ".Spawns");
        if (cs == null) { return; }
        int result = cs.getKeys(false).size() + 1;
        con.getConfig().set("ArenaData." + name + ".Spawns." + result + ".X", loc.getX());
        con.getConfig().set("ArenaData." + name + ".Spawns." + result + ".Y", loc.getY());
        con.getConfig().set("ArenaData." + name + ".Spawns." + result + ".Z", loc.getZ());
        con.getConfig().set("ArenaData." + name + ".Spawns." + result + ".World", loc.getWorld().getName());
        con.saveConfig();
    }

    public void clearSpawnList() {
        ConfigManager con = main.getConfigManager();
        ConfigurationSection cs = con.getConfig().getConfigurationSection("ArenaData." + name + ".Spawns");
        if (cs == null) { return; }
        for (String s : cs.getKeys(false)) {
            con.getConfig().set("ArenaData." + name + ".Spawns." + s, null);
            con.getConfig().set("ArenaData." + name + ".Spawns." + s, null);
            con.getConfig().set("ArenaData." + name + ".Spawns." + s, null);
            con.getConfig().set("ArenaData." + name + ".Spawns." + s, null);
            con.saveConfig();
        }
    }

    public void loadSpawn() {
        ConfigManager con = main.getConfigManager();
        ConfigurationSection cs = main.getConfigManager().getConfig().getConfigurationSection("ArenaData." + name + ".Spawns");
        if (cs == null) { return; }
        for (String s : cs.getKeys(false)) {
            int x = con.getConfig().getInt("ArenaData." + name + ".Spawns." + s + ".X");
            int y = con.getConfig().getInt("ArenaData." + name + ".Spawns." + s + ".Y");
            int z = con.getConfig().getInt("ArenaData." + name + ".Spawns." + s + ".Z");
            String world = con.getConfig().getString("ArenaData." + name + ".Spawns." + s + ".World");
            Location loc = new Location(Bukkit.getWorld(world), x, y, z);
            spawns.put(Integer.parseInt(s), loc);
        }
    }

    public boolean hasThisPlayer(Player player) {
        return cub.contains(player.getLocation());
    }

    public HashMap<Integer, Location> getSpawn() {
        return spawns;
    }

    public String getName() { return this.name; }

    public ArenaStatus getStatus() {
        return status;
    }

    public Cuboid getCuboid() {
        return cub;
    }

    public ArenaType getType() {
        return type;
    }
}
