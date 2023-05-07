package edu.szu.Impl;

import edu.szu.ZktTaskSercive;
import edu.szu.ZktWarehouseShelfSercive;
import edu.szu.mapper.ZktTaskMapper;
import edu.szu.mapper.ZktWarehouseShelfMapper;
import edu.szu.pojo.ZktTask;
import edu.szu.pojo.ZktWarehouseShelf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ZktWarehouseShelfServiceImpl implements ZktWarehouseShelfSercive {
    @Autowired
    private ZktWarehouseShelfMapper zktWarehouseShelfMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ZktWarehouseShelf> queryZktWarehouseShelfList() {
        return zktWarehouseShelfMapper.selectAll();
    }
//    @Override
//    public ZktWarehouseShelf queryZktWarehouseShelf(String id) {
//        Example taskExample = new Example(ZktWarehouseShelf.class);
//        Example.Criteria criteria = taskExample.createCriteria();
//        criteria.andEqualTo("id", id);
//        ZktWarehouseShelf zktWarehouseShelf=zktWarehouseShelfMapper.selectOneByExample(taskExample);
//        return zktWarehouseShelf;
//    }

}
