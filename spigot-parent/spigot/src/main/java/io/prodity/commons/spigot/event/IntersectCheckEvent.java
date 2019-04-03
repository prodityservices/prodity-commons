package io.prodity.commons.spigot.event;

import io.prodity.commons.spigot.model.RegionReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event used for checking if a region intersects an existing region. Plugins that listen to this
 * event should provide the region that is intersected, along with the name of that region.
 *
 * @see RegionReference
 */
public class IntersectCheckEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final RegionReference region;
    private final String regionName;

    private final Map<String, RegionReference> intersectRegions = new HashMap<>();

    public IntersectCheckEvent(RegionReference region, String regionName) {
        this.region = region;
        this.regionName = regionName;
    }

    public RegionReference getRegion() {
        return this.region;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public boolean intersects() {
        return !this.intersectRegions.isEmpty();
    }

    public void addIntersectRegion(RegionReference region, String regionName) {
        this.intersectRegions.put(regionName, region);
    }

    public Map<String, RegionReference> getIntersectRegions() {
        return this.intersectRegions;
    }

    public List<String> getIntersectNames() {
        return new ArrayList<>(this.intersectRegions.keySet());
    }

    @Override
    public HandlerList getHandlers() {
        return IntersectCheckEvent.HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return IntersectCheckEvent.HANDLERS;
    }

}
