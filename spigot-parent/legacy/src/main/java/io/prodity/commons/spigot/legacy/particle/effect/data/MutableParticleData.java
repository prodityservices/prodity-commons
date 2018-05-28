package io.prodity.commons.spigot.legacy.particle.effect.data;

import io.prodity.commons.spigot.legacy.lazy.LazyValue;
import io.prodity.commons.spigot.legacy.lazy.SimpleLazyValue;
import io.prodity.commons.spigot.legacy.particle.effect.data.block.MutableBlockParticleData;
import io.prodity.commons.spigot.legacy.particle.effect.data.item.MutableItemParticleData;
import com.google.common.base.Objects;
import lombok.Getter;
import org.bukkit.Material;

public abstract class MutableParticleData<T> implements ParticleData<T> {

    @Getter
    private Material material;

    @Getter
    private byte data;

    private final LazyValue<T> packetData = new SimpleLazyValue<>(this::createSpawnData);

    public MutableParticleData(Material material, byte data) throws IllegalArgumentException {
        this.verify(material, data);
        this.material = material;
        this.data = data;
        this.packetData.update();
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.packetData.update();
    }

    public void setData(byte data) {
        this.data = data;
        this.packetData.update();
    }

    public void setMaterialData(Material material, byte data) {
        this.material = material;
        this.data = data;
        this.packetData.update();
    }

    @Override
    public T getSpawnData() {
        return this.packetData.get();
    }

    @Override
    public MutableBlockParticleData toBlockData() {
        return new MutableBlockParticleData(this.material, this.data);
    }

    @Override
    public MutableItemParticleData toItemData() {
        return new MutableItemParticleData(this.material, this.data);
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
        if (!(obj instanceof MutableParticleData)) {
            return false;
        }
        final MutableParticleData that = (MutableParticleData) obj;
        return Objects.equal(this.material, that.material) && Objects.equal(this.data, that.data);
    }

}