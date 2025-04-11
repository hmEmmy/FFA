package me.emmy.tulip.profile;

import lombok.Getter;
import lombok.Setter;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.profile.data.coins.ProfileCoins;
import me.emmy.tulip.profile.enums.EnumProfileState;
import me.emmy.tulip.profile.data.kitlayout.ProfileKitLayout;
import me.emmy.tulip.profile.data.settings.ProfileSettings;
import me.emmy.tulip.profile.data.stats.ProfileStats;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 18:26
 */
@Getter
@Setter
public class Profile {
    private UUID uuid;
    private String name;

    private boolean firstJoin;
    private boolean buildMode;

    private AbstractGame game;
    private EnumProfileState state;

    private ProfileStats statsData;
    private ProfileSettings settingsData;
    private ProfileKitLayout kitLayoutData;
    private ProfileCoins coinsData;
    private List<String> ownedProducts;

    /**
     * Constructor for the Profile class
     *
     * @param uuid the UUID of the profile
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.firstJoin = true;
        this.buildMode = false;
        this.statsData = new ProfileStats();
        this.settingsData = new ProfileSettings();
        this.kitLayoutData = new ProfileKitLayout();
        this.coinsData = new ProfileCoins(1000);
        this.ownedProducts = new ArrayList<>();
    }

    /**
     * Load all the profile data
     */
    public void loadProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().loadProfile(this);
    }

    /**
     * Save all the profile data
     */
    public void saveProfile() {
        Tulip.getInstance().getProfileRepository().getProfile().saveProfile(this);
    }
}