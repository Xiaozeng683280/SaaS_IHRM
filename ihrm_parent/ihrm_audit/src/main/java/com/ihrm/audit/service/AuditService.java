package com.ihrm.audit.service;

import com.alibaba.fastjson.JSON;
import com.ihrm.audit.client.FeignClientService;
import com.ihrm.audit.dao.ProcInstanceDao;
import com.ihrm.audit.dao.ProcTaskInstanceDao;
import com.ihrm.audit.dao.ProcUserGroupDao;
import com.ihrm.audit.entity.ProcInstance;
import com.ihrm.audit.entity.ProcTaskInstance;
import com.ihrm.audit.entity.ProcUserGroup;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.User;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author: hyl
 * @date: 2020/03/20
 **/
@Service
public class AuditService {

    @Resource
    private ProcInstanceDao procInstanceDao;

    @Resource
    private FeignClientService feignClientService;

    @Resource
    private IdWorker idWorker;

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private ProcTaskInstanceDao procTaskInstanceDao;

    @Resource
    private ProcUserGroupDao procUserGroupDao;

    @Resource
    private EntityManager entityManager;

    /**
     * 查询所有的业务申请流程
     *
     * @param instance 流程实例
     * @param page     页码
     * @param size     每页大小
     * @return
     */
    public Page getInstanceList(ProcInstance instance, int page, int size) {
        //使用Specification进行查询
        Specification<ProcInstance> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            //审批类型
            if (!StringUtils.isEmpty(instance.getProcessKey())) {
                list.add(cb.equal(root.get("processKey").as(String.class), instance.getProcessKey()));
            }
            //审批状态
            if (!StringUtils.isEmpty(instance.getProcessState())) {
                Expression<String> exp = root.get("processState");
                list.add(exp.in(instance.getProcessState().split(",")));
            }
            //当前节点的待处理人
            if (!StringUtils.isEmpty(instance.getProcCurrNodeUserId())) {
                list.add(cb.like(root.get("procCurrNodeUserId").as(String.class), "%" + instance.getProcCurrNodeUserId() + "%"));
            }
            //发起人 -- userId
            if (!StringUtils.isEmpty(instance.getUserId())) {
                list.add(cb.equal(root.get("userId").as(String.class), instance.getUserId()));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        //构造查询条件
        //调用dao进行specification查询
        return procInstanceDao.findAll(spec, PageRequest.of(page - 1, size));
    }

    /**
     * 根据id查询申请的详细数据
     *
     * @param id 流程实例id
     * @return id对应申请的详细数据
     */
    public ProcInstance findInstanceDetail(String id) {
        Optional<ProcInstance> procInstanceOptional = procInstanceDao.findById(id);
        return procInstanceOptional.orElse(null);
    }

    /**
     * 流程申请
     *
     * @param map       内容参数
     * @param companyId 企业id
     */
    public void startProcess(Map<String, String> map, String companyId) {
        if(StringUtils.isEmpty(companyId)){
            companyId = "1";
        }
        //构造业务数据
        String userId = map.get("userId");
        String processKey = map.get("processKey");
        String processName = map.get("processName");
        System.out.println("111111");
        User user = feignClientService.getUserInfoByUserId(userId);
        ProcInstance instance = new ProcInstance();
        BeanUtils.copyProperties(user, instance);
        System.out.println("22222");
        instance.setUserId(userId);
        instance.setProcessId(idWorker.nextId() + "");
        instance.setProcApplyTime(new Date());
        instance.setProcessKey(processKey);
        instance.setProcessName(processName);
        //审批中
        instance.setProcessState("1");
        String data = JSON.toJSONString(map);
        instance.setProcData(data);

        System.out.println("33");
        //查询流程定义
        ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processKey).processDefinitionTenantId(companyId).latestVersion().singleResult();
        System.out.println("44");
        //开启流程
        Map<String, Object> vars = new HashMap<>(16);
        if ("process_leave".equals(processKey)) {
            //请假
            vars.put("days", map.get("duration"));
        }
        System.out.println("55");
        //流程定义的id,业务数据id,内置的参数
        runtimeService.startProcessInstanceById(result.getId(), instance.getProcessId(), vars);
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(result.getId(), instance.getProcessId(), vars);
        System.out.println("66");
        //自动执行第一个任务节点
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.complete(task.getId());
        System.out.println("77");
        //获取下一个节点数据,填充业务数据中当前待审批人
        Task nextTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        System.out.println("88");
        if (nextTask != null) {
            List<User> users = findCurrUsers(nextTask, user);
            StringBuilder userNameSb = new StringBuilder();
            StringBuilder userIdS = new StringBuilder();
            for (User user1 : users) {
                userNameSb.append(user1.getUsername()).append(" ");
                userIdS.append(user1.getId());
            }
            instance.setProcCurrNodeUserId(userIdS.toString());
            instance.setProcCurrNodeUserName(userNameSb.toString());
        }

        procInstanceDao.save(instance);
        ProcTaskInstance pti = new ProcTaskInstance();
        pti.setTaskId(idWorker.nextId() + "");
        pti.setProcessId(instance.getProcessId());
        pti.setHandleTime(new Date());
        //审批通过
        pti.setHandleType("2");
        pti.setHandleUserId(userId);
        pti.setHandleUserName(user.getUsername());
        pti.setTaskKey(task.getTaskDefinitionKey());
        pti.setTaskName(task.getName());
        pti.setHandleOpinion("发起申请");
        procTaskInstanceDao.save(pti);
    }

