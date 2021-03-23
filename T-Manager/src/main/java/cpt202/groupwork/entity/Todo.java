package cpt202.groupwork.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键，唯一标识todo
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer todoId;

  /**
   * 所属todoList的id
   */
  @Column(name = "todolist_id")
  private Integer todolistId;

  /**
   * to-do的名字
   */
  @Column(length = 20,name="todo_name")
  private String todoName;

  /**
   * to-do的描述
   */
  @Column(length = 100,name="todo_detail")
  private String todoDetail;

  /**
   * to-do的截止日期
   */
  @Column(name = "todo_ddl")
  private Date todoDdl;

  /**
   * to-do的执行人 user_id
   */
  @Column(name = "todo_member")
  private Integer todoMember;

  /**
   * to-do的是否完成
   */
  @Column(name = "todo_check")
  private Boolean todoCheck;
}
