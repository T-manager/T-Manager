package cpt202.groupwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "你还没有登录呢!")
public class UnAuthException extends RuntimeException  {
  private static final long serialVersionUID = 1L;
}
