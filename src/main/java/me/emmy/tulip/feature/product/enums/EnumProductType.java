package me.emmy.tulip.feature.product.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project FFA
 * @date 08/09/2024 - 14:46
 */
@Getter
public enum EnumProductType {
    EFFECT("Effect", "Gives you an effect"),
    COIN_BOOSTER("Coin Booster", "Doubles your coins by a certain amount each time you kill a player");

    private final String name;
    private final String description;

    /**
     * Constructor for the ProductType enum.
     *
     * @param name the name
     * @param description the description
     */
    EnumProductType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}