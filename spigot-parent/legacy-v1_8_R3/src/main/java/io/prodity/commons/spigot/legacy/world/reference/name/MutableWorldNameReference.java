package io.prodity.commons.spigot.legacy.world.reference.name;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.world.reference.abstrakt.AbstractMutableWorldReference;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class MutableWorldNameReference extends AbstractMutableWorldReference<String> implements WorldNameReference {

    public MutableWorldNameReference(@NonNull String worldName) {
        super(worldName);
    }

    @Override
    protected String worldToReference(World world) {
        return world.getName();
    }

    @Override
    protected World worldFromReference(String worldReferent) {
        return Bukkit.getWorld(worldReferent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.worldReferent);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof MutableWorldNameReference)) {
            return false;
        }

        return Objects.equal(this.worldReferent, ((MutableWorldNameReference) object).worldReferent);
    }

}