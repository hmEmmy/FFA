package me.emmy.tulip.config;

import lombok.Getter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.util.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:28
 */
@Getter
public class ConfigService {
    @Getter private static ConfigService instance;

    private final Tulip plugin = Tulip.getInstance();

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    private final FileConfiguration settingsConfig;
    private final FileConfiguration localeConfig;
    private final FileConfiguration hotbarConfig;
    private final FileConfiguration scoreboardConfig;
    private final FileConfiguration tablistConfig;
    private final FileConfiguration arenasConfig;
    private final FileConfiguration kitsConfig;
    private final FileConfiguration ffaConfig;
    private final FileConfiguration gameMenuConfig;
    private final FileConfiguration settingsMenuConfig;
    private final FileConfiguration kitEditorMenuConfig;
    private final FileConfiguration kitEditorSelectMenuConfig;
    private final FileConfiguration statsMenuConfig;

    /**
     * Array of all the config file names
     */
    private final String[] configFileNames = {
            "settings.yml", "locale.yml", "hotbar.yml",
            "storage/arenas.yml", "storage/kits.yml", "storage/ffa.yml",
            "visual/scoreboard.yml", "visual/tablist.yml",
            "menus/game-menu.yml", "menus/settings-menu.yml", "menus/kit-editor-menu.yml", "menus/kit-editor-select-menu.yml", "menus/stats-menu.yml"
    };

    /**
     * Constructor for the ConfigService class
     */
    public ConfigService() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }

        instance = this;

        settingsConfig = getConfig("settings.yml");
        localeConfig = getConfig("locale.yml");
        hotbarConfig = getConfig("hotbar.yml");
        scoreboardConfig = getConfig("visual/scoreboard.yml");
        tablistConfig = getConfig("visual/tablist.yml");
        arenasConfig = getConfig("storage/arenas.yml");
        kitsConfig = getConfig("storage/kits.yml");
        ffaConfig = getConfig("storage/ffa.yml");
        gameMenuConfig = getConfig("menus/game-menu.yml");
        settingsMenuConfig = getConfig("menus/settings-menu.yml");
        kitEditorMenuConfig = getConfig("menus/kit-editor-menu.yml");
        kitEditorSelectMenuConfig = getConfig("menus/kit-editor-select-menu.yml");
        statsMenuConfig = getConfig("menus/stats-menu.yml");
    }

    /**
     * Load a config file
     *
     * @param fileName the name of the file to load
     */
    private void loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        configFiles.put(fileName, configFile);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        fileConfigurations.put(fileName, config);
    }

    /**
     * Reload all the config files
     */
    public void reloadConfigs() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    /**
     * Save all the config files
     */
    public void saveConfigs() {
        for (Map.Entry<String, FileConfiguration> entry : fileConfigurations.entrySet()) {
            String fileName = entry.getKey();
            FileConfiguration config = entry.getValue();
            File configFile = configFiles.get(fileName);
            saveConfig(configFile, config);
        }
    }

    /**
     * Save a config file
     *
     * @param configFile the file to save
     * @param fileConfiguration the configuration to save
     */
    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            Logger.logError("Failed to save config file: " + configFile.getName());
        }
    }

    /**
     * Get a config file and load it to apply changes
     *
     * @param fileName the name of the file to get
     * @return the config file
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(Tulip.getInstance().getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get a config file from the configFiles map
     *
     * @param fileName the name of the file to get
     * @return the config file
     */
    public File getConfigFile(String fileName) {
        return configFiles.get(fileName);
    }
}