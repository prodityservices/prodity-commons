package io.prodity.commons.spigot.legacy.delegate;

import io.prodity.commons.spigot.legacy.builder.util.BuilderList;

public interface DelegateBuilderList<T, SELF extends DelegateBuilderList<T, SELF>> extends DelegateList<T>, BuilderList<T, SELF> {

}
