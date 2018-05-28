package io.prodity.commons.effect;

import io.prodity.commons.repository.Repository;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface EffectRepository extends Repository<String, Effect> {

    /**
     * Plays the {@link Effect} associated with the specified key, if present.
     *
     * @param key the {@link Effect} key
     * @param options {@link EffectOption}s to supply to the played effect
     * @return true if the {@link Effect} existed and was played, false if not
     */
    boolean play(String key, EffectOption... options);

}