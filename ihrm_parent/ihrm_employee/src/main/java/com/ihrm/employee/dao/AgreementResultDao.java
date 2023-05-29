package com.ihrm.employee.dao;

import com.ihrm.domain.employee.ProjectResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 数据访问接口
 */
public interface AgreementResultDao extends JpaRepository<ProjectResult, String>, JpaSpecificationExecutor<ProjectResult> {
//    Project findById(String uid);
    @Query(nativeQuery = true,
            value="SELECT pro.*,dept.name as department_name,unit.unit_name FROM em_project pro " +
                    "LEFT JOIN co_department dept on pro.department_id = dept.id " +
                    "left join em_unit unit on pro.unit_id = unit.id " +
                    "WHERE if( IFNULL(?1,'') !='' ,pro.company_id = ?1,1=1) and  (coalesce (?2 , null) is null or pro.department_id in ?2 ) " +
                    "and (pro.project_id like CONCAT('%', ?3, '%') or ?3 is null)  and if( IFNULL(?4,'') !='' ,pro.project_name = ?4,1=1) " +
                    "and if( IFNULL(?5,'') !='' ,pro.unit_id = ?5,1=1) ",
            countQuery = "select count(*) from em_project pro LEFT JOIN co_department dept on pro.department_id = dept.id  " +
                    "left join em_unit unit on pro.unit_id = unit.id WHERE if( IFNULL(?1,'') !='' ,pro.company_id = ?1,1=1) and  (coalesce (?2 , null) is null or pro.department_id in ?2 ,1=1) " +
                    "and (pro.project_id like CONCAT('%', ?3, '%') or ?3 is null) and if( IFNULL(?4,'') !='' ,pro.project_name = ?4,1=1) " +
                    "and if( IFNULL(?5,'') !='' ,pro.unit_id = ?5,1=1) "

    )
    Page<ProjectResult> findPage(String companyId, List<String> departmentIdList, String projectId,String projectName,String unitId,PageRequest pageRequest);
    @Query(nativeQuery = true,
            value = "SELECT pro.*,dept.name as department_name,unit.unit_name FROM em_project pro " +
                    "LEFT JOIN co_department dept on pro.department_id = dept.id " +
                    "left join em_unit unit on pro.unit_id = unit.id " +
                    "WHERE if( IFNULL(?1,'') !='' ,pro.id = ?1,1=1) "
    )
    ProjectResult findInfoById(String id);
}