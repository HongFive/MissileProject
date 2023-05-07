package edu.szu;

import edu.szu.pojo.ZktDevice;
import edu.szu.pojo.ZktWarehouseShelf;

import java.util.List;

public interface ZktDeviceSercive {
    public List<ZktDevice> queryZktDeviceList();
    public List<ZktDevice> queryZktDevice(String deviceType);
}
