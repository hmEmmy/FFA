package me.emmy.tulip.api.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.tulip.util.ItemBuilder;
import me.emmy.tulip.api.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 15/06/2024 - 23:57
 */
@AllArgsConstructor
public class CommandButton extends Button {

    private String command;
    private ItemStack itemStack;
    private List<String> lore;


    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(itemStack)
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) {
            return;
        }

        player.performCommand(command);
    }
}
