package cpt202.groupwork.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import lombok.NoArgsConstructor;

/**
 * @className: VerificationCode
 * @description: connect to the SQL database for Verification entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the verified code id
   */
  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer verifyId;

  /**
   *  username
   */
  @Column(length = 20, unique = true)
  private String userName;

  /**
   * user e-mail
   */
  @Column(length = 100, unique = true)
  private String userEmail;

  /**
   *  verification code
   */
  @Column(length = 75)
  private String verifyPassword;


  /**
   * verification expire time
   */
  @Column(name = "expire_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date expireTime;

}
