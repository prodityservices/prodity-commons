package io.prodity.commons.except;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import javax.annotation.Nonnull;

public class CompositeException extends Exception {

    private static String formatMessage(int amount) {
        return "Exception(s) occurred, total exceptions: " + amount;
    }

    private final ImmutableList<? extends Throwable> causes;

    public CompositeException(@Nonnull Throwable... causes) {
        super(CompositeException.formatMessage(causes.length));
        Preconditions.checkState(causes.length > 0, "causes length must be >0 but =%s", causes.length);
        this.causes = ImmutableList.copyOf(causes);
    }

    public CompositeException(@Nonnull Collection<? extends Throwable> causes) {
        super(CompositeException.formatMessage(causes.size()));
        Preconditions.checkState(causes.size() > 0, "causes size must be >0 but =%s", causes.size());
        this.causes = ImmutableList.copyOf(causes);
    }

    @Nonnull
    public ImmutableList<? extends Throwable> getCauses() {
        return this.causes;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        this.causes.forEach(Throwable::printStackTrace);
    }

    @Override
    public void printStackTrace(PrintStream stream) {
        super.printStackTrace();
        this.causes.forEach((throwable) -> throwable.printStackTrace(stream));
    }

    @Override
    public void printStackTrace(PrintWriter writer) {
        super.printStackTrace();
        this.causes.forEach((throwable) -> throwable.printStackTrace(writer));
    }

}