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
 * @className: User
 * @description: connect to the SQL database for User entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@Builder
@AllArgsConstructor
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the user
   */
  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer userId;

  /**
   * user name
   */
  @Column(length = 20, unique = true)
  private String userName;

  /**
   * email of user
   */
  @Column(length = 100, unique = true)
  private String userEmail;

  /**
   * password
   */
  @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
  @Column(length = 75)
  private String userPassword;

  /**
   * role of the user
   */
  @Column(length = 20)
  private String userRole;

  /**
   * avatar of the user
   */
  @Column(length = 100)
  private String userAvatar;

  public User() {

  }
}
