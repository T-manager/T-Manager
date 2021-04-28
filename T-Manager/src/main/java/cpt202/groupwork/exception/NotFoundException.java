package cpt202.groupwork.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @className: NotFoundException
 * @description: handle exception that data not found
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not Found. `(*>﹏<*)′")
public class NotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotFoundException() {
    super();
  }

  public NotFoundException(String info) {
    super(info);
  }
}
