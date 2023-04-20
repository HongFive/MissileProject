package edu.szu.pojo.vo;

import edu.szu.pojo.AgvInfo;

import javax.persistence.*;
import java.util.List;

public class ReprintArea {

    /**
     * 上转载区与下转载区
     */

    private Integer AreaId;

    /**
     * 转载区agv信息
     */
    public List<AgvInfo> agvInfoList;

    public ReprintArea(Integer areaId, List<AgvInfo> agvInfoList) {
        AreaId = areaId;
        this.agvInfoList = agvInfoList;
    }

    public Integer getAreaId() {
        return AreaId;
    }

    public void setAreaId(Integer areaId) {
        AreaId = areaId;
    }

    public List<AgvInfo> getAgvInfoList() {
        return agvInfoList;
    }

    public void setAgvInfoList(List<AgvInfo> agvInfoList) {
        this.agvInfoList = agvInfoList;
    }
}
