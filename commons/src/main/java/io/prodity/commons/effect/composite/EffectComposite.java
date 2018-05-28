package io.prodity.commons.effect.composite;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.prodity.commons.effect.Effect;
import io.prodity.commons.effect.EffectBase;
import io.prodity.commons.effect.EffectOption;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * A composition of multiple {@link Effect}s all to be played together.
 */
public class EffectComposite extends EffectBase {

    private final List<Effect> effects = Lists.newCopyOnWriteArrayList();

    public EffectComposite(String id) {
        super(id);
    }

    public EffectComposite(String id, Collection<Effect> effects) {
        super(id);
        Preconditions.checkNotNull(effects, "effects");
        this.effects.addAll(effects);
    }

    public EffectComposite(String id, Effect... effects) {
        super(id);
        Preconditions.checkNotNull(effects, "effects");
        this.effects.addAll(Arrays.asList(effects));
    }

    public EffectComposite addEffect(Effect effect) {
        Preconditions.checkNotNull(effect, "effect");
        this.effects.add(effect);
        return this;
    }

    public List<Effect> getEffects() {
        return this.effects;
    }

    @Override
    public void play(EffectOption... options) {
        this.effects.forEach((effect) -> effect.play(options));
    }

}