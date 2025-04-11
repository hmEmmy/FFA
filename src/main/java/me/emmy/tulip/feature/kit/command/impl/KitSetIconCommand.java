package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 11:18
 */
public class KitSetIconCommand extends BaseCommand {
    @Override
    @Command(name = "kit.seticon", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit seticon &b<name>"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", name));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setIcon(player.getItemInHand().getType());
        Tulip.getInstance().getKitRepository().getKit(name).setIconData(player.getItemInHand().getDurability());
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate(Locale.KIT_ICON_SET.getStringPath()).replace("{kit}", name).replace("{icon}", player.getItemInHand().getType().name()));
    }
}
