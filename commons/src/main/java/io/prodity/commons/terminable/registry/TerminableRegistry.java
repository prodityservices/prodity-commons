package io.prodity.commons.terminable.registry;

import io.prodity.commons.except.CompositeException;
import io.prodity.commons.terminable.Terminable;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface TerminableRegistry extends Terminable {

    static TerminableRegistry create() {
        return new SimpleTerminableRegistry();
    }

    @Override
    void close() throws CompositeException;

    void cleanup();

    TerminableRegistry bind(AutoCloseable autoCloseable);

    default TerminableRegistry bind(AutoCloseable... autoCloseables) {
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (autoCloseable != null) {
                this.bind(autoCloseable);
            }
        }
        return this;
    }

    default TerminableRegistry bind(Iterable<AutoCloseable> autoCloseables) {
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (autoCloseable != null) {
                this.bind(autoCloseable);
            }
        }
        return this;
    }

}