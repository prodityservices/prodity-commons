package io.prodity.commons.spigot.legacy.world.reference.factory;

import io.prodity.commons.spigot.legacy.world.reference.name.ImmutableWorldNameReference;
import io.prodity.commons.spigot.legacy.world.reference.uid.ImmutableWorldUidReference;
import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ImmutableWorldReferenceFactory implements WorldReferenceFactory<ImmutableWorldNameReference, ImmutableWorldUidReference> {

    @Override
    public ImmutableWorldNameReference byName(String worldName) {
        return new ImmutableWorldNameReference(worldName);
    }

    @Override
    public ImmutableWorldUidReference byUid(UUID worldUid) {
        return new ImmutableWorldUidReference(worldUid);
    }

}
