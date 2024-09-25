package com.muud.global.exception.response;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.util.List;

@Builder
public record ApiSimpleError(@Nonnull String field, @Nonnull String message) {

    public static List<ApiSimpleError> listOfCauseSimpleError(Throwable cause) {
        return List.of(arrayOfCauseSimpleError(cause));
    }

    public static ApiSimpleError[] arrayOfCauseSimpleError(Throwable cause) {
        int depth = 0;
        Throwable currentCause = cause;

        while (currentCause != null) {
            currentCause = currentCause.getCause();
            depth++;
        }

        ApiSimpleError[] subErrors = new ApiSimpleError[depth];
        currentCause = cause;
        for (int i = 0; i < depth; i++) {
            String errorFullName = currentCause.getClass().getSimpleName();
            String field = errorFullName.substring(errorFullName.lastIndexOf('.') + 1);
            subErrors[i] = ApiSimpleError.builder()
                    .field(field)
                    .message(currentCause.getLocalizedMessage())
                    .build();

            currentCause = currentCause.getCause();
        }

        return subErrors;
    }
}
