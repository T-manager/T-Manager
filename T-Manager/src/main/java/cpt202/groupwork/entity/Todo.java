package cpt202.groupwork.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: Todo
 * @description: connect to the SQL database for Todo entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the todo
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer todoId;

  /**
   * id of todolist that belongs to
   */
  @Column(name = "todolist_id")
  private Integer todolistId;

  /**
   * to-do name
   */
  @Column(length = 20, name="todo_name")
  private String todoName;

  /**
   * to-do detail
   */
  @Column(length = 100, name="todo_detail")
  private String todoDetail;

  /**
   * to-do deadline
   */
  @Column(name = "todo_ddl")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date todoDdl;

  /**
   * to-do executor's user id
   */
  @Column(name = "todo_member")
  private Integer todoMember;

  /**
   * whether the to-do is finished
   */
  @Column(name = "todo_check")
  private Boolean todoCheck;
}
