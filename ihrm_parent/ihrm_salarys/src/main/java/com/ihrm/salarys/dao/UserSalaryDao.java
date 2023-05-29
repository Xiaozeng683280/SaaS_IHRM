package com.ihrm.salarys.dao;

import com.ihrm.domain.salarys.UserSalary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * 自定义dao接口继承
 * JpaRepository<实体类，主键>
 * JpaSpecificationExecutor<实体类>
 */
public interface UserSalaryDao extends JpaRepository<UserSalary, String>, JpaSpecificationExecutor<UserSalary> {

	@Query(nativeQuery = true,
			value="select bu.id,bu.username,bu.mobile,bu.work_number workNumber," +
					"bu.in_service_status inServiceStatus,bu.department_name departmentName,bu.department_id departmentId,bu.time_of_entry timeOfEntry ," +
					"bu.form_of_employment formOfEmployment ,sauss.current_basic_salary currentBasicSalary,sauss.current_post_wage currentPostWage from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id " +
					"WHERE if( IFNULL(?1,'') !='' ,bu.company_id = ?1,1=1) and  (coalesce (?2 , null) is null or bu.form_of_employment in ?2 ) " +
					" and (coalesce (?3 , null) is null or bu.in_service_status in ?3) and (coalesce (?4 , null) is null or bu.department_id in ?4 )"
	)
	Page<Map> findPage(String companyId, List<Integer> formOfEmploymentMap, List<Integer> inServiceStatusList,List<String> departmentIdList, PageRequest pageRequest);

	@Query(nativeQuery = true,
			value="select bu.id,bu.username,bu.mobile,bu.work_number workNumber," +
					"bu.in_service_status inServiceStatus,bu.department_name departmentName,bu.department_id departmentId,bu.time_of_entry timeOfEntry ," +
					"bu.form_of_employment formOfEmployment ,sauss.current_basic_salary currentBasicSalary,sauss.current_post_wage currentPostWage from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE sauss.user_id = ?1",
			countQuery = "select count(*) from bs_user bu LEFT JOIN sa_user_salary sauss ON bu.id=sauss.user_id WHERE sauss.user_id = ?1"
	)
	Map<String,String> getUserInfo(String userId);
}
