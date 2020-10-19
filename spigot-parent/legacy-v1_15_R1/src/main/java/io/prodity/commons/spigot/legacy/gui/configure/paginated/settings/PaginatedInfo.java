package io.prodity.commons.spigot.legacy.gui.configure.paginated.settings;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder(builderClassName = "Builder")
public class PaginatedInfo {

    @Getter
    private final int totalPages;

    @Getter
    private final int currentPage;

    @Getter
    private final Optional<Integer> nextPage;

    @Getter
    private final Optional<Integer> previousPage;

    @Getter
    private final boolean drawElements;

    public boolean hasNextPage() {
        return this.nextPage.isPresent();
    }

    public boolean hasPreviousPage() {
        return this.previousPage.isPresent();
    }

}