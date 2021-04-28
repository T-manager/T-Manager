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
 * @className: UserServiceImpl
 * @description: implement User service
 * @Author: CPT202 Group 2
 * @version 1.0
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

  //set title to be sent
  public static String title="[T-Manager]get verification code";

  /**
   * check whether the user exists in user database
   * @param userId
   * @return response
   */
  @Override
  public Response<?> userIdExists(Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  /**
   * get user info by username in user database
   * @param username
   * @return response
   */
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
   * normal registration, set role = USER, encrypt the password and store it
   * @param user
   * @return response
   */
  @Override
  public Response<?> userCreate(User user) {
    // Springboot Security provide encrypt method，using SHA-256 + salt + key top encrypt password
    // format: https://stackoverflow.com/questions/6832445/how-can-bcrypt-have-built-in-salts

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

  /**
   * delete user by user id
   * @param userId
   * @return response
   */
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

  /**
   * delete user by user name
   * @param username
   * @return response
   */
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

  /**
   * modify user information using userid
   * @param userId
   * @param userMod
   * @return response
   */
  @Override
  public Response<?> userModify(Integer userId, User userMod) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      // find attribute that have no value
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
      // pass copyProperties
      BeanUtils.copyProperties(userMod, user.get(), emptyNames.toArray(result));
      userRepository.save(user.get());
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  /**
   * modify user information using username
   * @param username
   * @param userMod
   * @return response
   */
  @Override
  public Response<?> userModify(String username, User userMod) {
    Optional<User> user = userRepository.findByUserName(username);
    if (user.isPresent()) {
      if (userMod.getUserPassword() != null) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userMod.setUserPassword(encoder.encode(userMod.getUserPassword()));
      }
      // find attribute that have no value
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
      // pass copyProperties
      BeanUtils.copyProperties(userMod, user.get(), emptyNames.toArray(result));
      userRepository.save(user.get());
      return Response.ok(user.get());
    } else {
      return Response.ok("user not found");
    }
  }

  /**
   * login service of user
   * @param postUser
   * @return response
   */
  @Override
  public Response<?> userLogin(User postUser) {
    Optional<User> user = userRepository.findByUserName(postUser.getUserName());
    if (user.isPresent()) {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      // matches parameters: first parameter is unencrypted password, second is encrypted password, check whether they are matched
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

  /**
   * check user info
   * @param postUser
   * @return response
   */
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

  /**
   * send verification email
   * @param user
   * @return response
   */
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

  /**
   * generate code and send to user email
   * @param emailCode
   * @param emailAddr
   * @return boolean
   */
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

  /**
   * verify the verification code
   * @param verificationCode
   * @return response
   */
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

  /**
   * set the body of the e-mail
   * @param emailCode
   * @return response
   */
  private String setEmailBody(String emailCode){
    StringBuffer body = new StringBuffer();
    body.append("Dear user:\n\n").append("    Your verification code is:  ").append(emailCode+"\n\n");
    body.append("    Reminder: The verificationcode will be expired after 30 minutes\n\n");
    return body.toString();
  }

  /**
   * generate 6 digit for verification coder
   * @return string
   */
  public static String randomNumBuilder(){
    String result = "";
    for(int i=0;i<6;i++){
      result += Math.round(Math.random() * 9);
    }
    return result;
  }

  /**
   * check whether the username exist in user database using username
   * @param username
   * @return response
   */
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