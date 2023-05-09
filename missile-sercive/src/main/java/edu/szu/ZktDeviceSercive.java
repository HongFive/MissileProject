package edu.szu;

import edu.szu.pojo.ZktDevice;

import java.util.List;

public interface ZktDeviceSercive {
    public List<ZktDevice> queryZktDeviceList();
    public List<ZktDevice> queryZktDevice(String deviceType);
}
