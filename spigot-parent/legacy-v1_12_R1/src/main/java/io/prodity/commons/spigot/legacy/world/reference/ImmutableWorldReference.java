package io.prodity.commons.spigot.legacy.world.reference;

public interface ImmutableWorldReference<T> extends WorldReference<T> {

    interface Delegate<T> extends ImmutableWorldReference<T>, DelegateWorldReference<ImmutableWorldReference<T>, T> {

    }

}