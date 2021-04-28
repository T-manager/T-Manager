package cpt202.groupwork.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @className: UnAuthException
 * @description: Handle exception that user is unauthorized
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "you are not login yet!")
public class UnAuthException extends RuntimeException  {
  private static final long serialVersionUID = 1L;
}
