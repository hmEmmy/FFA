package me.emmy.tulip.profile.data.kitlayout.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.api.menu.button.BackButton;
import me.emmy.tulip.feature.hotbar.HotbarUtility;
import me.emmy.tulip.feature.kit.Kit;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import me.emmy.tulip.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 10/08/2024 - 20:21
 */
@AllArgsConstructor
public class KitLayoutEditorMenu extends Menu {

    private final FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorMenuConfig();
    private Kit kit;

    @Override
    public String getTitle(Player player) {
        return CC.translate(config.getString("title").replace("{kit}", kit.getName()));
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(config.getInt("back-button.slot"), new BackButton(new KitLayoutSelectionMenu()));
        buttons.put(config.getInt("buttons.load-current.slot"), new LoadCurrentLayoutButton(kit));
        buttons.put(config.getInt("buttons.save.slot"), new SaveButton(kit));
        buttons.put(config.getInt("buttons.reset.slot"), new ResetButton(kit));
        buttons.put(config.getInt("buttons.cancel.slot"), new CancelEditingProcessButton());

        if (config.getBoolean("glass-border.enabled")) {
            addBorder(buttons, (byte) config.getInt("glass-border.durability"), getSize() / 9);
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return config.getInt("rows") * 9;
    }

    @AllArgsConstructor
    public static class SaveButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorMenuConfig();
            return new ItemBuilder(Material.matchMaterial(config.getString("buttons.save.material")))
                    .name(config.getString("buttons.save.name"))
                    .lore(config.getStringList("buttons.save.lore"))
                    .durability(config.getInt("buttons.save.durability"))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            profile.getKitLayoutData().setLayout(kit.getName(), player.getInventory().getContents());
            profile.saveProfile();

            PlayerUtil.reset(player);
            player.closeInventory();
            player.sendMessage(CC.translate("&aKit layout saved."));
            HotbarUtility.applyHotbarItems(player);
            playSuccess(player);
        }
    }

    @AllArgsConstructor
    public static class ResetButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorMenuConfig();
            return new ItemBuilder(Material.matchMaterial(config.getString("buttons.reset.material")))
                    .name(config.getString("buttons.reset.name"))
                    .lore(config.getStringList("buttons.reset.lore"))
                    .durability(config.getInt("buttons.reset.durability"))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            player.getInventory().setContents(kit.getItems());
            player.sendMessage(CC.translate("&cGiving you the default kit layout."));
            playerClickSound(player);
        }
    }

    @AllArgsConstructor
    public static class LoadCurrentLayoutButton extends Button {

        private Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorMenuConfig();
            return new ItemBuilder(Material.matchMaterial(config.getString("buttons.load-current.material")))
                    .name(config.getString("buttons.load-current.name"))
                    .lore(config.getStringList("buttons.load-current.lore"))
                    .durability(config.getInt("buttons.load-current.durability"))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;
            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            ItemStack[] items = profile.getKitLayoutData().getLayout(kit.getName());

            if (items == null) {
                items = kit.getItems();
            }

            player.getInventory().setContents(items);
            player.sendMessage(CC.translate("&aLoaded your currently saved kit layout."));
            playNeutral(player);
        }
    }

    @AllArgsConstructor
    public static class CancelEditingProcessButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            FileConfiguration config = Tulip.getInstance().getConfigService().getKitEditorMenuConfig();
            return new ItemBuilder(Material.matchMaterial(config.getString("buttons.cancel.material")))
                    .name(config.getString("buttons.cancel.name"))
                    .lore(config.getStringList("buttons.cancel.lore"))
                    .durability(config.getInt("buttons.cancel.durability"))
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            PlayerUtil.reset(player);
            HotbarUtility.applyHotbarItems(player);
            player.closeInventory();
            player.sendMessage(CC.translate("&cEditing process cancelled."));
            playFail(player);
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