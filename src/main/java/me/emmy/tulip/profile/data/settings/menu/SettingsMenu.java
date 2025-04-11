package me.emmy.tulip.profile.data.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 01/08/2024 - 14:59
 */
@AllArgsConstructor
public class SettingsMenu extends Menu {

    private final FileConfiguration config = ConfigService.getInstance().getSettingsMenuConfig();
    
    @Override
    public String getTitle(Player player) {
        return CC.translate(config.getString("title", "&e&lSettings"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        // -- SIDEBAR --
        
        int sidebarSlot = config.getInt("buttons.sidebar-visibility.slot");
        Material sidebarMaterial = Material.matchMaterial(config.getString("buttons.sidebar-visibility.material", "PAPER"));
        String sidebarName = CC.translate(config.getString("buttons.sidebar-visibility.name", "&e&lSidebar Visibility"));
        int sidebarDurability = config.getInt("buttons.sidebar-visibility.durability", 0);

        buttons.put(sidebarSlot, new SettingsButton(sidebarMaterial, sidebarName, sidebarDurability,
                profile.getSettingsData().isShowScoreboard() ?
                        config.getStringList("buttons.sidebar-visibility.enabled-lore")
                        :
                        config.getStringList("buttons.sidebar-visibility.disabled-lore")
        ));
        
        // -- TABLIST --
        
        int tablistSlot = config.getInt("buttons.tablist-visibility.slot");
        Material tablistMaterial = Material.matchMaterial(config.getString("buttons.tablist-visibility.material", "NAME_TAG"));
        String tablistName = CC.translate(config.getString("buttons.tablist-visibility.name", "&e&lTablist Visibility"));
        int tablistDurability = config.getInt("buttons.tablist-visibility.durability", 0);
        
        buttons.put(tablistSlot, new SettingsButton(tablistMaterial, tablistName, tablistDurability,
                profile.getSettingsData().isShowTablist() ?
                        config.getStringList("buttons.tablist-visibility.enabled-lore")
                        :
                        config.getStringList("buttons.tablist-visibility.disabled-lore")
        ));
        
        // -- GLASS BORDER --
        
        if (config.getBoolean("glass-border.enabled")) {
            addBorder(buttons, (byte) config.getInt("glass-border.durability"), config.getInt("rows"));
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return config.getInt("rows") * 9;
    }

    @AllArgsConstructor
    public static class SettingsButton extends Button {
        private final Material icon;
        private final String name;
        private int data;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(icon)
                    .name(name)
                    .lore(lore)
                    .hideMeta()
                    .durability(data)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) {
                return;
            }

            FileConfiguration config = ConfigService.getInstance().getLocaleConfig();

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            switch (icon) {
                case PAPER:
                    profile.getSettingsData().setShowScoreboard(!profile.getSettingsData().isShowScoreboard());
                    player.sendMessage(profile.getSettingsData().isShowScoreboard() ? CC.translate(config.getString("profile-settings.sidebar.enabled")) : CC.translate(config.getString("profile-settings.sidebar.disabled")));
                    break;
                case NAME_TAG:
                    profile.getSettingsData().setShowTablist(!profile.getSettingsData().isShowTablist());
                    player.sendMessage(profile.getSettingsData().isShowTablist() ? CC.translate(config.getString("profile-settings.tablist.enabled")) : CC.translate(config.getString("profile-settings.tablist.disabled")));
                    break;
            }

            playerClickSound(player);
        }
    }
}