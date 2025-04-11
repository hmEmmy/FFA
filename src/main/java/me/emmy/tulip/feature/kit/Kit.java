package me.emmy.tulip.feature.kit;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 21:56
 */
@Getter
@Setter
public class Kit {
    private String name;
    private String description;

    private ItemStack[] items;
    private ItemStack[] armor;

    private Material icon;
    private int iconData;

    private boolean enabled;

    /**
     * Constructor for the Kit class
     *
     * @param name The name of the kit
     * @param description The description of the kit
     * @param items The items in the kit
     * @param armor The armor in the kit
     * @param icon The icon of the kit
     * @param enabled Whether the kit is enabled
     */
    public Kit(String name, String description, ItemStack[] items, ItemStack[] armor, Material icon, int iconData, boolean enabled) {
        this.name = name;
        this.description = description;
        this.items = items;
        this.armor = armor;
        this.icon = icon;
        this.iconData = iconData;
        this.enabled = enabled;
    }
}