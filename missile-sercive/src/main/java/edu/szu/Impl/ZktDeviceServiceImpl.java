package edu.szu.Impl;

import edu.szu.ZktDeviceSercive;

import edu.szu.mapper.ZktDeviceMapper;
import edu.szu.pojo.ZktDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class ZktDeviceServiceImpl implements ZktDeviceSercive {

    @Autowired
    private ZktDeviceMapper zktDeviceMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ZktDevice> queryZktDeviceList() {
        return zktDeviceMapper.selectAll();
    }

    @Override
    public List<ZktDevice> queryZktDevice(String deviceType) {
        Example taskExample = new Example(ZktDevice.class);
        Example.Criteria criteria = taskExample.createCriteria();
        criteria.andEqualTo("deviceType", deviceType);
        List<ZktDevice> zktDevices = zktDeviceMapper.selectByExample(taskExample);
        return zktDevices;
    }

}
