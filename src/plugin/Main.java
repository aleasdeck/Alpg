package plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public void onEnable() {
        getLogger().info("Plugin enabled");

        getCommand("createmaze").setExecutor(new Commands(this));

    }

    public void onDisable() {
        getLogger().info("Plugin disabled");
    }
}
