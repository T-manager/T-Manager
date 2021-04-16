package cpt202.groupwork.controller;

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
 * @description: User Register and Login
 * @author: nxh
 * @create: 2021-04-07 16:54
 **/
@CrossOrigin(origins = "*", maxAge = 3600)
// @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Resource
  private UserService userService;

  @PostMapping("/register")
  public Response<?> postUser(@RequestBody User user) {
    // @RequestBody注解用来绑定通过http请求中application/json类型上传的数据
    return userService.userCreate(user);
  }

  @PostMapping("/login")
  public Response<?> postUserLogin(@RequestBody User user){
    return userService.userLogin(user);
  }

  @GetMapping("/check")
  public Response<?> checkUserExists (@RequestParam("username") String username){
    return userService.userNameExists(username);
  }

  @PostMapping("/check")
  public Response<?> checkUserInfo (@RequestBody User user){
    return userService.userInfoCheck(user);
  }

  // 临时允许没有token就可以改密码
  @PutMapping("/edit/{username}")
  public Response<?> putUser(@PathVariable String username, @RequestBody User user) {
    return userService.userModify(username, user);
  }
}