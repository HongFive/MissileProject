package edu.szu;

import edu.szu.pojo.TaskInfo;

import java.util.List;

public interface TaskService {

    /**
     * 查找所有任务信息
     */
    public List<TaskInfo> queryTaskList();
}
