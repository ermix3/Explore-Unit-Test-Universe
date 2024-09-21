package io.ermix.demo.exception.handler;

import lombok.Builder;


@Builder
public record ErrorResponse(
        String message,
        String details,
        int status
) {
}
