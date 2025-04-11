package me.emmy.tulip.profile.data.stats;

import com.google.common.collect.Maps;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.profile.Profile;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project FFA
 * @date 31/07/2024 - 09:46
 */
@Getter
@Setter
public class ProfileStats {
    private Map<Kit, Integer> kitKills;
    private Map<Kit, Integer> kitDeaths;
    private Map<Kit, Integer> currentKillstreak;
    private Map<Kit, Integer> highestKillstreak;

    public ProfileStats() {
        this.kitKills = Maps.newHashMap();
        this.kitDeaths = Maps.newHashMap();
        this.currentKillstreak = Maps.newHashMap();
        this.highestKillstreak = Maps.newHashMap();
        this.feed();
    }

    private void feed() {
        Tulip.getInstance().getKitRepository().getKits().forEach(kit -> {
            this.kitKills.put(kit, 0);
            this.kitDeaths.put(kit, 0);
            this.currentKillstreak.put(kit, 0);
            this.highestKillstreak.put(kit, 0);
        });
    }

    /**
     * Increment the kills of a kit
     *
     * @param kit the kit
     */
    public void incrementKitKills(Kit kit) {
        this.kitKills.put(kit, this.kitKills.getOrDefault(kit, 0) + 1);
    }

    /**
     * Increment the deaths of a kit
     *
     * @param kit the kit
     */
    public void incrementKitDeaths(Kit kit) {
        this.kitDeaths.put(kit, this.kitDeaths.getOrDefault(kit, 0) + 1);
    }

    /**
     * Get the kills of a kit
     *
     * @param kit the kit
     * @return the kills
     */
    public int getKitKills(Kit kit) {
        return this.kitKills.getOrDefault(kit, 0);
    }

    /**
     * Get the deaths of a kit
     *
     * @param kit the kit
     * @return the deaths
     */
    public int getKitDeaths(Kit kit) {
        return this.kitDeaths.getOrDefault(kit, 0);
    }

    /**
     * Get total kills of all kits
     *
     * @return the total kills
     */
    public int getTotalKills() {
        return this.kitKills.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get total deaths of all kits
     *
     * @return the total deaths
     */
    public int getTotalDeaths() {
        return this.kitDeaths.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Get the Kill/Death ratio
     *
     * @return the KDR
     */
    public int getKDR() {
        return getTotalDeaths() == 0 ? getTotalKills() : getTotalKills() / getTotalDeaths();
    }

    /**
     * Reset the stats
     */
    public void resetStats() {
        this.kitKills.clear();
        this.kitDeaths.clear();
        this.highestKillstreak.clear();
    }

    /**
     * Set the deaths of a kit to a certain value
     *
     * @param kit the kit
     * @param deaths the deaths
     */
    public void setKitDeaths(Kit kit, int deaths) {
        this.kitDeaths.put(kit, deaths);
    }

    /**
     * Set the kills of a kit to a certain value
     *
     * @param kit the kit
     * @param kills the kills
     */
    public void setKitKills(Kit kit, int kills) {
        this.kitKills.put(kit, kills);
    }

    /**
     * Adds specific amount of deaths to a kit
     *
     * @param kit the kit
     * @param deaths the deaths
     */
    public void addKitDeaths(Kit kit, int deaths) {
        this.kitDeaths.put(kit, this.kitDeaths.getOrDefault(kit, 0) + deaths);
    }

    /**
     * Add specific amount of kills to a kit
     *
     * @param kit the kit
     * @param kills the kills
     */
    public void addKitKills(Kit kit, int kills) {
        this.kitKills.put(kit, this.kitKills.getOrDefault(kit, 0) + kills);
    }

    /**
     * Set the highest killstreak of a kit
     *
     * @param kit the kit
     * @param killstreak the killstreak
     */
    public void setHighestKillstreak(Kit kit, int killstreak) {
        this.highestKillstreak.put(kit, killstreak);
    }

    /**
     * Get the highest killstreak of a kit
     *
     * @param kit the kit
     * @return the highest killstreak
     */
    public int getHighestKillstreak(Kit kit) {
        return this.highestKillstreak.getOrDefault(kit, 0);
    }

    /**
     * Increment the killstreak of a kit
     *
     * @param kit the kit
     */
    public void incrementKillstreak(Kit kit) {
        int streak = this.currentKillstreak.getOrDefault(kit, 0) + 1;
        this.currentKillstreak.put(kit, streak);

        int highest = this.highestKillstreak.getOrDefault(kit, 0);
        if (streak > highest) {
            this.highestKillstreak.put(kit, streak);
        }
    }

    /**
     * Reset the killstreak of a kit and save it if it's higher than the previous highest
     *
     * @param kit the kit
     * @param player the player
     */
    public void resetStreakAndSaveIfPresent(Kit kit, UUID player) {
        int streak = this.currentKillstreak.getOrDefault(kit, 0);
        int highest = this.highestKillstreak.getOrDefault(kit, 0);

        if (streak > highest) {
            this.highestKillstreak.put(kit, streak);

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player);
            Document profileDocument = Tulip.getInstance().getProfileRepository().getCollection()
                    .find(Filters.eq("uuid", profile.getUuid().toString()))
                    .first();

            if (profileDocument != null) {
                Document statsDocument = (Document) profileDocument.getOrDefault("stats", new Document());

                Map<String, Integer> highestKillstreaksMap = new HashMap<>();
                for (Map.Entry<Kit, Integer> entry : this.highestKillstreak.entrySet()) {
                    highestKillstreaksMap.put(entry.getKey().getName(), entry.getValue());
                }
                statsDocument.put("highestKillstreaks", highestKillstreaksMap);

                Tulip.getInstance().getProfileRepository().getCollection()
                        .updateOne(Filters.eq("uuid", profile.getUuid().toString()), new Document("$set", new Document("stats", statsDocument)));
            }
        }

        this.currentKillstreak.put(kit, 0);
    }
}