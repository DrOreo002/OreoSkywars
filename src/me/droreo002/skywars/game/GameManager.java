package me.droreo002.skywars.game;

import com.sun.istack.internal.Nullable;
import me.droreo002.skywars.MainPlugin;
import me.droreo002.skywars.manager.Arena;
import me.droreo002.skywars.manager.util.ArenaStatus;
import me.droreo002.skywars.manager.util.PlayerStatus;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {


    private HashMap<Arena, HashSet<UUID>> players = new HashMap<>();
    private HashMap<String, Arena> arenas = new HashMap<>();
    private HashMap<UUID, Arena> onArena = new HashMap<>();
    private MainPlugin main;
    private static GameManager instance;
    private HashMap<UUID, PlayerStatus> player_status;

    private GameManager(MainPlugin main) {
        this.main = main;
    }

    public static GameManager getInstance(MainPlugin main) {
        if (instance == null) {
            instance = new GameManager(main);
            return instance;
        }
        return instance;
    }

    public void addArena(String name, Arena arena) {
        arenas.put(name, arena);
    }

    public void resetAll() {
        if (players.isEmpty() || onArena.isEmpty() || player_status.isEmpty()) { return; }
        player_status.clear();
        players.clear();
        onArena.clear();
    }

    public void addPlayer(Arena arena, Player player, PlayerStatus status) {
        HashSet<UUID> loaded = players.get(arena);
        loaded.add(player.getUniqueId());
        players.put(arena, loaded);
        player_status.put(player.getUniqueId(), status);
        onArena.put(player.getUniqueId(), arena);
    }

    public HashSet<UUID> getPlayers(Arena arena) {
        return players.get(arena);
    }

    public HashMap<String, Arena> getArenas() {
        return arenas;
    }

    public Arena getArena(String name) {
        if (!arenas.containsKey(name)) {
            return null;
        }
        return arenas.get(name);
    }

    public PlayerStatus getStatus(Player player) {
        if (!player_status.containsKey(player.getUniqueId())) { return PlayerStatus.NOT_LOADED; }
        return player_status.get(player.getUniqueId());
    }

    public void loadAllArena() {
        ConfigurationSection sec = main.getConfigManager().getConfig().getConfigurationSection("ArenaData");
        if (sec == null) { return; }
        for (String s : sec.getKeys(false)) {
            int x = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location1.X");
            int y = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location1.Y");
            int z = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location1.Z");
            String world = main.getConfigManager().getConfig().getString("ArenaData." + s + ".Location2.World");
            Location loc1 = new Location(Bukkit.getWorld(world), x, y, z);

            int x2 = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location2.X");
            int y2 = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location2.Y");
            int z2 = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".Location2.Z");
            int value = main.getConfigManager().getConfig().getInt("ArenaData." + s + ".MaxSpawn");
            String world2 = main.getConfigManager().getConfig().getString("ArenaData." + s + ".Location2.World");
            Location loc2 = new Location(Bukkit.getWorld(world2), x2, y2, z2);

            Arena arena = new Arena(s, loc1, loc2, main, ArenaStatus.WAITING, value);
            arena.loadSpawn();
            addArena(arena.getName(), arena);
            Bukkit.getLogger().info("Loaded : " + s + ", Arena!");
        }
    }

    @Nullable
    public Arena getArenaOnPlayer(Player player) {
        if (!onArena.containsKey(player.getUniqueId())) { return null; }
        return onArena.get(player.getUniqueId());
    }
}
