package com.grimels.lazurcity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundStatusException extends ResponseStatusException {
    public static final HttpStatus ERROR_RESPONSE_CODE = HttpStatus.NOT_FOUND;
    public static final String MSG_ENTITY_NOT_FOUND_BY_ID = "Could not find the %s with ID '%s'";

    public NotFoundStatusException(String reason) {
        super(ERROR_RESPONSE_CODE, reason);
    }

    public static NotFoundStatusException createEntityNotFoundByIdError(String entityName, Object id) {
        return new NotFoundStatusException(String.format(MSG_ENTITY_NOT_FOUND_BY_ID, entityName, id));
    }

}
