package me.emmy.tulip.feature.cooldown;

import lombok.Getter;
import me.emmy.tulip.util.MutableTriple;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project FFA
 * @date 5/27/2024
 */
@Getter
public class CooldownRepository {
    /**
     * -- GETTER --
     *  Get all the cooldowns from the repository
     *
     * @return the cooldowns
     */
    private final List<MutableTriple<UUID, String, Cooldown>> cooldowns;

    /**
     * Constructor for the CooldownRepository
     */
    public CooldownRepository() {
        this.cooldowns = new ArrayList<>();
    }

    /**
     * Add a cooldown to the repository
     *
     * @param uuid     the uuid of the player
     * @param key      the key of the cooldown
     * @param cooldown the cooldown
     */
    public void addCooldown(UUID uuid, String key, Cooldown cooldown) {
        cooldowns.removeIf(triple -> triple.getA().equals(uuid) && triple.getB().equals(key));
        cooldowns.add(new MutableTriple<>(uuid, key, cooldown));
    }

    /**
     * Get a cooldown from the repository by the uuid and key
     *
     * @param uuid the uuid of the player
     * @param key  the key of the cooldown
     * @return the cooldown
     */
    public Cooldown getCooldown(UUID uuid, String key) {
        return cooldowns.stream()
                .filter(triple -> triple.getA().equals(uuid) && triple.getB().equals(key))
                .map(MutableTriple::getC)
                .findFirst()
                .orElse(null);
    }
}