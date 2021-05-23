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
 * 在 controller 层的消息包装实体, 别的层就不要一层一层的把错误信息传下来了.
 */
@Data
@Builder
public class Response<T> {

    // http 状态
    private Integer status;

    // 消息
    private String message;

    // 数据
    private T data;

    // 正常通过
    public static Response<?> ok() {
        return Response.builder().status(HttpStatus.OK.value()).message("Success").data(new ArrayList<>()).build();
    }

    // 正常通过 + 携带相关 data 数据
    public static Response<?> ok(Object data) {
        return Response.builder().status(0).message("Success").data(data).build();
    }

    // 正常通过 + 携带相关 data 数据
    public static Response<?> ok(String message, Object data) {
        return Response.builder().status(0).message(message).data(data).build();
    }

    // 没有授权，请先登录
    public static Response<?> unAuth() {
        throw new UnAuthException();
    }

    // 没有找到该资源
    public static Response<?> notFound() {
        throw new NotFoundException();
    }

    // 没有进行这个操作的权限
    public static Response<?> permissionDenied(String message) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, message);
    }

    // 未通过
    public static Response<?> fail() {
        return Response.builder().status(HttpStatus.OK.value()).message("Fail").data(new ArrayList<>()).build();
    }

    // 未通过 + 携带相关 data 数据
    public static Response<?> fail(Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message("Fail").data(data).build();
    }

    // 未通过 + 携带相关 message
    public static Response<?> fail(String message) {
        return Response.builder().status(HttpStatus.BAD_REQUEST.value()).message(message).build();
    }

    // 未通过 + 携带相关 data 数据
    public static Response<?> fail(String message, Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message(message).data(data).build();
    }

    // 已经存在了, 不能再添加此类信息, 信息冲突
    public static Response<?> conflict(String message) {
        throw new ConflictException(message);
    }



     // 异常处理，需要传入错误状态码和想要抛给前端的message
     public static Response<?> exceptionHandling(int statusCode, String message,Object data) {
        return Response.builder().status(statusCode).message(message).data(data).build();
    }
    // 异常处理，需要传入错误状态码和想要抛给前端的message
    public static Response<?> exceptionHandling(int statusCode, String message) {
        return Response.builder().status(statusCode).message(message).build();
    }

}