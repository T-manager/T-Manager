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
 * @description: VerificationCode
 * @author: Zhonghao
 * @create:
 **/

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer verifyId;

  /**
   * 用户名
   */
  @Column(length = 20, unique = true)
  private String userName;

  /**
   * 用户邮箱
   */
  @Column(length = 100, unique = true)
  private String userEmail;

  /**
   * 验证码
   */
  @Column(length = 75)
  private String verifyPassword;


  /**
   * 验证码过期日期
   */
  @Column(name = "expire_time")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date expireTime;

}
