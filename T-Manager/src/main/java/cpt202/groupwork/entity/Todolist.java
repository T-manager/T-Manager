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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todolist implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 主键，唯一标识todoList
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
   * todoList的名字
   */
  @Column(length = 20, name = "todolist_name")
  private String todolistName;

  /**
   * 总共有多少的todo
   */

  @Column(name = "todolist_total_num")
  private Integer todolistTotalNum = 0;


  /**
   * 完成的todo的数量
   */
  @Column(name = "todolist_complete_num")
  private Integer todolistCompleteNum = 0;
}
