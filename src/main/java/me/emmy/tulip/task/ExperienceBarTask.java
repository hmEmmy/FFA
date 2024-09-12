package me.emmy.tulip.task;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.cooldown.Cooldown;
import me.emmy.tulip.cooldown.CooldownRepository;
import me.emmy.tulip.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * @author Emmy
 * @project Tulip
 * @date 08/09/2024 - 23:19
 */
public class ExperienceBarTask implements Runnable {

    //TODO: FIX THIS RETARDED CLASS

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            CooldownRepository cooldownRepository = Tulip.getInstance().getCooldownRepository();
            Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
            if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&aI'm not killing performance right now"));
                Cooldown cooldown = optionalCooldown.get();
                double seconds = cooldown.remainingTime();
                player.setLevel((int) seconds);
                player.setExp((float) (seconds / 20));
            } else {
                player.setLevel(0);
                player.setExp(0);
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cI'm killing performance right now"));
                Bukkit.getScheduler().cancelTask(this.hashCode());
            }
        }
    }
}
