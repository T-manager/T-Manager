package cpt202.groupwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @className: ConflictException
 * @description: Handle conflict exception like user name exist
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This is already exists. `(*>﹏<*)′")
public class ConflictException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ConflictException() {
        super();
    }

    public ConflictException(String info) {
        super(info);
    }
}