package cpt202.groupwork.service.impl;


import cpt202.groupwork.controller.UserController;
import cpt202.groupwork.Response;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.security.TokenUtils;
import cpt202.groupwork.service.UserService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import javax.swing.text.html.Option;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @description: implement User service
 * @author: Zhonghao
 * @create: 2021-04-07 16:48
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired UserRepository userRepository;

  @Resource TokenUtils tokenUtils;

  @Override
  public Response<?> userIdExists(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  @Override
  public Response<?> userGetInfoByName(String username) {
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      return Response.ok(user.get());
    } else {
      return Response.ok("username not found");
    }
  }

  /**
   * 普通的用户注册，自动给它role=USER,并将密码加密保存
   *
   * @param user
   * @return
   */
  @Override
  public Response<?> userCreate(User user) {
    // Springboot Security 提供的密码加密方法，使用SHA-256 + 随机盐 + 密钥对密码进行加密
    // 密文格式见 https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts

    try {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      user.setUserPassword(encoder.encode(user.getUserPassword()));
      user.setUserRole("USER");
      userRepository.save(user);
      return Response.ok(1000); // User created successfully
    } catch (Exception e) {
      return Response.fail(1001); // User already exists
    }
  }

  @Override
  public Response<?> userDelete(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      userRepository.deleteById(userId);
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  @Override
  public Response<?> userDelete(String username) {
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      userRepository.delete(user.get());
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  @Override
  public Response<?> userModify(Integer userId, User userMod) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      // 找出值为空的属性
      final BeanWrapper src = new BeanWrapperImpl(userMod);
      java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

      Set<String> emptyNames = new HashSet<String>();
      for (java.beans.PropertyDescriptor pd : pds) {
        Object srcValue = src.getPropertyValue(pd.getName());
        if (srcValue == null) {
          emptyNames.add(pd.getName());
        }
      }
      String[] result = new String[emptyNames.size()];
      // 找出为空的属性传入copyProperties
      BeanUtils.copyProperties(userMod, user.get(), emptyNames.toArray(result));
      userRepository.save(user.get());
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  @Override
  public Response<?> userModify(String username, User userMod) {
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      if (userMod.getUserPassword() != null) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userMod.setUserPassword(encoder.encode(userMod.getUserPassword()));
      }
      // 找出值为空的属性
      final BeanWrapper src = new BeanWrapperImpl(userMod);
      java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

      Set<String> emptyNames = new HashSet<String>();
      for (java.beans.PropertyDescriptor pd : pds) {
        Object srcValue = src.getPropertyValue(pd.getName());
        if (srcValue == null) {
          emptyNames.add(pd.getName());
        }
      }
      String[] result = new String[emptyNames.size()];
      // 找出为空的属性传入copyProperties
      BeanUtils.copyProperties(userMod, user.get(), emptyNames.toArray(result));
      userRepository.save(user.get());
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  @Override
  public Response<?> userLogin(User postUser) {
    Optional<User> user = userRepository.findByUserName(postUser.getUserName());
    if (user.isPresent()) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      // matches参数: 第一个参数为未加密密码，第二个为数据库中存储的加密后的密码，返回值为其是否匹配
      if (encoder.matches(postUser.getUserPassword(), user.get().getUserPassword())) {
        System.out.println(tokenUtils.createToken(user.get()));
        return Response.ok(tokenUtils.createToken(user.get()));
      } else {
        return Response.fail(2001); // Wrong password
      }
    } else {
      return Response.fail(2002); // User not found
    }
  }

  @Override
  public Response<?> userInfoCheck(User postUser) {
    Optional<User> user = userRepository.findByUserName(postUser.getUserName());
    if (user.isPresent()) {
      if (user.get().getUserEmail().equals(postUser.getUserEmail()))
        return Response.ok(2000); // Match
      else return Response.fail(2001); // Not match
    } else {
      return Response.fail(2002); // Not found
    }
  }

  @Override
  public Response<?> userNameExists(String username){
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      return Response.ok(2000); // Found
    } else {
      return Response.fail(2002); // Not found
    }
  }
}