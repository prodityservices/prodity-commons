package io.prodity.commons.spigot.legacy.v1_12_R1;


import io.prodity.commons.spigot.legacy.gui.anvil.AnvilInputListener;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.ContainerAnvil;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.ICrafting;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.InventoryCraftResult;
import net.minecraft.server.v1_12_R1.InventorySubcontainer;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.PlayerInventory;
import net.minecraft.server.v1_12_R1.Slot;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;

public class ContainerAnvilImpl extends ContainerAnvil {

    public int a;
    IInventory resultInventory = new InventoryCraftResult();
    IInventory craftInventory = new InventorySubcontainer("Repair", true, 2) {

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

    public ContainerAnvilImpl(PlayerInventory playerinventory, World world, BlockPosition blockposition,
        EntityPlayer player) {
        super(playerinventory, world, blockposition, player);
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
        if (newName.startsWith(ChatColor.RESET.toString())) {
            newName = newName.substring(2);
        } else if (newName.trim().equals(ChatColor.COLOR_CHAR)) {
            newName = "";
        }
        String oldName = this.currentName;
        //		if (oldName.equals(newName)) {
        //			return;
        //		}
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
