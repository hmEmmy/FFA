package me.emmy.tulip.game.spawn;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.arena.Arena;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.Cuboid;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.LocationUtil;
import me.emmy.tulip.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project FFA
 * @date 12/06/2024 - 22:14
 */
@Getter
@Setter
public class FFASpawnHandler {
    private Location spawnLocation;
    private Location safezoneMin;
    private Location safezoneMax;
    private Cuboid cuboid;

    /**
     * Load the FFA spawn location.
     */
    public void loadFFASpawn() {
        FileConfiguration config = ConfigService.getInstance().getArenasConfig();
        Arena arena = Tulip.getInstance().getArenaRepository().getArenas().stream()
                .findFirst()
                .orElse(null);

        if (arena != null) {
            this.spawnLocation = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".spawn"));
            this.safezoneMin = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safePos1"));
            this.safezoneMax = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safePos2"));
        } else {
            Logger.logError("No ffa arenas found!");
            return;
        }

        if (this.safezoneMin != null && this.safezoneMax != null) {
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone found!"));
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone min: " + this.safezoneMin.toString()));
            Bukkit.broadcastMessage(CC.translate("&a [+] FFA safezone max: " + this.safezoneMax.toString()));
            this.cuboid = new Cuboid(safezoneMin, safezoneMax);
        } else {
            Logger.logError("No safezone found!");
        }
    }
}