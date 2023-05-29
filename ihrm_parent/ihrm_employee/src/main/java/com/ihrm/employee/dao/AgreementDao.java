package com.ihrm.employee.dao;

import com.ihrm.domain.employee.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 */
public interface AgreementDao extends JpaRepository<Project, String>, JpaSpecificationExecutor<Project> {
    //    Project findById(String uid);
    @Query(nativeQuery = true,
            value = "SELECT pro.*,dept.name as department_name,unit.unit_name FROM em_project pro " +
                    "LEFT JOIN co_department dept on pro.department_id = dept.id " +
                    "left join em_unit unit on pro.unit_id = unit.id " +
                    "WHERE if( IFNULL(?1,'') !='' ,pro.company_id = ?1,1=1) and  (coalesce (?2 , null) is null or pro.department_id in ?2 ) "
    )
    Page<Project> findPage(String companyId, List<String> departmentIdList, PageRequest pageRequest);



}