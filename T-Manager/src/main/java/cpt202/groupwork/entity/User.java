package cpt202.groupwork.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import lombok.Getter;

/**
 * 用户实体
 * <p>
 * [x] length, [x] index
 */
@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;

  /**
   * 用户名
   */
  @Column(length = 20, unique = true)
  private String userName;

  /**
   * 用户邮箱
   */
  @Column(length = 100)
  private String userEmail;

  /**
   * 密码
   */
//  @JsonIgnore//后台返回时 用来屏蔽此字段
  @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
  @Column(length = 75)
  private String userPassword;



  /**
   * 用户角色
   */
  @Column(length = 20)
  private String userRole;
  /**
   * 用户头像
   */
  @Column(length = 100)
  private String userAvatar;

  public User() {

  }
}
