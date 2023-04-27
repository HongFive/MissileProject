package edu.szu;

import edu.szu.pojo.AgvInfo;

import java.util.List;

public interface OutAgvService {
    /**
     * 查找所有agv信息
     */
    public List<AgvInfo> queryAgvList();

    /**
     * 根据id查询货架信息
     */
    public AgvInfo queryAgvInfo(Integer id);
}
