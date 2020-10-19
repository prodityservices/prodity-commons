package io.prodity.commons.spigot.legacy.v1_15_R1;

import io.prodity.commons.spigot.legacy.gui.anvil.AnvilInputListener;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftInventoryView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.BiFunction;

public class ContainerAnvilImpl extends ContainerAnvil {

    public int a;
    IInventory resultInventory = new InventoryCraftResult();
    IInventory craftInventory = new InventorySubcontainer(2) {

        @Override
        public void update() {
            super.update();
            ContainerAnvilImpl.this.a(this);
        }
    };
    World world;
    BlockPosition position;
    String currentName;
    EntityPlayer player;
    CraftInventoryView bukkitEntity = null;
    PlayerInventory playerInventory;

    Collection<AnvilInputListener> listeners = new HashSet<>(3);

    public ContainerAnvilImpl(PlayerInventory playerinventory, World world, BlockPosition blockposition, EntityPlayer player) {
        super(player.nextContainerCounter(), playerinventory, new ContainerAccess() {
            @Override
            public <T> Optional<T> a(BiFunction<World, BlockPosition, T> biFunction) {
                return Optional.of(biFunction.apply(world, blockposition));
            }
        });
        this.slots.clear();
        this.items.clear();
        this.playerInventory = playerinventory;
        this.position = blockposition;
        this.world = world;
        this.player = player;
        this.a(new Slot(this.craftInventory, 0, 27, 47));
        this.a(new Slot(this.craftInventory, 1, 76, 47));
        this.a(new Slot(ContainerAnvilImpl.this.resultInventory, 2, 134, 47) {

            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public boolean isAllowed(EntityHuman entityhuman) {
                return false;
            }

        });

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.a(new Slot(playerinventory, j + (i * 9) + 9, 8 + (j * 18), 84 + (i * 18)));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new Slot(playerinventory, i, 8 + (i * 18), 142));
        }

    }

    @Override
    public void a(IInventory iinventory) {
        super.a(iinventory);
        if (iinventory == this.craftInventory) {
            this.e();
        }

    }

    @Override
    public void e() {
    }

    @Override
    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.a);
    }

    @Override
    public void b(EntityHuman entityhuman) {
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        return null;
    }

    public void invalidNameReceived(String name) {
        this.listeners.forEach(l -> l.onInvalidName(name));
    }

    @Override
    public void a(String newName) {
        final String oldName = this.currentName;
        this.currentName = newName;
        this.listeners.forEach((listener) -> listener.onNameChange(oldName, this.currentName));
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        } else {
            CraftInventory inventory = new AnvilGuiInventoryImpl(this);
            this.bukkitEntity = new CraftInventoryView(this.playerInventory.player.getBukkitEntity(), inventory, this);
            return this.bukkitEntity;
        }
    }

}
