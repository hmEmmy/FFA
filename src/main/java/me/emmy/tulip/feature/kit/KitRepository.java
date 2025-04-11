package me.emmy.tulip.feature.kit;

import lombok.Getter;
import me.emmy.tulip.config.ConfigService;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 26/07/2024 - 22:00
 */
@Getter
public class KitRepository {
    private final List<Kit> kits = new ArrayList<>();

    /**
     * Load kits from the kits.yml file
     */
    public void loadKits() {
        FileConfiguration config = ConfigService.getInstance().getKitsConfig();

        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            return;
        }

        for (String name : kitsSection.getKeys(false)) {
            String key = "kits." + name;
            ItemStack[] inventory = config.getList(key + ".items").toArray(new ItemStack[0]);
            ItemStack[] armor = config.getList(key + ".armor").toArray(new ItemStack[0]);
            Material icon = Material.matchMaterial(config.getString(key + ".icon"));
            int iconData = config.getInt(key + ".icondata");

            List<String> debugMessage = Arrays.asList(
                "",
                "&aLoading kit: &d" + name,
                "&aDescription: &d" + config.getString(key + ".description"),
                "&aIcon: &d" + icon.name(),
                "&aIcon Data: &d" + iconData,
                ""
            );
            //debugMessage.forEach(msg -> Bukkit.getConsoleSender().sendMessage(CC.translate(msg)));

            Kit kit = new Kit(
                    name,
                    config.getString(key + ".description"),
                    inventory,
                    armor,
                    icon,
                    iconData,
                    config.getBoolean(key + ".enabled")
            );

            kits.add(kit);
        }
    }

    /**
     * Save kits to the kits.yml file
     */
    public void saveKits() {
        FileConfiguration config = ConfigService.getInstance().getKitsConfig();

        kits.forEach(kit -> {
            String key = "kits." + kit.getName();
            config.set(key + ".description", kit.getDescription());
            config.set(key + ".items", kit.getItems());
            config.set(key + ".armor", kit.getArmor());
            config.set(key + ".icon", kit.getIcon().name());
            config.set(key + ".icondata", kit.getIconData());
            config.set(key + ".enabled", kit.isEnabled());
        });

        File file = ConfigService.getInstance().getConfigFile("storage/kits.yml");
        ConfigService.getInstance().saveConfig(file, config);

        //Bukkit.getConsoleSender().sendMessage(CC.translate("&aSaved: &d" + kits.size() + " kits"));
    }

    /**
     * Save a kit to the kits.yml file
     *
     * @param kit The kit to save
     */
    public void saveKit(Kit kit) {
        FileConfiguration config = ConfigService.getInstance().getKitsConfig();

        String key = "kits." + kit.getName();
        config.set(key + ".description", kit.getDescription());
        config.set(key + ".items", kit.getItems());
        config.set(key + ".armor", kit.getArmor());
        config.set(key + ".icon", kit.getIcon().name());
        config.set(key + ".icondata", kit.getIconData());
        config.set(key + ".enabled", kit.isEnabled());

        File file = ConfigService.getInstance().getConfigFile("storage/kits.yml");
        ConfigService.getInstance().saveConfig(file, config);
    }

    /**
     * Save a kit by its name to the kits.yml file
     *
     * @param kitName The name of the kit to save
     */
    public void saveKit(String kitName) {
        Kit kit = getKit(kitName);
        if (kit == null) {
            return;
        }
        saveKit(kit);
    }

    /**
     * Get a kit by name
     *
     * @param name The name of the kit
     * @return The kit
     */
    public Kit getKit(String name) {
        return kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Create a kit and save it to the kits.yml file
     *
     * @param name The name of the kit
     * @param description The description of the kit
     * @param items The items in the kit
     * @param armor The armor in the kit
     * @param icon The icon of the kit
     * @param iconData The icon data of the kit
     * @param enabled Whether the kit is enabled
     */
    public void createKit(String name, String description, ItemStack[] items, ItemStack[] armor, Material icon, int iconData, boolean enabled) {
        Kit kit = new Kit(name, description, items, armor, icon, iconData, enabled);
        kits.add(kit);
        saveKit(kit);
    }

    /**
     * Delete a kit from the kits.yml file
     *
     * @param kit The kit to delete
     */
    public void deleteKit(Kit kit) {
        FileConfiguration config = ConfigService.getInstance().getConfig("storage/kits.yml");

        kits.remove(kit);
        config.set("kits." + kit.getName(), null);

        File file = ConfigService.getInstance().getConfigFile("storage/kits.yml");
        ConfigService.getInstance().saveConfig(file, config);
    }
}