
package cpt202.groupwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;

import javax.validation.constraints.Size;

@Data
public class TodoCalendarDTO {
    // 在个人日历页查看todo的信息

    // todo的名字
    @Size(min = 1, max = 20, message = "todo名称必须在1到20个字之间哦")
    private String todoName;

    // 对应项目id
    private Integer projectId;

    // 对应项目名
    private String projectName;

    // todo所属的todolist
    private Integer todolistId;

    // todolist的名字
    private String todolistName;

    // todo的详细介绍
    @Size(min = 0, max = 100, message = "详细介绍必须小于100个字哦")
    private String todoDetail;

    // todo截止日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date todoDdl;

    // 是否完成
    private Boolean todoCheck;

}
