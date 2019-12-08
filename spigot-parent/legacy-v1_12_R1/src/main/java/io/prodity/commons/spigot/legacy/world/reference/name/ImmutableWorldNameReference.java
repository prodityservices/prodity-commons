package io.prodity.commons.spigot.legacy.world.reference.name;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.world.reference.abstrakt.AbstractImmutableWorldReference;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ImmutableWorldNameReference extends AbstractImmutableWorldReference<String> implements WorldNameReference {

    public ImmutableWorldNameReference(@NonNull String worldName) {
        super(worldName);
    }

    @Override
    protected String worldToReference(World world) {
        return world.getName();
    }

    @Override
    protected World worldFromReference(String reference) {
        return Bukkit.getWorld(reference);
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

        if (!(object instanceof ImmutableWorldNameReference)) {
            return false;
        }

        return Objects.equal(this.worldReferent, ((ImmutableWorldNameReference) object).worldReferent);
    }


}