package io.prodity.commons.spigot.legacy.world.reference.uid;

import com.google.common.base.Objects;
import io.prodity.commons.spigot.legacy.world.reference.abstrakt.AbstractMutableWorldReference;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.UUID;

public class MutableWorldUidReference extends AbstractMutableWorldReference<UUID> implements WorldUidReference {

    public MutableWorldUidReference(@NonNull UUID uuid) {
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

        if (!(object instanceof MutableWorldUidReference)) {
            return false;
        }

        return Objects.equal(this.worldReferent, ((MutableWorldUidReference) object).worldReferent);
    }

}