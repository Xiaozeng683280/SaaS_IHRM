package com.ihrm.employee.service;

import com.ihrm.common.entity.PageResult;
import com.ihrm.common.utils.QiniuUploadUtil;
import com.ihrm.domain.employee.Project;
import com.ihrm.domain.employee.ProjectResult;
import com.ihrm.employee.dao.AgreementDao;
import com.ihrm.employee.dao.AgreementResultDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AgreementService {
    @Resource
    private AgreementDao agreementDao;
    @Resource
    private AgreementResultDao agreementResultDao;


    /**
     * 5.根据id删除部门
     */
    public void deleteById(String id) {
        agreementDao.deleteById(id);
    }

    public ProjectResult findById(String uid) {
        return agreementResultDao.findInfoById(uid);
    }
    public void save(Project project) {
        agreementDao.save(project);
    }
    public PageResult findAll(Map<String,Object> map, int page, int size){
        //部门
        List<String> departmentIdList = new ArrayList<>();
        if(map.containsKey("departmentChecks")){
            departmentIdList =(List<String>) map.get("departmentChecks");
        }
        String projectId = "";
        if(map.containsKey("projectId")){
            projectId = map.get("projectId").toString();
        }
        String projectName = "";
        if(map.containsKey("projectName")){
            projectName = map.get("projectName").toString();
        }
        String unitId = "";
        if(map.containsKey("unitId")){
            unitId = map.get("unitId").toString();
        }
        Integer state = null;
        if(map.containsKey("state")){
            state = Integer.parseInt(map.get("state").toString());
        }
        Page<ProjectResult> page1 = agreementResultDao.findPage(map.get("companyId").toString(),departmentIdList,projectId, projectName, unitId,PageRequest.of(page - 1, size));
        //分页
        page1.getContent();
        return new PageResult(page1.getTotalElements(),page1.getContent());
    }

    /**
     *  完成图片处理 (上传到七牛云存储并且注册到百度云AI人脸库中)
     * @param     用户id
     * @param file  用户上传的头像文件
     * @return      请求路径
     */
    public String uploadImage(MultipartFile file) throws Exception {
        //1.根据id查询用户
//        Project project = agreementDao.findById(id).get();
        //2.根据图片上传到七牛云存储,获取到请求路径
        String imgUrl = new QiniuUploadUtil().upload(new Date().getTime() + "", file.getBytes());
        //3.更新用户头像地址
//        user.setStaffPhoto(imgUrl);
//        userDao.save(user);
//        //判断是否已经注册面部信息
//        Boolean faceExist = baiduAiUtil.faceExist(id);
//        String imgBase64 = Base64.encodeBase64String(file.getBytes());
//        if (faceExist){
//            //更新
//            baiduAiUtil.faceUpdate(id , imgBase64);
//        }else{
//            //注册
//            baiduAiUtil.faceRegister(id , imgBase64);
//        }
        //4.返回路径
        return imgUrl;
    }
}
