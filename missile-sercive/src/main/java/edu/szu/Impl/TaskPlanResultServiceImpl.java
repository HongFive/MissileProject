package edu.szu.Impl;

import edu.szu.TaskPlanResultService;
import edu.szu.mapper.OutTaskPlanResultPointInfoMapper;
import edu.szu.pojo.TaskPlanResultPointInfo;
import org.n3r.idworker.Id;
import org.n3r.idworker.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskPlanResultServiceImpl implements TaskPlanResultService {

    @Autowired
    private OutTaskPlanResultPointInfoMapper outTaskPlanResultPointInfoMapper;

    @Override
    public synchronized void SaveTaskPlanResult(TaskPlanResultPointInfo taskPlanResultPointInfo) {
//        Long id= Id.next();
//        taskPlanResultPointInfo.setId(id);
        outTaskPlanResultPointInfoMapper.insert(taskPlanResultPointInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void SavePathResultList(List<List<TaskPlanResultPointInfo>> lists) {
        for (List<TaskPlanResultPointInfo> list:lists){
            outTaskPlanResultPointInfoMapper.insertList(list);
        }
    }
}
