package me.emmy.tulip.feature.shop.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.feature.product.Product;
import me.emmy.tulip.profile.Profile;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 08/09/2024 - 14:57
 */
@AllArgsConstructor
public class ShopMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&e&lShop Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        int slot = 10;
        for (Product productos : Tulip.getInstance().getProductRepository().getProducts()) {
            buttons.put(slot++, new ShopMenuButton(productos));
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44) {
                slot += 2;
            }
        }

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @AllArgsConstructor
    public static class ShopMenuButton extends Button {
        private Product product;

        @Override
        public ItemStack getButtonItem(Player player) {
            if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getOwnedProducts().contains(this.product.getName())) {
                return new ItemBuilder(this.product.getIcon())
                        .name(CC.translate("&a&l" + this.product.getDisplayName()))
                        .durability(this.product.getDurability())
                        .lore(CC.translate("&aYou already own this product."))
                        .hideMeta()
                        .build();
            }
            return new ItemBuilder(this.product.getIcon())
                    .name(this.product.getDisplayName())
                    .durability(this.product.getDurability())
                    .lore(this.product.getDescription())
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            Profile profile = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            if (profile.getOwnedProducts().contains(this.product.getName())) {
                player.sendMessage(CC.translate("&cYou already own this product."));
                return;
            }

            int currentCoins = Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getCoinsData().getCoins();
            int remainingCoins = currentCoins - this.product.getPrice();

            if (remainingCoins < 0) {
                player.sendMessage(CC.translate("&cYou do not have enough coins to purchase this item."));
                return;
            }

            player.sendMessage(CC.translate("&aPurchased the product &e" + this.product.getDisplayName() + "&a."));
            player.sendMessage(CC.translate("&c-" + this.product.getPrice() + " &r| &a" + remainingCoins + " coins remaining."));

            profile.getCoinsData().removeCoins(this.product.getPrice());
            profile.getOwnedProducts().add(this.product.getName());
            profile.saveProfile();
        }
    }
}