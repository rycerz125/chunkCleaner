package rycerz125.chunkcleaner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import rycerz125.chunkcleaner.commands.CleanCommand;
import rycerz125.chunkcleaner.data.WorldDataService;
import rycerz125.chunkcleaner.utils.Utils;

public class ChunkCleaner extends JavaPlugin{

    public static WorldDataService worldDataService;
    public static Utils utils;
    public static ChunkRemover chunkRemover;
    @Override
    public void onEnable() {
        registerListeners();
        worldDataService = new WorldDataService();
        utils = new Utils();
        chunkRemover = new ChunkRemover();
        this.getCommand("clean").setExecutor(new CleanCommand());
        this.getCommand("clean").setPermission("chunkCleaner");
        this.getCommand("clean").setPermissionMessage("access denied");
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }
    private void registerListeners(){
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChunkEventsListener(), this);
    }
}
