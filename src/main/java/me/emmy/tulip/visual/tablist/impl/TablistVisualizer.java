package me.emmy.tulip.visual.tablist.impl;

import me.emmy.tulip.Tulip;
import me.emmy.tulip.config.ConfigService;
import me.emmy.tulip.util.CC;
import me.emmy.tulip.util.Logger;
import me.emmy.tulip.visual.tablist.ITablist;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project FFA
 * @date 07/09/2024 - 15:16
 */
public class TablistVisualizer implements ITablist {

    @Override
    public List<String> getHeader(Player player) {
        return ConfigService.getInstance().getTablistConfig().getStringList("tablist.header");
    }

    @Override
    public List<String> getFooter(Player player) {
        return ConfigService.getInstance().getTablistConfig().getStringList("tablist.footer");
    }

    @Override
    public void update(Player player) {
        if (Tulip.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getSettingsData().isShowTablist()) {
            List<String> headerLines = getHeader(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            List<String> footerLines = getFooter(player).stream()
                    .map(CC::translate)
                    .collect(Collectors.toList());

            String headerText = String.join("\n", headerLines);
            String footerText = String.join("\n", footerLines);

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(headerText));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(footerText));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.logError("Failed to update tablist for " + player.getName());
            }
        } else {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
            try {
                Field headerField = packet.getClass().getDeclaredField("a");
                headerField.setAccessible(true);
                headerField.set(packet, new ChatComponentText(""));

                Field footerField = packet.getClass().getDeclaredField("b");
                footerField.setAccessible(true);
                footerField.set(packet, new ChatComponentText(""));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Exception e) {
                Logger.logError("Failed to update tablist for " + player.getName());
            }
        }
    }
}
