package com.ihrm.employee.service;

import com.ihrm.domain.employee.Unit;
import com.ihrm.employee.dao.UnitDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UnitService {
    @Resource
    private UnitDao unitDao;


    /**
     * 查询列表
     */
    public List<Unit> findAll() {
        return unitDao.findAll();
    }
}
