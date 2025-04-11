package me.emmy.tulip.profile.data.kitlayout.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.feature.kit.KitRepository;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project FFA
 * @date 10/08/2024 - 19:59
 */
@AllArgsConstructor
public class KitLayoutSelectionMenu extends Menu {

    private final FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorSelectMenuConfig();

    @Override
    public String getTitle(Player player) {
        return CC.translate(config.getString("title"));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (config.getBoolean("glass-border.enabled")) {
            addBorder(buttons, (byte) config.getInt("glass-border.durability"), config.getInt("rows"));

            KitRepository kitRepository = Tulip.getInstance().getKitRepository();
            int slot = 10;

            for (Kit kit : kitRepository.getKits()) {
                buttons.put(slot++, new KitButton(kit));
                //if (slot >= 54) break;
            }
        } else {
            KitRepository kitRepository = Tulip.getInstance().getKitRepository();
            int slot = 0;

            for (Kit kit : kitRepository.getKits()) {
                buttons.put(slot++, new KitButton(kit));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return config.getInt("rows") * 9;
    }

    @AllArgsConstructor
    public static class KitButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorSelectMenuConfig();
            List<String> lore = config.getStringList("button.lore").stream()
                    .map(line -> line
                            .replace("{kit}", kit.getName())
                            .replace("{description}", kit.getDescription())
                    )
                    .collect(Collectors.toList());

            return new ItemBuilder(kit.getIcon())
                    .name(config.getString("button.name").replace("{kit}", kit.getName()))
                    .lore(lore)
                    .durability(kit.getIconData())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());

            new KitLayoutEditorMenu(kit).openMenu(player);
            player.getInventory().setContents(profile.getKitLayoutData().getLayout(kit.getName()));
            playerClickSound(player);
        }
    }

    @Override
    public boolean isUpdateAfterClick() {
        return false;
    }

    @Override
    public void onClose(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        HotbarUtility.applyHotbarItems(player);
    }
}
