package io.prodity.commons.inject;

import org.jvnet.hk2.annotations.Contract;

/**
 * Marker interface for services that should be eagerly instantiated
 * once the plugin is enabled.  HK2 is lazy, so the only way for services
 * to be created is for them to be requested from a ServiceLocator.
 */
@Contract
public interface Eager {

}
