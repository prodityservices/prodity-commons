package io.prodity.commons.spigot.legacy.placeholder;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @author TehNeon
 * @since 8/19/2017 <p> A Helper class which utilizes PlaceholderAPI to set placeholders from the PlaceholderAPI plugin.
 */
public class PlaceholderHelper {

    private static boolean usingPlaceholderAPI = false;
    private static boolean tested = false;

    /**
     * This method will test and see if we are actually running the PlaceholderAPI plugin, and if we aren't we will
     * prevent parsing any PlaceholderAPI based text/messages.
     */
    public static boolean testPlaceholderAPI() {
        Plugin placeholderApiPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderApiPlugin != null) {
            if (placeholderApiPlugin instanceof PlaceholderAPIPlugin) {
                PlaceholderHelper.usingPlaceholderAPI = true;

                Bukkit.getLogger().info("We tested to find PlaceholderAPI and we found it!");
            } else {
                Bukkit.getLogger()
                        .severe(
                                "We tested to find PlaceholderAPI and we found it, but it's not the right version or a different plugin in general.");
            }
        } else {
            Bukkit.getLogger().warning("We tested to find PlaceholderAPI and we couldn't find it!");
        }

        return PlaceholderHelper.usingPlaceholderAPI;
    }

    /**
     * This will filter the text which is inputted and return a version which has been altered by PlaceholderAPI.
     */
    public static String setPlaceholders(Player player, String text) {
        test();

        if (PlaceholderHelper.usingPlaceholderAPI) {
            return PlaceholderAPI.setPlaceholders(player, text);
        } else {
            return text;
        }
    }

    /**
     * This will filter text from a List, and will return a version which has been altered by PlaceholderAPI.
     */
    public static List<String> setPlaceholders(Player player, List<String> text) {
        test();

        if (PlaceholderHelper.usingPlaceholderAPI) {
            return PlaceholderAPI.setPlaceholders(player, text);
        } else {
            return text;
        }
    }

    private static void test() {
        if (tested == false) {
            testPlaceholderAPI();
            tested = true;
        }
    }
}
