package me.emmy.tulip.profile.stats.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.ffa.AbstractFFAMatch;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Tulip
 * @date 31/07/2024 - 13:32
 */
@AllArgsConstructor
public class StatsMenu extends Menu {

    private OfflinePlayer offlineTargetPlayer;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&e&l" + offlineTargetPlayer.getName() + "'s Stats");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;

        buttons.put(4, new GlobalStatsButton(offlineTargetPlayer));

        for (AbstractFFAMatch match : Tulip.getInstance().getFfaRepository().getMatches()) {
            buttons.put(slot, new StatsButton(offlineTargetPlayer, match));
            slot++;
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @AllArgsConstructor
    public static class StatsButton extends Button {

        private OfflinePlayer offlineTargetPlayer;
        private AbstractFFAMatch match;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(offlineTargetPlayer.getUniqueId());
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name(CC.translate("&c&lError"))
                        .lore(CC.translate("&cThis Player has never played before."))
                        .hideMeta()
                        .build();
            }
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("&e&l● &eKills: &d" + profile.getStats().getKitKills(match.getKit()));
            lore.add("&e&l● &eDeaths: &d" + profile.getStats().getKitDeaths(match.getKit()));
            lore.add("");
            lore.add("&e&l● &eHighest Killstreak: &d" + profile.getStats().getHighestKillstreak(match.getKit()));
            lore.add("");

            return new ItemBuilder(match.getKit().getIcon())
                    .name(CC.translate("&d&l" + match.getKit().getName()))
                    .durability(match.getKit().getIconData())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }
    }

    @AllArgsConstructor
    public static class GlobalStatsButton extends Button {

        private OfflinePlayer offlineTargetPlayer;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Tulip.getInstance().getProfileRepository().getProfileWithNoAdding(offlineTargetPlayer.getUniqueId());
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            if (profile == null) {
                return new ItemBuilder(Material.BARRIER)
                        .name(CC.translate("&c&lError"))
                        .lore(CC.translate("&cThis Player has never played before."))
                        .hideMeta()
                        .build();
            }
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add("&e&l● &eTotal Kills: &d" + profile.getStats().getTotalKills());
            lore.add("&e&l● &eTotal Deaths: &d" + profile.getStats().getTotalDeaths());
            lore.add("");
            lore.add("&e&l● &eKill/Death Ratio: &d" + decimalFormat.format(profile.getStats().getKDR()));
            lore.add("");

            return new ItemBuilder(Material.NETHER_STAR)
                    .name(CC.translate("&d&lPlayer Stats"))
                    .durability(0)
                    .lore(lore)
                    .hideMeta()
                    .build();
        }
    }
}
