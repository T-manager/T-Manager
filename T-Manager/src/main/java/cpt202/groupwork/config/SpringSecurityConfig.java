package cpt202.groupwork.config;

import cpt202.groupwork.security.ErrorAuthenticationEntryPoint;
import cpt202.groupwork.security.TokenFilter;
import cpt202.groupwork.security.UserDetailsServiceImpl;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description: Security Config
 * @author: wzh
 * @create: 2021-03-21 19:23
 **/

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Resource
  private UserDetailsServiceImpl userDetailsService;

  @Resource
  private ErrorAuthenticationEntryPoint errorAuthenticationEntryPoint;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Resource
  private TokenFilter tokenFilter;

  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    // ?????? BCryptPasswordEncoder ????????????
    authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt ??????
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * ??????????????????
   */
  @Override
  public void configure(WebSecurity webSecurity) {
    //?????????????????????,?????????????????????????????????
    webSecurity.ignoring().antMatchers(
        "/",
        "/css/**",
        "/js/**",
        "/images/**"
    );
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.headers().disable();
    httpSecurity.cors()
            .and()
            .csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/").permitAll();
    // ?????? CSRF ??????,??????????????????
//    httpSecurity.csrf().disable();
    // ???????????????????????????????????????
    httpSecurity.exceptionHandling().authenticationEntryPoint(errorAuthenticationEntryPoint);
    // ?????? Session
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // ?????? ?????? ????????? api ?????????????????????????????????????????????
    httpSecurity.authorizeRequests()
        .antMatchers("/auth/**")
        .permitAll().anyRequest().authenticated();
    // ???????????????
    httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    // ????????????
    httpSecurity.headers().cacheControl();
  }
}
