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
 * @className: SpringSecurityConfig
 * @description: TODO
 * @Author: CPT202 Group 2
 * @version 1.0
 */

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

  /**
   *
   * @param authenticationManagerBuilder
   * @throws Exception
   */
  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    // Use BCryptPasswordEncoder to verify the password
    authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
  }

  /**
   *
   * @return
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt password
    return new BCryptPasswordEncoder();
  }

  /**
   *
   * @return
   * @throws Exception
   */
  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   *
   * @param webSecurity
   */
  @Override
  public void configure(WebSecurity webSecurity) {
    //Do not intercept static resources and resource all user could access
    webSecurity.ignoring().antMatchers(
        "/",
        "/css/**",
        "/js/**",
        "/images/**"
    );
  }

  /**
   *
   * @param httpSecurity
   * @throws Exception
   */
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
    // config CSRF to off, allowcross-domain access
//    httpSecurity.csrf().disable();
    // determine class for handling unauthorized access
    httpSecurity.exceptionHandling().authenticationEntryPoint(errorAuthenticationEntryPoint);
    // turn off Session
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // allow unauthorized api access for registered and logined user, ask authorized access for others
    httpSecurity.authorizeRequests()
        .antMatchers("/auth/**")
        .permitAll().anyRequest().authenticated();
    // add interceptor
    httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    // disable cache
    httpSecurity.headers().cacheControl();
  }
}
