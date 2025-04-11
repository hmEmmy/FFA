package me.emmy.tulip.feature.hotbar;

import lombok.Getter;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 28/07/2024 - 20:45
 */
@Getter
public enum HotbarItem {

    GAME_MENU, KIT_LAYOUT, SHOP, STATS, SETTINGS;

    private Material material;
    private int slot;
    private int durability;
    private String name;
    private String command;
    private String[] lore;

    private static final Map<String, HotbarItem> hotbarItemsMap = new HashMap<>();

    static {
        loadConfig();
    }

    /**
     * Loads the hotbar items from the configuration file.
     */
    private static void loadConfig() {
        ConfigurationSection section = ConfigService.getInstance().getHotbarConfig().getConfigurationSection("hotbar-items");

        if (section != null) {
            for (String key : section.getKeys(false)) {
                HotbarItem hotbarItem = HotbarItem.valueOf(key.toUpperCase());
                ConfigurationSection itemSection = section.getConfigurationSection(key);
                hotbarItem.material = Material.valueOf(itemSection.getString("material"));
                hotbarItem.slot = itemSection.getInt("slot");
                hotbarItem.durability = itemSection.getInt("durability");
                hotbarItem.name = itemSection.getString("name");
                hotbarItem.command = itemSection.getString("command");
                hotbarItem.lore = itemSection.getStringList("lore").toArray(new String[0]);
                hotbarItemsMap.put(hotbarItem.name, hotbarItem);
            }
        }
    }

    /**
     * Check if the item is enabled in the config.
     *
     * @param hotbarItem the hotbar item
     * @return the boolean
     */
    public static boolean isItemEnabledInConfig(HotbarItem hotbarItem) {
        return ConfigService.getInstance().getHotbarConfig().getBoolean("hotbar-items." + hotbarItem.name() + ".enabled");
    }

    /**
     * Construct the Hotbar Items.
     *
     * @return the item stack
     */
    public ItemStack createItem() {
        return new ItemBuilder(material)
                .name(name)
                .durability(durability)
                .lore(lore)
                .hideMeta()
                .build();
    }

    /**
     * Gets the hotbar items.
     *
     * @param item the item
     * @return the item
     */
    public static HotbarItem getItem(ItemStack item) {
        for (HotbarItem hotbarItem : values()) {
            if (hotbarItem.createItem().equals(item)) {
                return hotbarItem;
            }
        }
        return null;
    }

    /**
     * Reloads the configuration and updates the hotbar items.
     */
    public static void reloadConfig() {
        hotbarItemsMap.clear();
        loadConfig();
    }
}