package cpt202.groupwork.controller;

import cpt202.groupwork.service.UserService;
import java.util.Optional;

import cpt202.groupwork.entity.User;
import cpt202.groupwork.repository.UserRepository;
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

    @GetMapping("/{username}")
    public Response<?> getUserInfo(@PathVariable String username) {
        return userService.userNameExists(username);
    }

    @PutMapping("/{username}")
    public Response<?> putUser(@PathVariable String username, @RequestBody User user) {
        return userService.userModify(username,user);
    }

    /**
     *
     * @param username
     * @return
     */
    @DeleteMapping("/{username}")
//    @PreAuthorize("hasRole('ADMIN')")
    public Response<?> deleteUser(@PathVariable String username) {

        return userService.userDelete(username);
    }
}
