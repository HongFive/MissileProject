package edu.szu.Impl;

import edu.szu.ShelfService;
import edu.szu.mapper.ShelfInfoMapper;
import edu.szu.pojo.ShelfInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


import java.util.List;

public class ShelfServiceImpl implements ShelfService {

    @Autowired
    private ShelfInfoMapper shelfInfoMapper;

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
