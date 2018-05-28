package io.prodity.commons.spigot.legacy.gui.configure;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import io.prodity.commons.spigot.legacy.gui.Gui;
import io.prodity.commons.spigot.legacy.gui.GuiItem;
import io.prodity.commons.spigot.legacy.gui.close.GuiCloseReason;
import io.prodity.commons.spigot.legacy.gui.configure.settings.ConfiguredGuiItem;
import io.prodity.commons.spigot.legacy.gui.configure.settings.ConfiguredGuiScheme;
import io.prodity.commons.spigot.legacy.gui.configure.settings.ConfiguredGuiSettings;
import io.prodity.commons.spigot.legacy.gui.scheme.MenuPopulator;
import io.prodity.commons.spigot.legacy.gui.scheme.MenuScheme;
import io.prodity.commons.spigot.legacy.gui.slot.Slot;
import io.prodity.commons.spigot.legacy.sound.playable.impl.ImmutablePlayableSound;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class ConfiguredGui<SELF extends ConfiguredGui<SELF>> extends Gui<SELF> {

    @Getter
    private final ConfiguredGuiSettings<SELF> settings;

    private final Table<String, String, Set<Slot>> schemeItemSlots;

    @Getter
    private final Multimap<String, Predicate<ConfiguredGuiScheme<SELF>>> schemeDrawConditions;

    @Getter
    private final Table<String, String, List<BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem>>> itemDrawConditions;

    public ConfiguredGui(UUID uniqueId, Plugin plugin, Player player, ConfiguredGuiSettings settings) {
        this(uniqueId, plugin, player, settings, settings.getTitle(player));
    }

    public ConfiguredGui(UUID uniqueId, Plugin plugin, Player player, ConfiguredGuiSettings settings, String intialTitle) {
        super(uniqueId, plugin, player, settings.getLines(), intialTitle);
        this.schemeItemSlots = HashBasedTable.create();
        this.itemDrawConditions = HashBasedTable.create();
        this.schemeDrawConditions = MultimapBuilder.hashKeys().arrayListValues().build();

        this.settings = settings;
    }

    protected void populateItemSlotMap() {
        for (Entry<String, ConfiguredGuiScheme<SELF>> entry : this.settings.getSchemes().entrySet()) {
            final String schemeKey = entry.getKey();
            for (String itemKey : entry.getValue().getItemKeys()) {
                if (!this.schemeItemSlots.contains(schemeKey, itemKey)) {
                    this.schemeItemSlots.put(schemeKey, itemKey, Sets.newHashSet());
                }
            }
        }
    }

    public boolean containsItem(String schemeKey, String itemKey) {
        return this.schemeItemSlots.contains(schemeKey, itemKey);
    }

    public boolean shouldDrawScheme(ConfiguredGuiScheme<SELF> scheme) {
        final Collection<Predicate<ConfiguredGuiScheme<SELF>>> conditions = this.schemeDrawConditions.get(scheme.getKey());
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }
        for (Predicate<ConfiguredGuiScheme<SELF>> predicate : conditions) {
            if (!predicate.test(scheme)) {
                return false;
            }
        }
        return true;
    }

    public boolean shouldDrawScheme(String schemeKey) {
        final ConfiguredGuiScheme<SELF> scheme = this.settings.getScheme(schemeKey);
        return this.shouldDrawScheme(scheme);
    }

    public boolean shouldDrawItem(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item) {
        if (!this.shouldDrawScheme(scheme)) {
            return false;
        }

        final List<BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem>> conditions = this.itemDrawConditions
            .get(scheme.getKey(), item.getKey());
        if (conditions == null || conditions.isEmpty()) {
            return true;
        }
        for (BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem> predicate : conditions) {
            if (!predicate.test(scheme, item)) {
                return false;
            }
        }
        return true;
    }

    public boolean shouldDrawItem(String schemeKey, String itemKey) {
        final ConfiguredGuiScheme<SELF> scheme = this.settings.getScheme(schemeKey);
        final ConfiguredGuiItem item = scheme.getItem(itemKey);
        return this.shouldDrawItem(scheme, item);
    }

    private Set<Slot> getSlots(String schemeKey, String itemKey) {
        return this.schemeItemSlots.get(schemeKey, itemKey);
    }

    private Set<Slot> getSlots(String schemeKey, ConfiguredGuiItem item) {
        return this.getSlots(schemeKey, item.getKey());
    }

    private Set<Slot> getSlots(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item) {
        return this.getSlots(scheme.getKey(), item.getKey());
    }

    private Set<Slot> getSlots(ConfiguredGuiScheme<SELF> scheme, String itemKey) {
        return this.getSlots(scheme.getKey(), itemKey);
    }

    public SELF bindSlotsIfPresent(ConfiguredGuiScheme<SELF> scheme, Runnable runnable, ClickType... clickTypes) {
        scheme.getItems().forEach((item) -> this.bindSlotsIfPresent(scheme, item, runnable, clickTypes));
        return this.getSelf();
    }

    public SELF bindSlotsIfPresent(String schemeKey, Runnable runnable, ClickType... clickTypes) {
        final ConfiguredGuiScheme<SELF> scheme = this.getSettings().getScheme(schemeKey);
        if (scheme != null) {
            scheme.getItemKeys().forEach((itemKey) -> this.bindSlotsIfPresent(scheme, itemKey, runnable, clickTypes));
        }
        return this.getSelf();
    }

    public SELF bindSlotsIfPresent(String schemeKey, ConfiguredGuiItem item, Runnable runnable, ClickType... clickTypes) {
        return this.bindSlotsIfPresent(schemeKey, item.getKey(), runnable, clickTypes);
    }

    public SELF bindSlotsIfPresent(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item, Runnable runnable, ClickType... clickTypes) {
        return this.bindSlotsIfPresent(scheme.getKey(), item.getKey(), runnable, clickTypes);
    }

    public SELF bindSlotsIfPresent(ConfiguredGuiScheme<SELF> scheme, String itemKey, Runnable runnable, ClickType... clickTypes) {
        return this.bindSlotsIfPresent(scheme.getKey(), itemKey, runnable, clickTypes);
    }

    public SELF bindSlotsIfPresent(String schemeKey, String itemKey, Runnable runnable, ClickType... clickTypes) {
        if (this.containsItem(schemeKey, itemKey)) {
            this.bindSlots(schemeKey, itemKey, runnable, clickTypes);
        }
        return this.getSelf();
    }

    public SELF bindSlots(ConfiguredGuiScheme<SELF> scheme, Runnable runnable, ClickType... clickTypes) {
        scheme.getItems().forEach((item) -> this.bindSlots(scheme, item, runnable, clickTypes));
        return this.getSelf();
    }

    public SELF bindSlots(String schemeKey, Runnable runnable, ClickType... clickTypes) {
        final ConfiguredGuiScheme<SELF> scheme = this.getSettings().getScheme(schemeKey);
        scheme.getItems().forEach((item) -> this.bindSlots(scheme, item, runnable, clickTypes));
        return this.getSelf();
    }

    public SELF bindSlots(ConfiguredGuiScheme<SELF> scheme, String itemKey, Runnable runnable, ClickType... clickTypes) {
        return this.bindSlots(scheme.getKey(), itemKey, runnable, clickTypes);
    }

    public SELF bindSlots(String schemeKey, ConfiguredGuiItem item, Runnable runnable, ClickType... clickTypes) {
        return this.bindSlots(schemeKey, item.getKey(), runnable, clickTypes);
    }

    public SELF bindSlots(String schemeKey, String itemKey, Runnable runnable, ClickType... clickTypes) {
        final ConfiguredGuiScheme<SELF> scheme = this.getSettings().getScheme(schemeKey);
        final ConfiguredGuiItem item = scheme.getItem(itemKey);
        return this.bindSlots(scheme, item, runnable, clickTypes);
    }

    public SELF bindSlots(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item, Runnable runnable, ClickType... clickTypes)
        throws NullPointerException {
        if (!this.isFirstDraw() && !scheme.isRedrawAlways()) {
            return this.getSelf();
        }

        if (!this.shouldDrawItem(scheme, item)) {
            return this.getSelf();
        }

        final ClickType[] types = clickTypes == null || clickTypes.length == 0 ? ClickType.values() : clickTypes;

        final Set<Slot> slots = this.getSlots(scheme.getKey(), item.getKey());
        if (slots == null) {
            throw new NullPointerException(
                "slots for scheme/item of " + scheme.getKey() + "/" + item.getKey() + " are not set for gui " + this.getClass().getName());
        }

        slots.forEach(slot -> slot.bind(runnable, types));
        return this.getSelf();
    }

    public SELF itemDrawCondition(String schemeKey, String itemKey, BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem> predicate) {
        List<BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem>> predicates = this.itemDrawConditions.get(schemeKey, itemKey);
        if (predicates != null) {
            predicates.add(predicate);
            return this.getSelf();
        }
        predicates = Lists.newArrayList(predicate);
        this.itemDrawConditions.put(schemeKey, itemKey, predicates);
        return this.getSelf();
    }

    public SELF itemDrawCondition(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item,
        BiPredicate<ConfiguredGuiScheme<SELF>, ConfiguredGuiItem> predicate) {
        return this.itemDrawCondition(scheme.getKey(), item.getKey(), predicate);
    }

    public SELF itemDrawCondition(String schemeKey, String itemKey, Supplier<Boolean> supplier) {
        return this.itemDrawCondition(schemeKey, itemKey, (s, i) -> supplier.get());
    }

    public SELF itemDrawCondition(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item, Supplier<Boolean> supplier) {
        return this.itemDrawCondition(scheme.getKey(), item.getKey(), (s, i) -> supplier.get());
    }

    public SELF schemeDrawCondition(String schemeKey, Predicate<ConfiguredGuiScheme<SELF>> predicate) {
        this.schemeDrawConditions.put(schemeKey, predicate);
        return this.getSelf();
    }

    public SELF schemeDrawCondition(ConfiguredGuiScheme<SELF> scheme, Predicate<ConfiguredGuiScheme<SELF>> predicate) {
        return this.schemeDrawCondition(scheme.getKey(), predicate);
    }

    public SELF schemeDrawCondition(String schemeKey, Supplier<Boolean> supplier) {
        this.schemeDrawConditions.put(schemeKey, (s) -> supplier.get());
        return this.getSelf();
    }

    public SELF schemeDrawCondition(ConfiguredGuiScheme<SELF> scheme, Supplier<Boolean> supplier) {
        return this.schemeDrawCondition(scheme.getKey(), (s) -> supplier.get());
    }

    @Override
    public void removeItem(int... slots) {
        for (int slot : slots) {
            this.clearSlot(slot);
        }
        super.removeItem(slots);
    }

    @Override
    public void removeItem(int slot) {
        this.clearSlot(slot);
        super.removeItem(slot);
    }

    @Override
    public void removeItem(Iterable<Integer> slots) {
        slots.forEach(this::clearSlot);
        super.removeItem(slots);
    }

    private void clearSlot(int index) {
        this.schemeItemSlots.values().forEach((slotSet) -> slotSet.removeIf((slot) -> slot.getId() != index));
    }

    @Override
    public void clearItems() {
        this.schemeItemSlots.clear();
        super.clearItems();
    }

    @Override
    public final void open() {
        super.open();
        this.getSettings().getOpenSound().ifPresent((sound) -> sound.play(this.getPlayer()));
        this.onOpen();
    }

    protected void onOpen() {

    }

    @Override
    public void redraw() {
        super.redraw();
        this.populateItemSlotMap();

        if (this.isFirstDraw()) {
            this.getSettings().getCloseSound().ifPresent((sound) -> {
                this.bind(() -> sound.play(this.getPlayer()), GuiCloseReason.FORCEFUL, GuiCloseReason.PLAYER_CLOSED);
            });
        }

        for (ConfiguredGuiScheme<SELF> guiScheme : this.settings.getSchemesAsCollection()) {
            if (!this.isFirstDraw() && !guiScheme.isRedrawAlways()) {
                continue;
            }

            if (!this.shouldDrawScheme(guiScheme)) {
                continue;
            }

            final String schemeKey = guiScheme.getKey();
            final MenuScheme scheme = guiScheme.getScheme();
            final MenuPopulator populator = scheme.populator(this);

            for (Iterator<String> itr = guiScheme.getItemKeysToApply(); itr.hasNext(); ) {
                final String itemKey = itr.next();

                if (!populator.hasSpace()) {
                    break;
                }

                final ConfiguredGuiItem guiItem = guiScheme.getItem(itemKey);

                if (!this.shouldDrawItem(guiScheme, guiItem)) {
                    continue;
                }

                final ItemStack itemStack = this.createItem(guiScheme, guiItem);

                final Optional<ImmutablePlayableSound> clickSound = guiScheme.getClickSound(guiItem);

                final GuiItem.Builder menuItemBuilder = GuiItem.builder(itemStack);
                clickSound.ifPresent((sound) -> {
                    menuItemBuilder.bind(() -> {
                        sound.play(this.getPlayer());
                    }, ClickType.values());
                });

                populator.accept(menuItemBuilder.build());

                final Slot slot = populator.getLastSlotUsed();
                this.schemeItemSlots.get(schemeKey, itemKey).add(slot);
            }
        }
    }

    protected ItemStack createItem(ConfiguredGuiScheme<SELF> scheme, ConfiguredGuiItem item) {
        return item.papiBuild(this.getPlayer());
    }

}