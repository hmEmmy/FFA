package me.emmy.tulip.util;

import lombok.experimental.UtilityClass;
import me.emmy.tulip.Tulip;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Level;

/**
 * @author Emmy
 * @project FFA
 * @date 10/10/2024 - 18:36
 */
@UtilityClass
public class PluginUtil {
    /**
     * Register listeners in a specific package.
     *
     * @param packageName the package name
     */
    public void registerListenersInPackage(String packageName, String... excluded) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<? extends Listener>> classes = reflections.getSubTypesOf(Listener.class);

        for (Class<? extends Listener> clazz : classes) {
            boolean isExcluded = false;
            for (String excludedPackage : excluded) {
                if (clazz.getName().startsWith(excludedPackage)) {
                    isExcluded = true;
                    break;
                }
            }

            if (!isExcluded) {
                try {
                    Listener listener = clazz.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, Tulip.getInstance());
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&cRegistered listener: &f" + clazz.getSimpleName()));
                } catch (Exception e) {
                    //e.printStackTrace();
                    Bukkit.getLogger().log(Level.SEVERE, "Failed to register listener: " + clazz.getSimpleName(), e);
                }
            }
        }
    }
}