package cpt202.groupwork;

import cpt202.groupwork.exception.ConflictException;
import cpt202.groupwork.exception.NotFoundException;
import cpt202.groupwork.exception.PermissionDeniedException;
import cpt202.groupwork.exception.UnAuthException;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.server.ResponseStatusException;

/**
 * @className: Response
 * @description: pack message entity in the controller layer
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
@Builder
public class Response<T> {

    // http status
    private Integer status;

    // message
    private String message;

    // data
    private T data;

    // success case
    public static Response<?> ok() {
        return Response.builder().status(HttpStatus.OK.value()).message("Success")
            .data(new ArrayList<>())
            .build();
    }

    // success + relating data
    public static Response<?> ok(Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message("Success").data(data).build();
    }

    // success + message + relating data
    public static Response<?> ok(String message, Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message(message).data(data).build();
    }

    // unauthorized
    public static Response<?> unAuth() {
        throw new UnAuthException();
    }

    // resource not found
    public static Response<?> notFound() {
        throw new NotFoundException();
    }

    // no permission to do the action
    public static Response<?> permissionDenied(String message) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }
    // fail
    public static Response<?> fail() {
        return Response.builder().status(HttpStatus.OK.value()).message("Fail").data(new ArrayList<>()).build();
    }

    // fail + relating data
    public static Response<?> fail(Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message("Fail").data(data).build();
    }

    // fail +  message
    public static Response<?> fail(String message) {
        return Response.builder().status(HttpStatus.BAD_REQUEST.value()).message(message).build();
    }

    // fail + message + data
    public static Response<?> fail(String message, Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message(message).data(data).build();
    }

    // data already exist, do not add again to make conflict
    public static Response<?> conflict(String message) {
        throw new ConflictException(message);
    }

}