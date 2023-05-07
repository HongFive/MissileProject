package edu.szu.utils;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ZktDevice;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class AgvTransferUtils {
    public static AgvInfo convert(ZktDevice zktDevice) throws JSONException {
        AgvInfo agvInfo = new AgvInfo();
        agvInfo.setId(zktDevice.getId());
        agvInfo.setAgvName(zktDevice.getDeviceNum());
//        agvInfo.setId(zktDevice.getId());
        String deviceParams = zktDevice.getDeviceParams();
        JSONObject jsonObj = new JSONObject(deviceParams);
        agvInfo.setLocation(jsonObj.getString("location"));
        agvInfo.setType(jsonObj.getString("type"));
        return agvInfo;
    }
}
