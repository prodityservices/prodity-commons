package io.prodity.commons.spigot.message.color;

import io.prodity.commons.effect.message.color.Colorizer;
import io.prodity.commons.inject.Export;
import org.bukkit.ChatColor;
import org.jvnet.hk2.annotations.Service;

@Service
@Export
public class SpigotColorizer implements Colorizer {

    @Override
    public String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}