package io.prodity.commons.spigot.legacy.gui.anvil.input;

import com.google.common.base.Preconditions;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilFactory;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilGUI;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilSlot;
import io.prodity.commons.spigot.legacy.gui.anvil.click.GUIClickable;
import io.prodity.commons.spigot.legacy.item.builder.ItemBuilder;
import io.prodity.commons.spigot.legacy.item.repo.ItemRepository;
import io.prodity.commons.spigot.legacy.message.replace.Replacer;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AnvilInputGui<T> extends AnvilGUI {

    private final ItemRepository items;
    private final AnvilInputData<T> data;

    public AnvilInputGui(
        ItemRepository items,
        AnvilFactory anvilFactory,
        JavaPlugin plugin,
        AnvilInputData<T> data
    ) {
        super(anvilFactory, plugin);
        Preconditions.checkNotNull(items, "items");
        Preconditions.checkNotNull(data, "data");
        this.items = items;
        this.data = data;

        this.addClickable(new GUIClickable(guiEvent -> {
            final InventoryClickEvent clickEvent = guiEvent.getWrappedEvent();
            clickEvent.setCancelled(true);
            clickEvent.setResult(Event.Result.DENY);

            final String currentText = this.getCurrentText();
            final AnvilInputResult<T> result = this.data.parseResult(currentText);

            if (result.isValid()) {
                this.data.callSuccessCallback(this, result.getValue());
            } else {
                this.data.callFailureCallback(this, result.getErrorKey());
            }
        }, AnvilSlot.OUTPUT));
    }

    @Override
    public void handleInput(String oldText) {
        super.handleInput(oldText);
        this.data.callInputCallback();
    }

    @Override
    public void onClose(CloseReason closeReason) {
        this.data.callCloseCallbacks(closeReason);
    }

    @Override
    public ItemStack getInputItem() {
        final AnvilInputResult<T> result = this.data.parseResult(this.getCurrentText());
        final Replacer replacer = this.createReplacer(result);
        final String itemKey = this.data.getInputItemKey();
        return this.items.get(itemKey).build(this.getPlayer(), replacer);
    }

    @Override
    public ItemStack getOutputItem() {
        final AnvilInputResult<T> result = this.data.parseResult(this.getCurrentText());

        final ItemBuilder itemBuilder;
        if (result.isValid()) {
            final String itemKey = this.data.getOutputValidItemKey();
            itemBuilder = this.items.get(itemKey);
        } else {
            itemBuilder = this.items.get(result.getErrorKey());
        }

        final Replacer replacer = this.createReplacer(result);
        return itemBuilder.build(this.getPlayer(), replacer);
    }

    private Replacer createReplacer(AnvilInputResult<T> result) {
        Preconditions.checkNotNull(result, "result");
        final Replacer replacer = Replacer.createWithPapi().add("%current_text%", this::getCurrentText);
        this.data.modifyReplacer(replacer, result);
        return replacer;
    }

}