package io.prodity.commons.spigot.legacy.message.composite;

import io.prodity.commons.spigot.legacy.config.yaml.repo.YamlRepository;
import io.prodity.commons.spigot.legacy.config.yaml.types.primitive.YamlColorizedString;
import io.prodity.commons.spigot.legacy.message.send.TitleTimes;
import java.io.File;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

public class MessageCompositeRepository extends YamlRepository<MessageComposite> {

    public MessageCompositeRepository(Plugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public MessageCompositeRepository(File file) {
        super(file);
    }

    @Override
    protected boolean shouldLoad(ConfigurationSection section, String path) {
        if (!section.isConfigurationSection(path)) {
            return false;
        }
        final ConfigurationSection compositeSection = section.getConfigurationSection(path);
        return compositeSection.isString(Keys.CHAT)
            || compositeSection.isString(Keys.ACTION_BAR)
            || compositeSection.isString(Keys.TITLE)
            || compositeSection.isString(Keys.SUB_TITLE);
    }

    @Override
    protected MessageComposite load(ConfigurationSection section, String path) {
        final ConfigurationSection compositeSection = section.getConfigurationSection(path);

        final MessageComposite.Builder builder = MessageComposite.builder();
        builder.chatMessage(YamlColorizedString.get().loadOrDefault(compositeSection, Keys.CHAT, null));
        builder.actionBarMessage(YamlColorizedString.get().loadOrDefault(compositeSection, Keys.ACTION_BAR, null));
        builder.titleMessage(YamlColorizedString.get().loadOrDefault(compositeSection, Keys.TITLE, null));
        builder.subTitleMessage(YamlColorizedString.get().loadOrDefault(compositeSection, Keys.SUB_TITLE, null));

        if (compositeSection.contains(Keys.TITLE_TIMES)) {
            final TitleTimes times = TitleTimes.of();
            final ConfigurationSection timesSection = compositeSection.getConfigurationSection(Keys.TITLE_TIMES);
            if (timesSection.isInt(Keys.TITLE_TIMES_SHOW)) {
                times.stay(timesSection.getInt(Keys.TITLE_TIMES_SHOW));
            }
            if (timesSection.isInt(Keys.TITLE_TIMES_STAY)) {
                times.stay(timesSection.getInt(Keys.TITLE_TIMES_STAY));
            }
            if (timesSection.isInt(Keys.TITLE_TIMES_FADE)) {
                times.stay(timesSection.getInt(Keys.TITLE_TIMES_FADE));
            }
            builder.titleTimes(times);
        }

        return builder.build();
    }

    public enum Keys {
        ;

        public static final String CHAT = "chat";
        public static final String ACTION_BAR = "action-bar";
        public static final String TITLE = "title";
        public static final String SUB_TITLE = "sub-title";
        public static final String TITLE_TIMES = "title-times";
        public static final String TITLE_TIMES_SHOW = "show";
        public static final String TITLE_TIMES_STAY = "stay";
        public static final String TITLE_TIMES_FADE = "fade";

    }

}