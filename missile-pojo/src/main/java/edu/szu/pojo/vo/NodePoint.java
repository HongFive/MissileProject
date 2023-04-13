package edu.szu.pojo.vo;

import edu.szu.pojo.ShelfInfo;

import javax.persistence.*;
import java.util.List;

public class NodePoint {

    /**
     * 点位
     */
    @Id
    private Integer point;

    /**
     * 该点位货架信息（如果有）
     */
    public List<ShelfInfo> shelfs;

    /**
     * 该点位是否被占用
     */
    private boolean state;

    /**
     * 该点位转载区信息（如果有）
     */
    public ReprintArea area;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public List<ShelfInfo> getShelfs() {
        return shelfs;
    }

    public void setShelfs(List<ShelfInfo> shelfs) {
        this.shelfs = shelfs;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public ReprintArea getArea() {
        return area;
    }

    public void setArea(ReprintArea area) {
        this.area = area;
    }
}
