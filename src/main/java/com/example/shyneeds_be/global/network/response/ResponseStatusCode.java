package com.example.shyneeds_be.global.network.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseStatusCode {
    SUCCESS(200),
    CREATED(201),
    FAIL(202),
    NO_CONTENT(204),
    EXTERNAL_MEMBER(205), //sns 멤버
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503),
    DB_ERROR(600);

    private final int value;
}
