package com.ihrm.employee.dao;

import com.ihrm.domain.employee.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 数据访问接口
 */
public interface UnitDao extends JpaRepository<Unit, String>, JpaSpecificationExecutor<Unit> {
//    Project findById(String uid);
}