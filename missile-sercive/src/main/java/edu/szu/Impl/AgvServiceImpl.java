package edu.szu.Impl;

import edu.szu.OutAgvService;
import edu.szu.mapper.OutAgvInfoMapper;
import edu.szu.pojo.AgvInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AgvServiceImpl implements OutAgvService {

    @Autowired
    private OutAgvInfoMapper agvInfoMapper;

    @Override
    public List<AgvInfo> queryAgvList() {
        return agvInfoMapper.selectAll();
    }

    @Override
    public AgvInfo queryAgvInfo(Integer id) {
        Example agvExample = new Example(AgvInfo.class);
        Example.Criteria criteria = agvExample.createCriteria();
        criteria.andEqualTo("id", id);
        AgvInfo agv=agvInfoMapper.selectOneByExample(agvExample);
        return agv;
    }

//    @Transactional(propagation = Propagation.SUPPORTS)
//    @Override
//    public List<AgvInfo> queryAgvList() {
//        return agvInfoMapper.selectAll();
//    }
//
//    @Transactional(propagation = Propagation.SUPPORTS)
//    @Override
//    public AgvInfo queryAgvInfo(Integer agvId) {
//        Example agvExample = new Example(AgvInfo.class);
//        Example.Criteria criteria = agvExample.createCriteria();
//        criteria.andEqualTo("id", agvId);
//        AgvInfo agv=agvInfoMapper.selectOneByExample(agvExample);
//        return agv;
//    }
}
