package io.prodity.commons.spigot.legacy.v1_15_R1;

import com.google.common.collect.Lists;
import io.netty.channel.*;
import io.prodity.commons.inject.Eager;
import io.prodity.commons.inject.Export;
import io.prodity.commons.spigot.inject.McVersion;
import io.prodity.commons.spigot.inject.ProdityVersions;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilFactory;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilGUI;
import io.prodity.commons.spigot.legacy.gui.anvil.AnvilGuiInventory;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jvnet.hk2.annotations.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
@Export
@McVersion(ProdityVersions.V1_12)
public class AnvilFactoryImpl implements AnvilFactory, Listener, Eager {

    @Inject
    private JavaPlugin plugin;

    @Inject
    private BukkitScheduler scheduler;

    @Inject
    private Logger logger;

    private final List<AnvilGUI> anvilGuis = Lists.newArrayList();

    @Override
    public void registerGui(AnvilGUI gui) {
        this.anvilGuis.add(gui);
    }

    @Override
    public void unregisterGui(AnvilGUI gui) {
        this.anvilGuis.remove(gui);
    }

    @Override
    public void fakeSetItem(Player player, int slot, ItemStack itemStack) {
        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(entityPlayer.activeContainer.windowId, slot,
            CraftItemStack.asNMSCopy(itemStack));
        entityPlayer.playerConnection.sendPacket(packet);
    }

    @PreDestroy
    private void cleanup() {
        Lists.newArrayList(this.anvilGuis).forEach(AnvilGUI::close);
        this.anvilGuis.clear();
    }

    @PostConstruct
    private void injectServer() {
        this.logger.info("Injecting Anvil packet handler into all online players...");
        Bukkit.getOnlinePlayers().forEach(this::injectPlayer);
    }

    private void injectPlayer(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        NetworkManager network = entityPlayer.playerConnection.networkManager;

        Channel channel = network.channel;
        ChannelPipeline pipe = channel.pipeline();

        String priorHandler = this.getPriorHandlerName(pipe);
        if (priorHandler == null) {
            throw new IllegalStateException("was not able to find the priorHandler");
        }

        if (pipe.get("prime_anvilgui") == null) {
            pipe.addBefore(priorHandler, "prime_anvilgui", new Handler(entityPlayer));
        }
    }

    private String getPriorHandlerName(ChannelPipeline pipe) {
        if (pipe.get("packet_handler") != null) {
            return "packet_handler";
        }

        for (Map.Entry<String, ChannelHandler> entry : pipe.toMap().entrySet()) {
            if (entry.getValue() instanceof NetworkManager) {
                return entry.getKey();
            }
        }

        return null;
    }

    @Override
    public AnvilGuiInventory createInventory(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        ContainerAnvilImpl anvilContainer = new ContainerAnvilImpl(entityPlayer.inventory, entityPlayer.world,
            new BlockPosition(0, 0, 0),
            entityPlayer);

        AnvilGuiInventoryImpl inventory = (AnvilGuiInventoryImpl) anvilContainer.getBukkitView().getTopInventory();

        return inventory;
    }

    private class Handler extends ChannelDuplexHandler {

        private final EntityPlayer playerHandle;
        private final Thread mainThread;

        protected Handler(EntityPlayer playerHandle) {
            this.playerHandle = playerHandle;
            // The patch is applied in the main thread
            this.mainThread = Thread.currentThread();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof PacketPlayInCustomPayload) {
                PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) msg;
                if (packet.tag.getKey().equals("MC|ItemName")) {
                    if (Thread.currentThread() == this.mainThread) {
                        if (this.handlePacket(packet)) {
                            super.channelRead(ctx, msg);
                        }
                    } else {
                        AnvilFactoryImpl.this.scheduler.runTask(AnvilFactoryImpl.this.plugin, () -> {
                            if (this.handlePacket(packet)) {
                                try {
                                    super.channelRead(ctx, msg);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    return;
                }
            }
            super.channelRead(ctx, msg);
        }

        private boolean handlePacket(PacketPlayInCustomPayload msg) {
            if (!(this.playerHandle.activeContainer instanceof ContainerAnvilImpl)) {
                return true;
            }

            final ContainerAnvilImpl container = (ContainerAnvilImpl) this.playerHandle.activeContainer;

            String value = null;
            if ((msg.data != null) && (msg.data.readableBytes() >= 1)) {
                value = this.filterInput(msg.data.e(Short.MAX_VALUE));
                if (value.length() > 30) {
                    container.invalidNameReceived(value);
                    return false;
                }
            }
            if (value == null) {
                container.invalidNameReceived("");
                return false;
            }

            container.a(value);
            return false;
        }

        private String filterInput(String input) {
            final StringBuilder builder = new StringBuilder();
            for (char c : input.toCharArray()) {
                if (c >= ' ' && c != '\u0000') {
                    builder.append(c);
                }
            }
            return builder.toString();
        }

    }

}