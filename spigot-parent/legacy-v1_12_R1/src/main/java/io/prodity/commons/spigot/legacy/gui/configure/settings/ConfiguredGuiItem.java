package io.prodity.commons.spigot.legacy.gui.configure.settings;

import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@EqualsAndHashCode(of = {"key"})
@Builder(builderClassName = "Builder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ConfiguredGuiItem {

    private final String key;
    private final boolean useSchemeClickSound;
    private final Optional<ImmutablePlayableSound> clickSound;
    private final Optional<ImmutableItemBuilder> itemBuilder;

    protected ConfiguredGuiItem(ConfiguredGuiItem item) {
        this.key = item.key;
        this.useSchemeClickSound = item.useSchemeClickSound;
        this.clickSound = item.clickSound;
        this.itemBuilder = item.itemBuilder;
    }

    public boolean isKey(String key) {
        return Objects.equals(this.key, key);
    }

    public ImmutableItemBuilder getItemBuilderOrAir() {
        return this.itemBuilder.orElseGet(() -> ImmutableItemBuilder.of(Material.AIR));
    }

    public ItemStack build() {
        return this.getItemBuilderOrAir().build();
    }

    public ItemStack build(Replacer replacer) {
        return this.getItemBuilderOrAir().build(replacer);
    }

    public ItemStack build(Player player, Replacer replacer) {
        return this.getItemBuilderOrAir().build(player, replacer);
    }

    public ItemStack papiBuild(Player player) {
        return this.getItemBuilderOrAir().papiBuild(player);
    }

    public ItemStack papiBuild(Player player, Replacer replacer) {
        return this.getItemBuilderOrAir().papiBuild(player, replacer);
    }

}