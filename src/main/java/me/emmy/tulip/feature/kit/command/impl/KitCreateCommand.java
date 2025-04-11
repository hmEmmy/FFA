package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 22:26
 */
public class KitCreateCommand extends BaseCommand {
    @Override
    @Command(name = "kit.create", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit create &b<name>"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) != null) {
            player.sendMessage(CC.translate(Locale.KIT_ALREADY_EXISTS.getStringPath()).replace("{kit}", name));
            return;
        }

        ItemStack[] items = player.getInventory().getContents();
        ItemStack[] armor = player.getInventory().getArmorContents();

        String defaultDescription = Locale.KIT_DEFAULT_DESCRIPTION.getStringPath().replace("{kit}", name);

        Tulip.getInstance().getKitRepository().createKit(name, defaultDescription, items, armor, Material.DIAMOND_AXE, 0, false);
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate(Locale.KIT_CREATED.getStringPath()).replace("{kit}", name));
        player.sendMessage(CC.translate(" &7TIP: Your current inventory will be set as the kit's inventory content. You can change it by using &b/kit setinv " + name + "&7&o."));
    }
}
