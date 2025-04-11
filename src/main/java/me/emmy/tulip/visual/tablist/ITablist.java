package me.emmy.tulip.visual.tablist;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Emmy
 * @project FFA
 * @date 07/09/2024 - 15:17
 */
public interface ITablist {

    List<String> getHeader(Player player);

    List<String> getFooter(Player player);

    void update(Player player);
}
