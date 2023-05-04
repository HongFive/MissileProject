package edu.szu.Impl;

import edu.szu.TaskResultService;
import edu.szu.mapper.OutTaskPlanResultPointInfoMapper;
import edu.szu.pojo.TaskPlanResultPointInfo;
import org.n3r.idworker.Id;
import org.n3r.idworker.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskResultServiceImpl implements TaskResultService {

    @Autowired
    private OutTaskPlanResultPointInfoMapper taskPlanResultPointInfoMapper;

    @Autowired
    private IdWorker idworker;

    @Override
    public void saveTaskResult(TaskPlanResultPointInfo taskPlanResultPointInfo) {
        taskPlanResultPointInfo.setId((int) idworker.nextId());
        taskPlanResultPointInfoMapper.insert(taskPlanResultPointInfo);
    }
}
