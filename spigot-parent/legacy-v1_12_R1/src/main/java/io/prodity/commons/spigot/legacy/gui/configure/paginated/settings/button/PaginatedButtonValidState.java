package io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.button;

import io.prodity.commons.spigot.legacy.gui.configure.paginated.settings.PaginatedInfo;
import java.util.function.BiPredicate;

public enum PaginatedButtonValidState {

    VALID((type, info) -> type.isValid(info) && info.isDrawElements()),
    INVALID((type, info) -> !type.isValid(info));

    public static PaginatedButtonValidState from(PaginatedButtonType buttonType, PaginatedInfo info) {
        for (PaginatedButtonValidState state : PaginatedButtonValidState.values()) {
            if (state.isCorrectState(buttonType, info)) {
                return state;
            }
        }
        throw new IllegalStateException("could not parse PaginatedButtonValidState");
    }

    private final BiPredicate<PaginatedButtonType, PaginatedInfo> verifier;

    PaginatedButtonValidState(BiPredicate<PaginatedButtonType, PaginatedInfo> verifier) {
        this.verifier = verifier;
    }

    public boolean isCorrectState(PaginatedButtonType type, PaginatedInfo info) {
        return this.verifier.test(type, info);
    }

}