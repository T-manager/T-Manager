package cpt202.groupwork.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @description: DTO about verification code and user information
 * @author: Zhonghao Wang
 * @create:
 **/
@Data
public class VerifyDTO {
  //user email
  @NotNull(message = "userEmail cannot be null")
  private String userEmail;

  //verification Code
  @NotNull(message = "verificationCode cannot be null")
  private String verificationCode;
}