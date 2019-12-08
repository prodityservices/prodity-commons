package io.prodity.commons.spigot.legacy.world.reference.uid;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.world.reference.abstrakt.AbstractImmutableWorldReference;
import java.util.UUID;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class ImmutableWorldUidReference extends AbstractImmutableWorldReference<UUID> implements WorldUidReference {

    public ImmutableWorldUidReference(@NonNull UUID uuid) {
        super(uuid);
    }

    @Override
    protected UUID worldToReference(World world) {
        return world.getUID();
    }

    @Override
    protected World worldFromReference(UUID reference) {
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

        if (!(object instanceof ImmutableWorldUidReference)) {
            return false;
        }

        return Objects.equal(this.worldReferent, ((ImmutableWorldUidReference) object).worldReferent);
    }

}