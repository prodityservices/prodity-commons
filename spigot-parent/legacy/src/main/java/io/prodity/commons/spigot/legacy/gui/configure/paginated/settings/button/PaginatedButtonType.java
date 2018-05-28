package io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button;

import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.PaginatedInfo;
import java.util.function.Predicate;

public enum PaginatedButtonType {

    NEXT_PAGE(PaginatedInfo::hasNextPage),
    PREVIOUS_PAGE(PaginatedInfo::hasPreviousPage);

    private final Predicate<PaginatedInfo> verifier;

    PaginatedButtonType(Predicate<PaginatedInfo> verifier) {
        this.verifier = verifier;
    }

    public boolean isValid(PaginatedInfo info) {
        return this.verifier.test(info);
    }

}