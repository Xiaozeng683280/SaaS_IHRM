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
@Table(name = "em_unit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unit implements Serializable {
    private static final long serialVersionUID = 2391824518947910774L;
    /**
     * 项目ID
     */
    @Id
    private String id;
    /**
     * 项目编码
     */
    private String unitName;
    /**
     * 往来单位编码
     */
    private String unitCode;
    /**
     * 附件
     */
    private String invoiceInformation;
    /**
     * 公司id
     */
    private String phone;
    /**
     * 创建时间
     */
    private String address;

    /**
     * 往来单位编码
     */
    private String describe;

    /**
     * 公司id
     */
    private String  companyId;
}
