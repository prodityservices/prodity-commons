package io.prodity.commons.identity;

@FunctionalInterface
public interface Identifiable<T> {

    T getId();

}