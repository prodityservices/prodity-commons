package io.prodity.commons.terminable.registry;

import com.google.common.collect.Lists;
import io.prodity.commons.except.CompositeException;
import io.prodity.commons.terminable.Terminable;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import javax.annotation.Nonnull;
import org.jvnet.hk2.annotations.Service;

@Service
public class SimpleTerminableRegistry implements TerminableRegistry {

    private final Deque<AutoCloseable> closeables;

    protected SimpleTerminableRegistry() {
        this.closeables = new ConcurrentLinkedDeque<>();
    }

    @Override
    public void close() throws CompositeException {
        final List<Exception> exceptions = Lists.newArrayList();

        for (AutoCloseable closeable; (closeable = this.closeables.poll()) != null; ) {
            try {
                closeable.close();
            } catch (Exception exception) {
                exceptions.add(exception);
            }
        }

        if (!exceptions.isEmpty()) {
            throw new CompositeException(exceptions);
        }
    }

    @Override
    public void cleanup() {
        this.closeables.removeIf((closeable) -> closeable instanceof Terminable && ((Terminable) closeable).isClosed());
    }

    @Override
    public TerminableRegistry bind(@Nonnull AutoCloseable autoCloseable) {
        this.closeables.push(autoCloseable);
        return this;
    }

}