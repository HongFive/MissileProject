package edu.szu.Impl;

import edu.szu.ZktTaskSercive;
import edu.szu.mapper.ZktTaskMapper;
import edu.szu.pojo.ZktTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ZktTaskServiceImpl implements ZktTaskSercive {

    @Autowired
    private ZktTaskMapper zktTaskMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ZktTask> queryTaskList() {
        return zktTaskMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ZktTask queryTask(String id) {
        Example taskExample = new Example(ZktTask.class);
        Example.Criteria criteria = taskExample.createCriteria();
        criteria.andEqualTo("id", id);
        ZktTask task=zktTaskMapper.selectOneByExample(taskExample);
        return task;
    }
}
