package me.droreo002.skywars.listener.custom;

import me.droreo002.skywars.MainPlugin;
import me.droreo002.skywars.manager.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameJoinEvent extends Event implements Cancellable {

    private Player player;
    private Arena arena;
    private boolean isCancelled;
    private static final HandlerList handlers = new HandlerList();

    public GameJoinEvent(Player player, Arena arena) {
        this.player = player;
        this.arena = arena;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlersList() {
        return handlers;
    }

    public Arena getArena() {
        return arena;
    }

    public Player getPlayer() {
        return player;
    }
}
