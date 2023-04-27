package edu.szu;

import edu.szu.pojo.ShelfInfo;

import java.util.List;

public interface OutShelfService {
    /**
     * 查找所有货架信息
     */
    public List<ShelfInfo> queryShelfList();

    /**
     * 根据id查询货架信息
     */
    public ShelfInfo queryShelfInfo(Integer id);
}
