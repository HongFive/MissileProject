package edu.szu.Impl;

import edu.szu.OutShelfService;
import edu.szu.mapper.OutShelfInfoMapper;
import edu.szu.pojo.ShelfInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

@Service
public class ShelfServiceImpl implements OutShelfService {

    @Autowired
    private OutShelfInfoMapper shelfInfoMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShelfInfo> queryShelfList() {
        return shelfInfoMapper.selectAll();
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ShelfInfo queryShelfInfo(Integer shelfId) {
        Example shelfExample = new Example(ShelfInfo.class);
        Example.Criteria criteria = shelfExample.createCriteria();
        criteria.andEqualTo("id", shelfId);
        ShelfInfo shelf=shelfInfoMapper.selectOneByExample(shelfExample);
        return shelf;
    }
}
