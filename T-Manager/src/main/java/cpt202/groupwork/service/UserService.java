package cpt202.groupwork.service;

import cpt202.groupwork.controller.UserController;
import cpt202.groupwork.Response;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.entity.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  /**
   *
   * @param userId
   * @return
   */
  public Response<?> userIdExists(final Integer userId){
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return Response.ok(user.get());
    }
    else {
      return Response.ok("user not found");
    }
  }

  public Response<?> userCreate(final User user){
    userRepository.save(user);
    return Response.ok();
  }
}
