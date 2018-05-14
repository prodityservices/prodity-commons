package io.prodity.commons.config.inject.member;

import io.prodity.commons.config.inject.ConfigInjectable;
import io.prodity.commons.config.inject.object.ConfigObject;

public interface ConfigMember extends ConfigInjectable {

    ConfigObject<?> getPossessor();

}