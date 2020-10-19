package io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button;

import com.google.common.collect.ImmutableMap;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.PaginatedInfo;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.message.replace.PlayerReplacer;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class PaginatedControlButton {

    @Builder(builderClassName = "Builder")
    public static class ButtonData {

        @Getter
        private final PaginatedButtonValidState state;

        private final ImmutableItemBuilder itemBuilder;

        private final ImmutablePlayableSound clickSound;

        public ItemStack buildItem(Player player, PaginatedInfo pageInfo) {
            final PlayerReplacer replacer = PlayerReplacer.createWithPapi()
                .add("%page-total%", () -> String.valueOf(pageInfo.getTotalPages()))
                .add("%page-current%", () -> String.valueOf(pageInfo.getCurrentPage() + 1))
                .add("%page-next%", () -> pageInfo.getNextPage().map(i -> i + 1).map(String::valueOf).orElse("none"))
                .add("%page-previous%", () -> pageInfo.getPreviousPage().map(i -> i + 1).map(String::valueOf).orElse("none"));
            return this.itemBuilder.build(player, replacer);
        }

        public Optional<ImmutablePlayableSound> getClickSound() {
            return Optional.ofNullable(this.clickSound);
        }

    }

    @Getter
    private final PaginatedButtonType type;
    @Getter
    private final int slot;
    @Getter
    private final ImmutableMap<PaginatedButtonValidState, ButtonData> data;

    public PaginatedControlButton(PaginatedButtonType type, int slot, ButtonData... buttonData) {
        this.type = type;
        this.slot = slot;

        final ImmutableMap.Builder<PaginatedButtonValidState, ButtonData> buttonBuilder = ImmutableMap.builder();

        for (ButtonData button : buttonData) {
            buttonBuilder.put(button.getState(), button);
        }

        this.data = buttonBuilder.build();
    }

    public ItemStack buildItem(Player player, PaginatedInfo info) {
        final PaginatedButtonValidState state = PaginatedButtonValidState.from(this.type, info);
        final ButtonData data = this.data.get(state);
        if (data == null) {
            return null;
        }
        return data.buildItem(player, info);
    }

    public ButtonData getData(PaginatedInfo info) {
        final PaginatedButtonValidState state = PaginatedButtonValidState.from(this.type, info);
        return this.data.get(state);
    }

}
