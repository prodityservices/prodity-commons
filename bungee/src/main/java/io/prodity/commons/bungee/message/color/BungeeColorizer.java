package io.prodity.commons.bungee.message.color;

import io.prodity.commons.message.color.Colorizer;
import net.md_5.bungee.api.ChatColor;
import org.jvnet.hk2.annotations.Service;

@Service
public class BungeeColorizer implements Colorizer {

    @Override
    public String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}