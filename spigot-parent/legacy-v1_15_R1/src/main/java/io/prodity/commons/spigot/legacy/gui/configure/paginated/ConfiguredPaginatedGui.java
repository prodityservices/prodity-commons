package io.prodity.commons.spigot.legacy.gui.configure.paginated;

import com.google.common.collect.ImmutableList;
import io.prodity.commons.spigot.legacy.gui.configure.ConfiguredGui;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.ConfiguredPaginatedSettings;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.PaginatedInfo;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedButtonType;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedButtonValidState;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedControlButton;
import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button.PaginatedControlButton.ButtonData;
import io.prodity.commons.spigot.legacy.gui.slot.Slot;
import io.prodity.commons.spigot.legacy.item.builder.ImmutableItemBuilder;
import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public abstract class ConfiguredPaginatedGui<SELF extends ConfiguredPaginatedGui<SELF, T>, T> extends ConfiguredGui<SELF> {

    private final LazyValue<PaginatedInfo> info;

    @Getter
    private ImmutableList<T> elements;

    private int currentPage;

    @Getter
    private boolean drawElements;

    public ConfiguredPaginatedGui(UUID uniqueId, Plugin plugin, Player player, ConfiguredPaginatedSettings<SELF> settings) {
        this(uniqueId, plugin, player, settings, settings.getTitle(player));
    }

    public ConfiguredPaginatedGui(UUID uniqueId, Plugin plugin, Player player, ConfiguredPaginatedSettings<SELF> settings, String title) {
        super(uniqueId, plugin, player, settings, title);
        this.currentPage = 0;
        this.info = new SimpleLazyValue<>(this::createInfo);
        this.drawElements = true;
    }

    @Override
    public ConfiguredPaginatedSettings<SELF> getSettings() {
        return (ConfiguredPaginatedSettings<SELF>) super.getSettings();
    }

    public int getElementsSize() {
        return this.elements == null ? 0 : this.elements.size();
    }

    public boolean isElementsEmpty() {
        return this.elements == null || this.elements.isEmpty();
    }

    public void setDrawElements(boolean drawElements) {
        this.drawElements = drawElements;
        this.info.update();
    }

    public int calculateTotalPages() {
        if (this.elements == null) {
            return 0;
        }
        final int totalElements = this.elements.size();
        final int elementsPerPage = this.getSettings().getElementSlots().size();
        return (int) Math.ceil((double) totalElements / (double) elementsPerPage);
    }

    public PaginatedInfo createInfo() {
        final PaginatedInfo.Builder builder = PaginatedInfo
            .builder();
        builder.currentPage(this.currentPage);
        builder.drawElements(this.drawElements);

        final int totalPages = this.calculateTotalPages();
        builder.totalPages(totalPages);

        final Optional<Integer> nextPage = Optional.of(this.currentPage + 1).filter(i -> i < totalPages);
        builder.nextPage(nextPage);

        final Optional<Integer> previousPage = Optional.of(this.currentPage - 1).filter(i -> i >= 0);
        builder.previousPage(previousPage);

        return builder.build();
    }

    public void open(Collection<T> elements) {
        this.open(ImmutableList.copyOf(elements));
    }

    public void open(ImmutableList<T> elements) {
        if (this.isValid()) {
            throw new IllegalStateException("gui is already open");
        }
        this.elements = elements;
        this.info.update();
        this.open();
    }

    public void updateElements(Collection<T> elements) {
        this.updateElements(ImmutableList.copyOf(elements));
    }

    public void updateElements(ImmutableList<T> elements) {
        this.elements = elements;
        this.info.update();
        this.setPage(this.currentPage);
    }

    @Override
    public void redraw() {
        for (int slotIndex : this.getSettings().getElementSlots()) {
            final Slot slot = this.getSlot(slotIndex);
            if (slot != null) {
                slot.clear();
            }
        }

        if (this.isFirstDraw()) {
            this.setPage(this.currentPage);
        }

        if (this.isDrawElements()) {
            final int totalSlots = this.getSettings().getElementSlots().size();
            final int startIndex = this.currentPage * totalSlots;
            final ImmutableItemBuilder elementItemBuilder = this.getSettings().getElementItemBuilder();
            for (int i = 0; i < totalSlots; i++) {
                final int elementIndex = startIndex + i;
                if (elementIndex >= this.elements.size()) {
                    break;
                }
                final int slotNumber = this.getSettings().getElementSlots().get(i);
                final Slot slot = this.getSlot(slotNumber);
                slot.clear();

                final T element = this.elements.get(elementIndex);
                final ItemStack itemStack = this.getElementAsItem(elementItemBuilder, element);
                slot.setItem(itemStack).bind((event) -> {
                    event.setCancelled(true);
                    this.getSettings().getElementClickSound().ifPresent((sound) -> sound.play(this.getPlayer()));
                    this.onElementClick(element, event);
                }, ClickType.values());
            }
        }

        super.redraw();

        this.addButton(PaginatedButtonType.NEXT_PAGE, () -> this.setPage(this.currentPage + 1));
        this.addButton(PaginatedButtonType.PREVIOUS_PAGE, () -> this.setPage(this.currentPage - 1));
    }

    private void addButton(PaginatedButtonType type, Runnable onSuccess) {
        final PaginatedControlButton button = this.getSettings().getButtons().get(type);
        final ItemStack itemStack = button.buildItem(this.getPlayer(), this.info.get());

        final Slot slot = this.getSlot(button.getSlot());
        slot.clear();
        slot.setItem(itemStack).bind(() -> {
            final ButtonData data = button.getData(this.info.get());

            data.getClickSound().ifPresent((sound) -> sound.play(this.getPlayer()));

            if (data.getState() == PaginatedButtonValidState.VALID) {
                onSuccess.run();
            }
        }, ClickType.values());
    }

    public void setPage(int page) {
        final PaginatedInfo info = this.info.get();
        if (page != 0 && page >= info.getTotalPages()) {
            page = info.getTotalPages() - 1;
        } else if (page < 0) {
            page = 0;
        }

        this.currentPage = page;
        this.info.update();
        if (this.isValid()) {
            this.redraw();
        }
    }

    public ItemStack getElementAsItem(ImmutableItemBuilder itemBuilder, T element) {
        return itemBuilder.papiBuild(this.getPlayer());
    }

    protected abstract void onElementClick(T element, InventoryClickEvent event);

}