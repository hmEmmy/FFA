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
 * @date 27/07/2024 - 10:40
 */
public class KitToggleCommand extends BaseCommand {
    @Override
    @Command(name = "kit.toggle", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit toggle &b<name>"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", name));
            return;
        }

        Tulip.getInstance().getKitRepository().getKit(name).setEnabled(!Tulip.getInstance().getKitRepository().getKit(name).isEnabled());
        Tulip.getInstance().getKitRepository().saveKit(name);

        player.sendMessage(CC.translate(Locale.KIT_TOGGLED.getStringPath()).replace("{kit}", name).replace("{status}", Tulip.getInstance().getKitRepository().getKit(name).isEnabled() ? CC.translate("&aenabled") : CC.translate("&cdisabled")));
    }
}
