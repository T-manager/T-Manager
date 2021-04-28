package cpt202.groupwork.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cpt202.groupwork.repository.UserRepository;

/**
 * @className: UserDetailsServiceImpl
 * @description: implement user detail service, load user information after login
 * @Author: CPT202 Group 2
 * @version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * load user by user name
     * @param username
     * @return user or user name not find
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<cpt202.groupwork.entity.User> user = userRepository.findByUserName(username);
        if(user.isPresent()) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.get().getUserRole()));
            // pass username password, role to Spring Security
            return new User(username, user.get().getUserPassword(), authorities);
        }
        else
            throw new UsernameNotFoundException(username);
    }
}