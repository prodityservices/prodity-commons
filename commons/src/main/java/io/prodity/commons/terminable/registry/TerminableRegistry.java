package io.prodity.commons.terminable.registry;

import io.prodity.commons.except.CompositeException;
import io.prodity.commons.terminable.Terminable;
import javax.annotation.Nonnull;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface TerminableRegistry extends Terminable {

    @Nonnull
    static TerminableRegistry create() {
        return new SimpleTerminableRegistry();
    }

    @Override
    void close() throws CompositeException;

    void cleanup();

    TerminableRegistry bind(@Nonnull AutoCloseable autoCloseable);

    default TerminableRegistry bind(@Nonnull AutoCloseable... autoCloseables) {
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (autoCloseable != null) {
                this.bind(autoCloseable);
            }
        }
        return this;
    }

    default TerminableRegistry bind(@Nonnull Iterable<AutoCloseable> autoCloseables) {
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (autoCloseable != null) {
                this.bind(autoCloseable);
            }
        }
        return this;
    }

}