package io.prodity.commons.spigot.legacy.particle.effect.data;

import io.prodity.commons.spigot.legacy.particle.effect.data.block.ImmutableBlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.ImmutableItemParticleData;
import com.google.common.base.Objects;
import lombok.Getter;
import org.bukkit.Material;

public abstract class ImmutableParticleData<T> implements ParticleData<T> {

    @Getter
    private final Material material;

    @Getter
    private final byte data;

    @Getter
    private final T spawnData;

    public ImmutableParticleData(Material material, byte data) throws IllegalArgumentException {
        this.verify(material, data);
        this.material = material;
        this.data = data;
        this.spawnData = this.createSpawnData();
    }

    @Override
    public ImmutableBlockParticleData toBlockData() {
        return new ImmutableBlockParticleData(this.material, this.data);
    }

    @Override
    public ImmutableItemParticleData toItemData() {
        return new ImmutableItemParticleData(this.material, this.data);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.material, this.data);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImmutableParticleData)) {
            return false;
        }
        final ImmutableParticleData that = (ImmutableParticleData) obj;
        return Objects.equal(this.material, that.material) && Objects.equal(this.data, that.data);
    }

}