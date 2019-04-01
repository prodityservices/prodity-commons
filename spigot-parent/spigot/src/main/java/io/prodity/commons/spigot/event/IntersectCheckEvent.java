package io.prodity.commons.spigot.event;

import io.prodity.commons.spigot.model.RegionReference;
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

    private RegionReference intersectRegion;
    private String intersectName;

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
        return this.intersectRegion != null;
    }

    public void setIntersectRegion(RegionReference region, String regionName) {
        this.intersectRegion = region;
        this.intersectName = regionName;
    }

    public RegionReference getIntersectRegion() {
        return this.intersectRegion;
    }

    public String getIntersectName() {
        return this.intersectName;
    }

    @Override
    public HandlerList getHandlers() {
        return IntersectCheckEvent.HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return IntersectCheckEvent.HANDLERS;
    }

}
