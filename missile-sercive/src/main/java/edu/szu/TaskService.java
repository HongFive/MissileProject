package edu.szu;

import edu.szu.pojo.TaskInfo;

import java.util.List;

public interface TaskService {

    /**
     * 查找所有任务信息
     */
    public List<TaskInfo> queryTaskList();

    /**
     * 添加新任务
     */
    public void saveTask(TaskInfo taskInfo);

    /**
     * 查询任务信息
     */
    public TaskInfo queryTaskInfo(Integer id);


}
