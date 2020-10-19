package io.prodity.commons.spigot.legacy.world.reference.factory;

import io.prodity.commons.spigot.legacy.world.reference.name.MutableWorldNameReference;
import io.prodity.commons.spigot.legacy.world.reference.uid.MutableWorldUidReference;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class MutableWorldReferenceFactory implements WorldReferenceFactory<MutableWorldNameReference, MutableWorldUidReference> {

    @Override
    public MutableWorldNameReference byName(String worldName) {
        return new MutableWorldNameReference(worldName);
    }

    @Override
    public MutableWorldUidReference byUid(UUID worldUid) {
        return new MutableWorldUidReference(worldUid);
    }

}
