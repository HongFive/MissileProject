package edu.szu.Impl;

import edu.szu.AgvService;
import edu.szu.mapper.AgvInfoMapper;
import edu.szu.pojo.AgvInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AgvServiceImpl implements AgvService {

    @Autowired
    private AgvInfoMapper agvInfoMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<AgvInfo> queryAgvList() {
        return agvInfoMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public AgvInfo queryAgvInfo(Integer agvId) {
        Example agvExample = new Example(AgvInfo.class);
        Example.Criteria criteria = agvExample.createCriteria();
        criteria.andEqualTo("id", agvId);
        AgvInfo agv=agvInfoMapper.selectOneByExample(agvExample);
        return agv;
    }
}
