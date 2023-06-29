package edu.szu;

import edu.szu.pojo.TaskPlanResultPointInfo;

import java.util.List;

public interface TaskPlanResultService {

    public void SaveTaskPlanResult(TaskPlanResultPointInfo taskPlanResultPointInfo);
    public void SavePathResultList(List<List<TaskPlanResultPointInfo>> lists);
}
