package com.ihrm.domain.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//转正申请
@Entity
@Table(name = "em_project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project implements Serializable {
    private static final long serialVersionUID = 2391824518947910774L;
    /**
     * 项目ID
     */
    @Id
    private String id;
    /**
     * 项目编码
     */
    private String projectId;
    /**
     * 往来单位编码
     */
    private String unitId;
//    /**
//     * 往来单位名称
//     */
//    private String unitName;
//
    /**
     * 附件
     */
    private String projectName;
    /**
     * 部门id
     */
    private String departmentId;
//    /**
//     * 部门名称
//     */
//    private String departmentName;

    /**
     * 创建时间
     */
    private String describes;

    /**
     * 项目合同路径
     */
    private String filePath;
    /**
     * 项目合同名称
     */
    private String fileName;
    /**
     * 附件
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    /**
     * 单据状态 1是未执行，2是已执行
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private  Date endTime;
    /**
     * 项目进行状态 0 履行之中 1 终止合同
     */
    private Integer state;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 公司id
     */
    private String  companyId;
}
