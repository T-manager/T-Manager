package cpt202.groupwork.controller;

import cpt202.groupwork.entity.VerificationCode;
import cpt202.groupwork.service.UserService;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.Response;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: AuthController
 * @description: Controller layer for the auth module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Resource
  private UserService userService;

  /**
   * register a user
   * @param user
   * @return response
   */
  @PostMapping("/register")
  public Response<?> createUser(@RequestBody User user) {
    return userService.userCreate(user);
  }

  /**
   * User login
   * @param user
   * @return response
   */
  @PostMapping("/login")
  public Response<?> postUserLogin(@RequestBody User user) {
    return userService.userLogin(user);
  }

  /**
   * check whether the user name exists
   * @param username
   * @return response
   */
  @GetMapping("/check")
  public Response<?> checkUserExists(@RequestParam("username") String username) {
    return userService.userNameExists(username);
  }

  /**
   * check whether the user infomation is matched
   * @param user
   * @return response
   */
  @PostMapping("/check")
  public Response<?> checkUserInfo(@RequestBody User user) {
    return userService.userInfoCheck(user);
  }

  /**
   * allow modify password when no token temporarily, will change in the following sprint
   * @param username
   * @param user
   * @return response
   */
  @PutMapping("/edit/{username}")
  public Response<?> modifyUser(@PathVariable String username, @RequestBody User user) {
    return userService.userModify(username, user);
  }

  /**
   * send verification code to specific e-mail address
   * @param user(必须有email)
   * @return response
   */
  @PostMapping("/codesending")
  public Response<?> getEmail(@RequestBody User user) {
    return userService.verificationEmailSend(user);
  }

  /**
   * verify email code when reset password or registration
    * @param verificationCode
   * @return response
   */
  @PostMapping("/codeVerification")
  public Response<?> verifyCode(@RequestBody VerificationCode verificationCode) {
    return userService.verifyCode(verificationCode);
  }


}