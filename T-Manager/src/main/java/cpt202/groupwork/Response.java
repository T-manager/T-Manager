package cpt202.groupwork;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;

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
        return Response.builder().status(HttpStatus.OK.value()).message("Success").data(new ArrayList<>())
                .build();
    }

    // 正常通过 + 携带相关 data 数据
    public static Response<?> ok(Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message("Success").data(data).build();
    }

    // 正常通过 + 携带相关 data 数据
    public static Response<?> ok(String message, Object data) {
        return Response.builder().status(HttpStatus.OK.value()).message(message).data(data).build();
    }


    // 未通过
    public static Response<?> fail() {
        return Response.builder().status(HttpStatus.OK.value()).message("Fail").data(new ArrayList<>()).build();
    }
}