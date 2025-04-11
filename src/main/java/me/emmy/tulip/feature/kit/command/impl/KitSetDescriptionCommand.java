package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 11:12
 */
public class KitSetDescriptionCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setdescription",aliases = "kit.setdesc", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setdescription &b<name> &b<description>"));
            return;
        }

        String name = args[0];
        String description = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", name));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setDescription(description);
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate(Locale.KIT_DESCRIPTION_SET.getStringPath()).replace("{kit}", name));
    }
}
