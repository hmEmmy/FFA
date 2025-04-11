package me.emmy.tulip.feature.kit.command.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.feature.kit.KitRepository;
import me.emmy.tulip.locale.Locale;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.api.command.BaseCommand;
import me.emmy.tulip.api.command.CommandArgs;
import me.emmy.tulip.api.command.annotation.Command;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project FFA
 * @date 27/07/2024 - 10:38
 */
public class KitSetInvCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setinv", permission = "Tulip.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setinv &b<name>"));
            return;
        }

        String name = args[0];

        if (Tulip.getInstance().getKitRepository().getKit(name) == null) {
            player.sendMessage(CC.translate(Locale.KIT_DOES_NOT_EXIST.getStringPath()).replace("{kit}", name));
            return;
        }

        KitRepository kitRepository = Tulip.getInstance().getKitRepository();

        kitRepository.getKit(name).setItems(player.getInventory().getContents());
        kitRepository.getKit(name).setArmor(player.getInventory().getArmorContents());
        kitRepository.saveKit(name);

        player.sendMessage(CC.translate(Locale.KIT_INVENTORY_SET.getStringPath()).replace("{kit}", name));
    }
}
