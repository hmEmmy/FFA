package me.emmy.tulip.hotbar;

import lombok.Getter;
import me.emmy.tulip.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Tulip
 * @date 28/07/2024 - 20:45
 */
@Getter
public enum HotbarItemBuilder {

    GAME_MENU(Material.DIAMOND_SWORD, 0, 0, "&dGame Menu &7(Right Click)", "play"),
    KIT_LAYOUT(Material.BOOK, 1, 0, "&dEdit Kit Layout &7(Right Click)", "kitlayout"),
    SHOP(Material.DIAMOND, 4, 0, "&dShop &7(Right Click)", "shop"),
    STATS(Material.NETHER_STAR, 7, 0, "&dStats &7(Right Click)", "stats"),
    SETTINGS(Material.SKULL_ITEM, 8, 0, "&dSettings &7(Left Click)", "ffasettings")

    ;

    private final Material material;
    private final int slot;
    private final int durability;
    private final String name;
    private final String command;
    private final String[] lore;

    /**
     * Instantiates a new Hotbar item builder.
     *
     * @param material    the material
     * @param slot        the slot
     * @param durability  the durability
     * @param name        the name
     * @param command     the command
     * @param lore        the lore
     */
    HotbarItemBuilder(Material material, int slot, int durability, String name, String command, String... lore) {
        this.material = material;
        this.slot = slot;
        this.durability = durability;
        this.name = name;
        this.command = command;
        this.lore = lore;
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
                .build();
    }

    /**
     * Gets the hotbar items.
     *
     * @param item the item
     * @return the item
     */
    public static HotbarItemBuilder getItem(ItemStack item) {
        for (HotbarItemBuilder hotbarItem : values()) {
            if (hotbarItem.createItem().equals(item)) {
                return hotbarItem;
            }
        }
        return null;
    }
}