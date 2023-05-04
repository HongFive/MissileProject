package edu.szu;

import edu.szu.pojo.ZktTask;

import java.util.List;

public interface ZktTaskSercive {

    public List<ZktTask> queryTaskList();

    public ZktTask queryTask(Integer id);
}
