package cpt202.groupwork.controller;

import cpt202.groupwork.service.UserService;

import cpt202.groupwork.entity.User;
import cpt202.groupwork.Response;
import javax.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
// @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("/user")
public class UserController {
//    @Autowired
//    UserRepository userRepository;
    @Resource
    private UserService userService;

    @GetMapping("/{userId}")
    public Response<?> getUserInfo(@PathVariable Integer userId) {
//        Optional<User> user = userRepository.findById(userId);
//        if (user.isPresent()) {
//            return Response.ok(user.get());
//        }
//        else {
//            return Response.ok("user not found");
//        }
        return userService.userIdExists(userId);
        // 没登录的人, 去访问别人的用户主页
    }

    @PutMapping("/{id}")
    public Response<?> putUser(@PathVariable Integer id, @RequestBody User user) {

       return userService.userModify(id,user);
    }

    /**
     *
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteUser(@PathVariable Integer id) {

        return userService.userDelete(id);
    }
}