    private List<User> findCurrUsers(Task nextTask, User user) {
        //查询任务的节点数据(候选人组)
        List<IdentityLink> list = taskService.getIdentityLinksForTask(nextTask.getId());
        List<User> users = new ArrayList<>();
        for (IdentityLink identityLink : list) {
            //候选人组
            String groupId = identityLink.getGroupId();
            //查询UserGroup
            ProcUserGroup procUserGroup = procUserGroupDao.findById(groupId).orElse(null);
            if (procUserGroup == null) {
                continue;
            }
            String param = procUserGroup.getParam();
            String paramValue = "";
            if ("user_id".equals(param)) {
                paramValue = user.getId();
            } else if ("department_id".equals(param)) {
                paramValue = user.getDepartmentId();
            } else if ("company_id".equals(param)) {
                paramValue = user.getCompanyId();
            }
            String sql = procUserGroup.getIsql().replaceAll("\\$\\{" + param + "}", paramValue);
            Query query = entityManager.createNativeQuery(sql);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(User.class));
            if (!CollectionUtils.isEmpty(query.getResultList())) {
                users.addAll(query.getResultList());
            }
        }
        return users;
    }

    /**
     * 提交审核
     *
     * @param taskInstance 流程任务
     */
    public void commit(ProcTaskInstance taskInstance) {
        //查询业务流程对象
        String processId = taskInstance.getProcessId();
        ProcInstance instance = procInstanceDao.findById(processId).orElse(null);
        if (instance == null) {
            return;
        }
        //设置业务流程状态
        instance.setProcessState(taskInstance.getHandleType());
        //根据不同的操作类型,完成不同的业务处理
        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(processId).list();

        User user = feignClientService.getUserInfoByUserId(taskInstance.getHandleUserId());
        if ("2".equals(taskInstance.getHandleType())) {
            //如果审核通过,完成当前的任务
            //查询出当前节点,完成当前节点任务
            Task task = taskService.createTaskQuery().processInstanceId(processInstanceList.get(0).getId()).singleResult();
            taskService.complete(task.getId());
            //查询出下一个节点,如果存在下一个流程没有结束
            Task nextTask = taskService.createTaskQuery().processInstanceId(processInstanceList.get(0).getId()).singleResult();
            if (nextTask != null) {
                List<User> users = findCurrUsers(nextTask, user);
                StringBuilder usernames = new StringBuilder();
                StringBuilder userIdS = new StringBuilder();
                for (User user1 : users) {
                    usernames.append(user1.getUsername()).append(" ");
                    userIdS.append(user1.getId());
                }
                instance.setProcCurrNodeUserId(userIdS.toString());
                instance.setProcCurrNodeUserName(usernames.toString());
                instance.setProcessState("1");
            } else {
                //如果不存在下一个节点,任务结束
                instance.setProcessState("2");
            }
        } else {
            //如果审核不通过/撤销,删除流程
            runtimeService.deleteProcessInstance(processInstanceList.get(0).getId(), taskInstance.getHandleOpinion());
        }
        //更新业务流程对象,保存业务任务对象
        procInstanceDao.save(instance);
        taskInstance.setTaskId(idWorker.nextId() + "");
        taskInstance.setHandleUserName(user.getUsername());
        taskInstance.setHandleTime(new Date());
        procTaskInstanceDao.save(taskInstance);
    }

    public List<ProcTaskInstance> findTasksByProcess(String id) {
        return procTaskInstanceDao.findByProcessId(id);
    }
}
