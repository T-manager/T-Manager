package cpt202.groupwork.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Do not have permission. `(*>﹏<*)′")
public class PermissionDeniedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PermissionDeniedException() {
    super();
  }

  public PermissionDeniedException(String info) {
    super(info);
  }
}