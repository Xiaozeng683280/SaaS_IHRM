package com.ihrm.employee.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.domain.employee.Project;
import com.ihrm.domain.employee.ProjectResult;
import com.ihrm.employee.service.AgreementService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/agreement")
public class AgreementController extends BaseController {
    @Resource
    private AgreementService agreementService;

    /**
     * 根据id删除
     */
    @RequestMapping(value="/project/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        agreementService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 查询用户列表
     * @return
     */
    @RequestMapping(value = "/project" , method = RequestMethod.GET)
    public Result findAll(int page, int size, @RequestParam() Map map){

        System.out.println("project_start");
        //获取当前的企业id
        map.put("companyId" , companyId);
        PageResult pageProject = agreementService.findAll(map, page, size);
        return new Result(ResultCode.SUCCESS , pageProject);
    }
    /**
     * 项目信息修改保存
     */
    @RequestMapping(value = "/{id}/projectInfo", method = RequestMethod.PUT)
    public Result savePersonalInfo(@PathVariable(name = "id") String uid, @RequestBody Map map) throws Exception {
        Project project = BeanMapUtils.mapToBean(map, Project.class);
        project.setId(uid);
        project.setCompanyId(super.companyId);
        agreementService.save(project);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 项目信息读取
     */
    @RequestMapping(value = "/{id}/projectInfo", method = RequestMethod.GET)
    public Result findPersonalInfo(@PathVariable(name = "id") String uid) {
        ProjectResult info = agreementService.findById(uid);
        if(info == null) {
            info = new ProjectResult();
            info.setId(uid);
        }
        return new Result(ResultCode.SUCCESS,info);
    }
    /**
     * 保存
     * @return
     */
    @RequestMapping(value = "/project" , method = RequestMethod.POST)
    public Result save(@RequestBody Project project){
        //设置保存的用户id
        project.setCompanyId(companyId);
        //调用service完成保存用户
        agreementService.save(project);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 更新用户图片
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping("/upload")
    public Result upload( @RequestParam(value = "file" ,required = false) MultipartFile file,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {
//        public Result upload( St/ring  file) throws Exception {
        //1.调用service保存图片
        String imgUrl = null;
        try {
            //需要配置百度AI,如果不正确会抛出异常
            imgUrl = agreementService.uploadImage( file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //2.返回数据
        return new Result(ResultCode.SUCCESS , imgUrl);
    }

}
