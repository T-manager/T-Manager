package cpt202.groupwork.controller;

import cpt202.groupwork.service.UserService;

import cpt202.groupwork.entity.User;
import cpt202.groupwork.Response;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @className: UserController
 * @description: Controller layer for the todolist module.
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserController {

  @Resource
  private UserService userService;

  /**
   * get the user information by username
   * @param username
   * @return response
   */
  @GetMapping("/get/{username}")
  public Response<?> getUserInfo(@PathVariable String username) {
    return userService.userGetInfoByName(username);
  }

  /**
   * edit user information
   * @param username
   * @param user
   * @return
   */
  @PutMapping("/edit/{username}")
  public Response<?> putUser(@PathVariable String username, @RequestBody User user) {
    return userService.userModify(username, user);
  }

  /**
   * delete user
   * @param username
   * @return response
   */
  @DeleteMapping("/delete/{username}")
  public Response<?> deleteUser(@PathVariable String username) {
    return userService.userDelete(username);
  }

}
