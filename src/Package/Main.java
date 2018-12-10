package Package;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        getLogger().info("Plugin enabled");

        getCommand("createmaze").setExecutor(new Commands(this));
        Bukkit.getPluginManager().registerEvents(new Handler(),this);
    }

    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}
