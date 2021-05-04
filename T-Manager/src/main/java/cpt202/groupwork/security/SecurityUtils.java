package cpt202.groupwork.security;



import cpt202.groupwork.exception.UnAuthException;
import cpt202.groupwork.repository.UserRepository;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;



public class SecurityUtils {
 @Autowired
 UserRepository userRepository;

 private SecurityUtils() {
 }

 /**
  * Get the login of the current user. 如果没有登陆的话就是空, 登陆过了就会有用户名
  *
  * @return the login of the current user.
  */
 public static Optional<String> getCurrentUsername() {
   final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

   if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
     return Optional.empty();
   }

   String username = null;
   if (authentication.getPrincipal() instanceof UserDetails) {
     UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
     username = springSecurityUser.getUsername();
   } else if (authentication.getPrincipal() instanceof String) {
     username = (String) authentication.getPrincipal();
   }

   return Optional.ofNullable(username);
 }

 /**
  * 直接返回存在的用户名, 如果没有登录就直接返回 401
  *
  * @return 401 or username
  */
 public static String getUsername() {
   final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

   if (authentication instanceof AnonymousAuthenticationToken || authentication == null) {
     throw new UnAuthException();
   }
   String username = null;
   if (authentication.getPrincipal() instanceof UserDetails) {
     UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
     username = springSecurityUser.getUsername();
   } else if (authentication.getPrincipal() instanceof String) {
     username = (String) authentication.getPrincipal();
   }
   return username;
 }
}
