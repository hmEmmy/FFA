package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.feature.kit.Kit;
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
 * @date 27/07/2024 - 11:05
 */
public class KitInfoCommand extends BaseCommand {
    @Override
    @Command(name = "kit.info", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit info &b<name>"));
            return;
        }

        String name = args[0];

        Kit kit = Tulip.getInstance().getKitRepository().getKit(name);

        if (kit == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", name));
            return;
        }

        ItemStack[] items = kit.getItems();
        ItemStack[] armor = kit.getArmor();

        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lKit Information"));
        player.sendMessage(CC.translate(" &7- &dKit Name: &f" + kit.getName()));
        player.sendMessage(CC.translate(" &7- &dKit Description: &f" + kit.getDescription()));
        player.sendMessage(CC.translate(" &7- &dKit Icon: &f" + kit.getIcon().name() + "&7:&f" + kit.getIconData()));
        player.sendMessage(CC.translate(" &7- &dStatus: &f" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled")));
        player.sendMessage("");

        if (ConfigService.getInstance().getLocaleConfig().getBoolean("kit.extend-info-command")) {
            player.sendMessage(CC.translate("&d&lKit Items"));
            for (ItemStack item : items) {
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }

                player.sendMessage(CC.translate(" &7- &d" + item.getType().name() + " &7x &f" + item.getAmount()));
            }

            player.sendMessage(CC.translate("&d&lKit Armor"));
            for (ItemStack item : armor) {
                if (item == null || item.getType() == Material.AIR) {
                    continue;
                }

                player.sendMessage(CC.translate(" &7- &d" + item.getType().name() + " &7x &f" + item.getAmount()));
            }

            player.sendMessage("");
        }
    }
}
