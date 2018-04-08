package cc.funkemunky.dreya;

import cc.funkemunky.dreya.command.DreyaCommand;
import cc.funkemunky.dreya.data.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Dreya extends JavaPlugin {

    private static Dreya instance;
    private DataManager dataManager;

    public void onEnable() {
        instance = this;
        dataManager = new DataManager();

    }

    private void registerCommands() {
        getCommand("dreya").setExecutor(new DreyaCommand());
    }

    public static Dreya getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}
