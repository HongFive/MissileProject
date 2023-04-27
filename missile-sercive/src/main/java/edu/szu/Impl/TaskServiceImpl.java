package edu.szu.Impl;

import edu.szu.OutTaskService;
import edu.szu.mapper.OutTaskInfoMapper;
import edu.szu.pojo.TaskInfo;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class TaskServiceImpl implements OutTaskService {

    @Autowired
    private OutTaskInfoMapper outTaskInfoMapper;

    @Autowired
    private Sid sid;

    @Override
    public List<TaskInfo> queryTaskList() {
        return outTaskInfoMapper.selectAll();
    }

    @Override
    public void saveTask(TaskInfo taskInfo) {
        Integer id= Integer.valueOf(sid.nextShort());
        taskInfo.setId(id);
        outTaskInfoMapper.insertSelective(taskInfo);
    }

    @Override
    public TaskInfo queryTaskInfo(Integer id) {
        Example taskExample = new Example(TaskInfo.class);
        Example.Criteria criteria = taskExample.createCriteria();
        criteria.andEqualTo("id", id);
        TaskInfo task=outTaskInfoMapper.selectOneByExample(taskExample);
        return task;
    }

}
