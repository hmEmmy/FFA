package me.emmy.tulip.feature.shop.command;

import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.feature.shop.menu.ShopMenu;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 08/09/2024 - 14:58
 */
public class ShopCommand extends BaseCommand {
    @Command(name = "shop")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        new ShopMenu().openMenu(player);
    }
}
