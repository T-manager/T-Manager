package cpt202.groupwork.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @className: Todolist
 * @description: connect to the SQL database for Todolist entity
 * @Author: CPT202 Group 2
 * @version 1.0
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todolist implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * primary key, to identify the gantt
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer todolistId;

  /**
   * 所属project的id
   */
  @Column(name = "project_id")
  private Integer projectId;

  /**
   * todoList name
   */
  @Column(length = 20, name = "todolist_name")
  private String todolistName;

  /**
   * all to-dos belong to this todolist
   */

  @Column(name = "todolist_total_num")
  private Integer todolistTotalNum = 0;


  /**
   * all finished to-dos
   */
  @Column(name = "todolist_complete_num")
  private Integer todolistCompleteNum = 0;
}
