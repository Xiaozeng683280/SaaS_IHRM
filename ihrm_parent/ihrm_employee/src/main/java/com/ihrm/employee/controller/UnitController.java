package com.ihrm.employee.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.employee.Unit;
import com.ihrm.employee.service.UnitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/agreement")
public class UnitController extends BaseController {
    @Resource
    private UnitService unitService;



    //查询全部企业列表
    @RequestMapping(value="/unit",method = RequestMethod.GET)
    public Result findAll() {
        List<Unit> list = unitService.findAll();

        Result result = new Result(ResultCode.SUCCESS);
        result.setData(list);
        return result;
    }

}
