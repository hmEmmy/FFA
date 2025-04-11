package me.emmy.tulip.game.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project FFA
 * @date 25/05/2024 - 14:25
 */
@Getter
public enum EnumFFAState {
    SPAWN("Spawn", "The player is in the safezone."),
    FIGHTING("Fighting", "The player is fighting outside safezone.")

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumFFAState enum.
     *
     * @param name The name of the state.
     * @param description The description of the state.
     */
    EnumFFAState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
