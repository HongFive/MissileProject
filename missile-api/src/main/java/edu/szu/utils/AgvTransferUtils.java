package edu.szu.utils;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ZktDevice;
import org.springframework.boot.configurationprocessor.json.JSONException;

public class AgvTransferUtils {
    public static AgvInfo convert(ZktDevice zktDevice) throws JSONException {
        AgvInfo agvInfo = new AgvInfo();
        agvInfo.setId(zktDevice.getId());
        agvInfo.setAgvName(zktDevice.getDeviceNum());
        agvInfo.setType(zktDevice.getType());
        agvInfo.setLocation(zktDevice.getLocation());
//        agvInfo.setId(zktDevice.getId());

//        String deviceParams = zktDevice.getDeviceParams();
//        JSONObject jsonObj = new JSONObject(deviceParams);
//        agvInfo.setLocation(jsonObj.getString("location"));
//        agvInfo.setType(jsonObj.getString("type"));
        return agvInfo;
    }
}
