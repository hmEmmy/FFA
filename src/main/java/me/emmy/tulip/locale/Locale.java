package me.emmy.tulip.locale;

import lombok.Getter;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.CC;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 21/08/2024 - 20:19
 */
@Getter
public enum Locale {
    NO_PERMISSION("no-permission", "locale.yml"),

    SPAWN_SET("spawn.set", "locale.yml"),
    SPAWN_TELEPORTED("spawn.teleported", "locale.yml"),
    
    ARENA_ALREADY_EXISTS("arena.already-exists", "locale.yml"),
    ARENA_DOES_NOT_EXIST("arena.does-not-exist", "locale.yml"),
    ARENA_CREATED("arena.created", "locale.yml"),
    ARENA_DELETED("arena.deleted", "locale.yml"),
    ARENA_CENTER_SET("arena.center-set", "locale.yml"),
    ARENA_SAFE_POS_SET("arena.safe-pos-set", "locale.yml"),
    ARENA_SPAWN_SET("arena.spawn-set", "locale.yml"),
    ARENA_CENTER_NOT_SET("arena.center-not-set", "locale.yml"),

    KIT_DEFAULT_DESCRIPTION("kit.default-description", "locale.yml"),

    KIT_ALREADY_EXISTS("kit.already-exists", "locale.yml"),
    KIT_DOES_NOT_EXIST("kit.does-not-exist", "locale.yml"),
    KIT_CREATED("kit.created", "locale.yml"),
    KIT_DELETED("kit.deleted", "locale.yml"),
    KIT_INVENTORY_GIVEN("kit.inventory-given", "locale.yml"),
    KIT_DESCRIPTION_SET("kit.description-set", "locale.yml"),
    KIT_ICON_SET("kit.icon-set", "locale.yml"),
    KIT_INVENTORY_SET("kit.inventory-set", "locale.yml"),
    KIT_TOGGLED("kit.toggled", "locale.yml"),
    KIT_DISABLED("kit.disabled", "locale.yml"),

    FFA_MATCH_ALREADY_EXISTS("ffa-match.already-exists", "locale.yml"),
    FFA_MATCH_DOES_NOT_EXIST("ffa-match.does-not-exist", "locale.yml"),
    FFA_MATCH_CREATED("ffa-match.created", "locale.yml"),
    FFA_MATCH_DELETED("ffa-match.deleted", "locale.yml"),
    FFA_PLAYER_NOT_IN_MATCH("ffa-match.player-not-in-match", "locale.yml"),
    FFA_KICKED_PLAYER("ffa-match.kicked-player", "locale.yml"),
    FFA_KICKED("ffa-match.kicked", "locale.yml"),
    FFA_MAX_PLAYERS_SET("ffa-match.max-players-set", "locale.yml"),
    FFA_NOT_IN_MATCH("ffa-match.not-in-match", "locale.yml"),
    FFA_ALREADY_IN_MATCH("ffa-match.already-in-match", "locale.yml"),
    FFA_ENDERPEARL_COOLDOWN("ffa-match.enderpearl-cooldown", "locale.yml"),
    FFA_COOLDOWN_EXPIRED("ffa-match.cooldown-expired", "locale.yml"),

    ;

    private final String string;
    private final String config;

    /**
     * Constructor for the locale
     *
     * @param string the string
     * @param config the config
     */
    Locale(String string, String config) {
        this.string = string;
        this.config = config;
    }

    /**
     * Get the string from the config file
     *
     * @return the string and the config
     */
    public String getStringPath() {
        return CC.translate(ConfigService.getInstance().getConfig(config).getString(string));
    }

    /**
     * Get the string list from the config file
     *
     * @return the string list and the config
     */
    public List<String> getStringListPath() {
        return ConfigService.getInstance().getConfig(config).getStringList(string);
    }

    /**
     * Get the boolean result from the config file
     *
     * @return the boolean and the config
     */
    public boolean getBooleanPath() {
        return ConfigService.getInstance().getConfig(config).getBoolean(string);
    }

    /**
     * Get the integer value from the config file
     *
     * @return the integer and the config
     */
    public int getIntPath() {
        return ConfigService.getInstance().getConfig(config).getInt(string);
    }

    /**
     * Get the double value from the config file
     *
     * @return the double and the config
     */
    public double getDoublePath() {
        return ConfigService.getInstance().getConfig(config).getDouble(string);
    }

    /**
     * Get the long value from the config file
     *
     * @return the long and the config
     */
    public long getLongPath() {
        return ConfigService.getInstance().getConfig(config).getLong(string);
    }

    /**
     * Get the ChatColor value from the config file
     *
     * @return the color and the config
     */
    public ChatColor getColorPath() {
        return ChatColor.valueOf(String.valueOf(ConfigService.getInstance().getConfig(config).getColor(string)));
    }
}