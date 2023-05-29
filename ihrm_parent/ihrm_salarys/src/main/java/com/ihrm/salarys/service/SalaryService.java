package com.ihrm.salarys.service;

import com.ihrm.common.entity.PageResult;
import com.ihrm.domain.salarys.UserSalary;
import com.ihrm.salarys.dao.UserSalaryDao;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SalaryService {
	
    @Resource
    private UserSalaryDao userSalaryDao;

    //定薪或者调薪
    public void saveUserSalary(UserSalary userSalary) {
        userSalaryDao.save(userSalary);
    }

	//查询用户薪资
    public UserSalary findUserSalary(String userId) {
        Optional<UserSalary> optional = userSalaryDao.findById(userId);
        return optional.orElse(null);
    }

	//分页查询当月薪资列表
	public PageResult findAll(Integer page, Integer pageSize, String companyId,Map map) {


        //条件封装
        //聘用形式
        List<Integer> formOfEmploymentList = new ArrayList<>();
        if(map.containsKey("approvalsTypeChecks")){
            formOfEmploymentList =(List<Integer>) map.get("approvalsTypeChecks");
        }
        System.out.println(("formOfEmploymentList" + formOfEmploymentList));
        //员工状态
        List<Integer> inServiceStatusList = new ArrayList<>();
        if(map.containsKey("approvalsTypeChecks")){
            inServiceStatusList =(List<Integer>) map.get("approvalsStateChecks");
        }
        System.out.println(("inServiceStatusList" + inServiceStatusList));
        //部门
        List<String> departmentIdList = new ArrayList<>();
        if(map.containsKey("departmentChecks")){
            departmentIdList =(List<String>) map.get("departmentChecks");
        }
        System.out.println(("departmentIdList" + departmentIdList));
        Page page1 = userSalaryDao.findPage(companyId,formOfEmploymentList,inServiceStatusList ,departmentIdList,PageRequest.of(page - 1, pageSize));
		page1.getContent();
		return new PageResult(page1.getTotalElements(),page1.getContent());
	}
    public Map<String,String> getUserInfo(String userId){

        return userSalaryDao.getUserInfo(userId);
    }
}
