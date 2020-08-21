package eu.endermite.togglepvp;

import eu.endermite.togglepvp.commands.MainCommand;
import eu.endermite.togglepvp.config.ConfigCache;
import eu.endermite.togglepvp.listeners.player.*;
import eu.endermite.togglepvp.listeners.player.WolfAttackPlayerListener;
import eu.endermite.togglepvp.listeners.wolf.WolfTargettingPlayerListener;
import eu.endermite.togglepvp.listeners.unspecific.*;
import eu.endermite.togglepvp.listeners.wolf.*;
import eu.endermite.togglepvp.players.PlayerManager;
import eu.endermite.togglepvp.players.SmartCache;
import eu.endermite.togglepvp.util.Listener;
import eu.endermite.togglepvp.util.DatabaseSQLite;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;

public final class TogglePvP extends JavaPlugin {

    @Getter private static TogglePvP plugin;
    private ConfigCache configCache;
    private PlayerManager playerManager;
    private DatabaseSQLite sqLite;

    @Override
    public void onEnable() {
        plugin = this;
        reloadPluginConfig();
        File dbFile = new File("plugins/TogglePvP");
        sqLite = new DatabaseSQLite("jdbc:sqlite:plugins/TogglePvP/TogglePvP.db", dbFile);
        sqLite.createDatabaseFile();
        if (!sqLite.testConnection()) {
            getLogger().severe("Error with accessing database. Check if server has write rights.");
            getLogger().severe("Plugin will now disable.");
            getServer().getPluginManager().disablePlugin(this);
        }
        playerManager = new PlayerManager();

        SmartCache.runSmartCache();

        // Register listeners
        Reflections reflections = new Reflections(new String[]{"eu.endermite.togglepvp"});
        Set<Class<?>> listenerClasses = reflections.getTypesAnnotatedWith(Listener.class);
        listenerClasses.forEach((listener)-> {
            try {
                getServer().getPluginManager().registerEvents((org.bukkit.event.Listener) listener.getConstructor().newInstance(), this);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                getLogger().severe("Error with registering listeners.");
                getLogger().severe("Plugin will now disable.");
                getServer().getPluginManager().disablePlugin(this);
            }
        });

        // Register command
        Objects.requireNonNull(getCommand("pvp")).setExecutor(new MainCommand());
        Objects.requireNonNull(getCommand("pvp")).setTabCompleter(new MainCommand());
    }

    public void reloadPluginConfig() {
        saveDefaultConfig();
        reloadConfig();
        configCache = new ConfigCache();
    }

    public void reloadPluginConfig(CommandSender commandSender) {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            reloadPluginConfig();
            commandSender.sendMessage("TogglePvP configuration reloaded.");
        });
    }

    public ConfigCache getConfigCache() {
        return configCache;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DatabaseSQLite getSqLite() {return sqLite;}

}
