package edu.szu.Impl;

import edu.szu.TaskService;
import edu.szu.mapper.TaskInfoMapper;
import edu.szu.pojo.TaskInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<TaskInfo> queryTaskList() {
        return taskInfoMapper.selectAll();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveTask(TaskInfo taskInfo) {
        Integer id= Integer.valueOf(sid.nextShort());
        taskInfo.setId(id);
        taskInfoMapper.insertSelective(taskInfo);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public TaskInfo queryTaskInfo(Integer taskId) {
        Example taskExample = new Example(TaskInfo.class);
        Example.Criteria criteria = taskExample.createCriteria();
        criteria.andEqualTo("id", taskId);
        TaskInfo task=taskInfoMapper.selectOneByExample(taskExample);
        return task;
    }
}
