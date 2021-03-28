package cpt202.groupwork.service;


import cpt202.groupwork.controller.UserController;
import cpt202.groupwork.Response;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.security.TokenUtils;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
  @Autowired
  UserRepository userRepository;

  @Resource
  TokenUtils tokenUtils;
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

  /**
   * 普通的用户注册，自动给它role=USER,并将密码加密保存
   * @param user
   * @return
   */
  public Response<?> userCreate(final User user){
    // Springboot Security 提供的密码加密方法，使用SHA-256 + 随机盐 + 密钥对密码进行加密
    // 密文格式见 https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts

    try {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      user.setPassword(encoder.encode(user.getPassword()));
      user.setRole("USER");
      userRepository.save(user);
      return Response.ok(1000); //User created successfully
    } catch (Exception e){
      return Response.fail(1001); // User already exists
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
      //找出值为空的属性
      final BeanWrapper src = new BeanWrapperImpl(userMod);
      java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

      Set<String> emptyNames = new HashSet<String>();
      for (java.beans.PropertyDescriptor pd : pds) {
        Object srcValue = src.getPropertyValue(pd.getName());
        if (srcValue == null)
          emptyNames.add(pd.getName());
      }
      String[] result = new String[emptyNames.size()];
//      emptyNames.toArray(result);
      //找出为空的属性传入copyProperties
      BeanUtils.copyProperties(userMod,user.get(),emptyNames.toArray(result));
//      user.get().setId(userId);
      userRepository.save(user.get());
      return Response.ok(user.get());
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
        System.out.println(tokenUtils.createToken(user.get()));
        return Response.ok(tokenUtils.createToken(user.get()));
      }
      else{
        return Response.fail("Wrong password"); // not match
      }
    }
    else
      return Response.fail("User not exist"); // not found
  }
}
