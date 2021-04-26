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
    // 使用 BCryptPasswordEncoder 验证密码
    authenticationManagerBuilder.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // BCrypt 密码
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  /**
   * 静态资源设置
   */
  @Override
  public void configure(WebSecurity webSecurity) {
    //不拦截静态资源,所有用户均可访问的资源
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
    // 配置 CSRF 关闭,允许跨域访问
//    httpSecurity.csrf().disable();
    // 指定错误未授权访问的处理类
    httpSecurity.exceptionHandling().authenticationEntryPoint(errorAuthenticationEntryPoint);
    // 关闭 Session
    httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 允许 登录 注册的 api 的无授权访问，其他需要授权访问
    httpSecurity.authorizeRequests()
        .antMatchers("/auth/**")
        .permitAll().anyRequest().authenticated();
    // 添加拦截器
    httpSecurity.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    // 禁用缓存
    httpSecurity.headers().cacheControl();
  }
}
