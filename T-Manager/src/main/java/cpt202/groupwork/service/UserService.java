package cpt202.groupwork.service;

import cpt202.groupwork.controller.UserController;
import cpt202.groupwork.Response;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.entity.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    // Springboot Security 提供的密码加密方法，使用SHA-256 + 随机盐 + 密钥对密码进行加密
    // 密文格式见 https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));
    try {
      userRepository.save(user);
      return Response.ok();
    } catch (Exception e){
      return Response.fail();
    }
  }

  public Response<?> userDelete(final Integer userId){
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      userRepository.deleteById(userId);
      return Response.ok(user.get());
    }
    else {
      return Response.ok("user not found");
    }
  }

  public Response<?> userModify(final Integer userId,  final User userMod){
    Optional<User> user = userRepository.findById(userId);
//    if(userMod.getId()!=userId){
//      return Response.ok("userID not found");
//    }
    if (user.isPresent()) {
      userRepository.saveAndFlush(userMod);
      return Response.ok(userMod);
    }
    else {
      return Response.ok("user not found");
    }
  }

  public Response<?> userLogin(final User postUser){
    Optional<User> user = userRepository.findByUsername(postUser.getUsername());
    if(user.isPresent()) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      // matches参数: 第一个参数为未加密密码，第二个为数据库中存储的加密后的密码，返回值为其是否匹配
      if(encoder.matches(postUser.getPassword(), user.get().getPassword())){
        return Response.ok();
      }
      else{
        return Response.fail(); // not match
      }
    }
    else
      return Response.fail(); // not found
  }
}
