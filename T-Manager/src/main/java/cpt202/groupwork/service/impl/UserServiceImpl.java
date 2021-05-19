package cpt202.groupwork.service.impl;


import cpt202.groupwork.Response;
import cpt202.groupwork.entity.User;
import cpt202.groupwork.entity.VerificationCode;
import cpt202.groupwork.repository.UserRepository;
import cpt202.groupwork.repository.VerifyRepository;
import cpt202.groupwork.security.TokenUtils;
import cpt202.groupwork.service.UserService;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @description: implement User service
 * @author: Zhonghao
 * @create: 2021-04-07 16:48
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;
  @Autowired
  VerifyRepository verifyRepository;

  @Resource
  TokenUtils tokenUtils;

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String emailUserName;

  //定义发送的标题
  public static String title="[T-Manager]获取验证码";

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
      user.setUserAvatar("default.jpg");
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
  public Response<?> verificationEmailSend(User user) {
    String emailCode = randomNumBuilder();
    Date current=new Date();
    long time = 30*60*1000;//30minutes
    if(user.getUserEmail()==null){
      return Response.fail("Need email");
    }
    Optional<VerificationCode> verification=verifyRepository.findByUserEmail(user.getUserEmail());
    verification.ifPresent(verificationCode -> verifyRepository.delete(verificationCode));

    Date afterDate = new Date(current.getTime() + 300000);
    VerificationCode verificationEmail=new VerificationCode();
    verificationEmail.setUserEmail(user.getUserEmail());
    verificationEmail.setVerifyPassword(emailCode);
    verificationEmail.setExpireTime(afterDate);
    verifyRepository.save(verificationEmail);

    if(generateCodeandSend(emailCode, user.getUserEmail())){
      return Response.ok(3000); // ok
    }
    else {
      return Response.fail(3001); // fail to send email
    }
  }
  public boolean generateCodeandSend(String emailCode, String emailAddr){
    try{
      String body = setEmailBody(emailCode);
      MimeMessage mimeMessage = this.mailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setFrom(emailUserName);//set Sender
      message.setTo(emailAddr);//Set receiver
      message.setSubject(title);  //Set title
      message.setText(body);
      this.mailSender.send(mimeMessage);
    } catch(Exception e){
      return false;
    }
    return true;
  }

  @Override
  public Response<?> verifyCode(VerificationCode verificationCode) {
    Optional<VerificationCode> verification;
    try{
      verification=verifyRepository.findByUserEmail(verificationCode.getUserEmail());
    }catch (Exception e) {
      return Response.fail("No verification code");
    }
    if(!verification.isPresent()){
      return Response.fail("No verification code");
    }
    Date current=new Date();
    if(current.after(verification.get().getExpireTime())){
      verifyRepository.delete(verification.get());
      return Response.fail("Verification code expire");
    }
    if(!verificationCode.getVerifyPassword().equals(verification.get().getVerifyPassword())){
      return Response.fail("Wrong Verification code");
    }
    return Response.ok("Verify successful");
  }

  private String setEmailBody(String emailCode){
    StringBuffer body = new StringBuffer();
    body.append("Dear user:\n\n").append("    Your verification code is:  ").append(emailCode+"\n\n");
    body.append("    Reminder: The verificationcode will be expired after 30 minutes\n\n");
    return body.toString();
  }
  public static String randomNumBuilder(){
    String result = "";
    for(int i=0;i<6;i++){
      result += Math.round(Math.random() * 9);
    }
    return result;
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