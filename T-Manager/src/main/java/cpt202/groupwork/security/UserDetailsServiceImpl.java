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
import cpt202.groupwork.entity.*;
import cpt202.groupwork.repository.UserRepository;

/**
 * @author xian
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

//    @Resource
//    private SysUserMapper sysUserMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        cpt202.groupwork.entity.User user = userRepository.findByUsername(username);
        if (user == null ) {
            throw new UsernameNotFoundException(username);
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // Spring Security 角色名称默认使用 "ROLE_" 开头
        // authorities.add 可以增加多个用户角色，对于一个用户有多种角色的系统来说，
        // 可以通过增加用户角色表、用户--角色映射表，存储多个用户角色信息
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        // 给 Spring Security 传入用户名、用户密码、用户角色。
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}