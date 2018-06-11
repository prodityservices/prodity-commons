package io.prodity.commons.spigot.legacy.sound.playable.collect.list;

import io.prodity.commons.spigot.legacy.sound.playable.PlayableSound;
import io.prodity.commons.spigot.legacy.sound.playable.collect.PlayableSoundCollection;
import java.util.List;

public interface PlayableSoundList<T extends PlayableSound> extends PlayableSoundCollection<T>, List<T> {

}
