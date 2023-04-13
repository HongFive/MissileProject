package edu.szu.Impl;

import edu.szu.TaskService;
import edu.szu.mapper.TaskInfoMapper;
import edu.szu.pojo.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskInfoMapper taskInfoMapper;

    @Override
    public List<TaskInfo> queryTaskList() {
        return taskInfoMapper.selectAll();
    }
}
