package me.emmy.tulip.feature.product;

import lombok.Getter;
import me.emmy.tulip.feature.product.enums.EnumProductType;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project FFA
 * @date 08/09/2024 - 14:42
 */
@Getter
public class Product {
    private final String name;
    private final String displayName;
    private final String description;

    private final Material icon;

    private final int price;
    private final int durability;

    private final EnumProductType type;

    /**
     * Instantiates a new Product.
     *
     * @param name        the name
     * @param displayName the display name
     * @param description the description
     * @param icon        the icon
     * @param price       the price
     * @param durability  the durability
     * @param type        the type
     */
    public Product(String name, String displayName, String description, Material icon, int price, int durability, EnumProductType type) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.icon = icon;
        this.price = price;
        this.durability = durability;
        this.type = type;
    }
}