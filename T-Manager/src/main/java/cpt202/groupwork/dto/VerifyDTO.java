package cpt202.groupwork.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @className: VerifyDTO
 * @description: DTO about verification code and user information
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Data
public class VerifyDTO {
  //user email
  @NotNull(message = "userEmail cannot be null")
  private String userEmail;

  //verification Code
  @NotNull(message = "verificationCode cannot be null")
  private String verificationCode;
}