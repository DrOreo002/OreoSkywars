package me.droreo002.skywars;

import me.droreo002.skywars.commands.MainCommand;
import me.droreo002.skywars.game.GameManager;
import me.droreo002.skywars.manager.Arena;
import me.droreo002.skywars.manager.ConfigManager;
import me.droreo002.skywars.manager.Cuboid;
import me.droreo002.skywars.manager.EditorManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MainPlugin extends JavaPlugin {

    private String prefix;
    private ConfigManager manager;
    private GameManager gameManager;
    private EditorManager editorManager;
    public HashMap<UUID, Arena> on_setup = new HashMap<>();
    public HashMap<UUID, Location> pos1 = new HashMap<UUID, Location>();
    public HashMap<UUID, Location> pos2 = new HashMap<UUID, Location>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        manager = ConfigManager.getInstance();
        manager.setup();
        prefix = manager.getConfig().getString("Prefix");
        gameManager = GameManager.getInstance(this);
        gameManager.loadAllArena();
        editorManager = EditorManager.getInstance(this);
        ConfigurationSerialization.registerClass(Cuboid.class, "ArenaCuboid");

        //Register commands
        Bukkit.getPluginCommand("oreoskywars").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ConfigurationSerialization.unregisterClass(Cuboid.class);
        getLogger().warning("Unregistering all class!");
    }

    public ConfigManager getConfigManager() {
        return manager;
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', prefix);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public EditorManager getEditorManager() {
        return editorManager;
    }
}
