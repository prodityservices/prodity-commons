package io.prodity.commons.spigot.legacy.chunk.anchor;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import io.prodity.commons.spigot.legacy.chunk.reference.ChunkLocation;
import io.prodity.commons.spigot.legacy.chunk.reference.ChunkReferences;
import io.prodity.commons.spigot.legacy.plugin.Initializable;
import io.prodity.commons.spigot.legacy.plugin.Terminable;
import io.prodity.commons.spigot.legacy.tryto.Try;
import java.util.Collection;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class ChunkPersister implements Listener, Runnable, Initializable, Terminable {

    private final Plugin plugin;
    private final Multimap<ChunkLocation, ChunkAnchor> anchors;

    @Getter
    private final int periodInTicks;

    private BukkitTask bukkitTask;

    public ChunkPersister(Plugin plugin) {
        this(plugin, 0);
    }

    public ChunkPersister(Plugin plugin, int periodInTicks) {
        this.plugin = plugin;
        this.periodInTicks = periodInTicks;
        this.anchors = MultimapBuilder.hashKeys().hashSetValues().build();
    }

    @Override
    public void initialize() {
        if (this.bukkitTask != null) {
            this.bukkitTask.cancel();
        }

        Bukkit.getPluginManager().registerEvents(this, this.plugin);
        if (this.periodInTicks > 0) {
            this.bukkitTask = Bukkit.getScheduler().runTaskTimer(this.plugin, this, this.periodInTicks, this.periodInTicks);
        }
    }

    @Override
    public void terminate() {
        if (this.bukkitTask != null) {
            this.bukkitTask.cancel();
            this.bukkitTask = null;
        }
        HandlerList.unregisterAll(this);
    }

    public boolean addAnchor(ChunkLocation chunkLocation, ChunkAnchor anchor) {
        boolean added = this.anchors.put(chunkLocation, anchor);
        chunkLocation.getBukkitChunk().load(true);
        return added;
    }

    public boolean removeAnchor(ChunkAnchor anchor) {
        boolean contains = false;
        while (this.anchors.values().contains(anchor)) {
            contains = true;
            this.anchors.values().remove(anchor);
        }
        return contains;
    }

    public boolean removeAnchor(ChunkLocation chunkLocation, ChunkAnchor anchor) {
        return this.anchors.remove(chunkLocation, anchor);
    }

    public Collection<ChunkAnchor> removeChunk(ChunkLocation chunkLocation) {
        return this.anchors.removeAll(chunkLocation);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        final ChunkLocation chunkLocation = ChunkReferences.immutableFactory().fromChunk(event.getChunk());
        if (this.anchors.containsKey(chunkLocation)) {
            event.setCancelled(true);
        }
    }

    @Override
    public void run() {
        this.anchors.keySet().stream()
            .map(ChunkLocation::getBukkitChunk)
            .filter(Try.negate(Chunk::isLoaded))
            .forEach(chunk -> chunk.load(true));
    }

}