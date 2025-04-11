package me.emmy.tulip.feature.arena;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.LocationUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:35
 */
@Getter
@Setter
public class ArenaRepository {
    private List<Arena> arenas = new ArrayList<>();

    /**
     * Load arenas from the arenas.yml file
     */
    public void loadArenas() {
        FileConfiguration config = ConfigService.getInstance().getArenasConfig();

        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");
        if (arenasConfig == null) {
            return;
        }

        for (String name : arenasConfig.getKeys(false)) {
            String key = "arenas." + name;

            Arena arena = new Arena(
                    name,
                    LocationUtil.deserialize(config.getString(key + ".spawn")),
                    LocationUtil.deserialize(config.getString(key + ".center")),
                    LocationUtil.deserialize(config.getString(key + ".safePos1")),
                    LocationUtil.deserialize(config.getString(key + ".safePos2"))
            );
            arenas.add(arena);
        }
    }

    /**
     * Save all arenas to the arenas.yml file
     */
    public void saveArenas() {
        FileConfiguration config = ConfigService.getInstance().getArenasConfig();

        for (Arena arena : arenas) {
            String key = "arenas." + arena.getName();
            config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
            config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));
            config.set(key + ".safePos1", LocationUtil.serialize(arena.getSafePos1()));
            config.set(key + ".safePos2", LocationUtil.serialize(arena.getSafePos2()));
        }

        ConfigService.getInstance().saveConfig(ConfigService.getInstance().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Save a specific arena to the arenas.yml file
     *
     * @param arena The arena to save
     */
    public void saveArena(Arena arena) {
        FileConfiguration config = ConfigService.getInstance().getArenasConfig();

        String key = "arenas." + arena.getName();
        //config.set(key + ".name", arena.etName());
        config.set(key + ".spawn", LocationUtil.serialize(arena.getSpawn()));
        config.set(key + ".center", LocationUtil.serialize(arena.getCenter()));
        config.set(key + ".safePos1", LocationUtil.serialize(arena.getSafePos1()));
        config.set(key + ".safePos2", LocationUtil.serialize(arena.getSafePos2()));

        ConfigService.getInstance().saveConfig(ConfigService.getInstance().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Get an arena by name
     *
     * @param name The name of the arena
     * @return The arena
     */
    public Arena getArena(String name) {
        return arenas.stream()
                .filter(arena -> arena.getName().equalsIgnoreCase(name))
                .findFirst().
                orElse(null);
    }

    /**
     * Create an arena
     *
     * @param name     The name of the arena
     * @param spawn    The spawn location of the arena
     * @param center   The center location of the arena
     * @param safePos1 The first safe position of the arena
     * @param safePos2 The second safe position of the arena
     */
    public void createArena(String name, String spawn, String center, String safePos1, String safePos2) {
        Arena arena = new Arena(name, LocationUtil.deserialize(spawn), LocationUtil.deserialize(center), LocationUtil.deserialize(safePos1), LocationUtil.deserialize(safePos2));
        arenas.add(arena);
        saveArena(arena);
    }

    /**
     * Delete an arena
     *
     * @param name The name of the arena
     */
    public void deleteArena(String name) {
        Arena arena = getArena(name);
        arenas.remove(arena);
        FileConfiguration config = ConfigService.getInstance().getArenasConfig();
        config.set("arenas." + name, null);
        ConfigService.getInstance().saveConfig(ConfigService.getInstance().getConfigFile("storage/arenas.yml"), config);
    }
}