package me.emmy.tulip.game.menu;

import lombok.AllArgsConstructor;
import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.game.AbstractGame;
import me.emmy.tulip.api.menu.Button;
import me.emmy.tulip.api.menu.Menu;
import me.emmy.tulip.util.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project FFA
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ConfigService.getInstance().getGameMenuConfig().getString("title");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        if (ConfigService.getInstance().getGameMenuConfig().getBoolean("glass-border.enabled")) {
            addBorder(buttons, (byte) ConfigService.getInstance().getGameMenuConfig().getInt("glass-border.durability"), ConfigService.getInstance().getGameMenuConfig().getInt("rows"));

            int slot = 10;

            for (AbstractGame match : Tulip.getInstance().getGameRepository().getMatches()) {
                buttons.put(slot++, new FFAButton(match));
                //if (slot >= 54) break;
            }
        } else {
            int slot = 0;

            for (AbstractGame match : Tulip.getInstance().getGameRepository().getMatches()) {
                buttons.put(slot++, new FFAButton(match));
                //if (slot >= 54) break;
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return ConfigService.getInstance().getGameMenuConfig().getInt("rows") * 9;
    }

    @AllArgsConstructor
    public static class FFAButton extends Button {

        private final AbstractGame match;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            for (String line : ConfigService.getInstance().getGameMenuConfig().getStringList("button.lore")) {
                lore.add(line
                        .replace("{players}", String.valueOf(match.getPlayers().size()))
                        .replace("{max-players}", String.valueOf(match.getMaxPlayers()))
                        .replace("{arena}", match.getArena().getName())
                        .replace("{description}", match.getKit().getDescription())
                        .replace("{kit}", match.getKit().getName()));
            }

            return new ItemBuilder(match.getKit().getIcon())
                    .name(ConfigService.getInstance().getGameMenuConfig().getString("button.name")
                            .replace("{kit}", match.getKit().getName()))
                    .durability(match.getKit().getIconData())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType != ClickType.LEFT) return;
            playSuccess(player);
            match.join(player);
        }
    }
}
