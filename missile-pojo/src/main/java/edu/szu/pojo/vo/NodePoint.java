package edu.szu.pojo.vo;

import edu.szu.pojo.ShelfInfo;

import javax.persistence.*;
import java.util.List;

public class NodePoint {

    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 路径编号
     */
    private String pathNum;

    /**
     * 点位
     */
    private List<String> point;

    /**
     * 该点位货架信息（如果有）
     */
    public List<ShelfInfo> shelfs;

    /**
     * 该点位是否被占用
     * true代表被占用，false代表没有
     */
    private boolean state;

    /**
     * 该点位转载区信息（如果有）
     */
    public ReprintArea area;

    public NodePoint(Integer id,String pathnum,List<String> point, List<ShelfInfo> shelfs, boolean state, ReprintArea area) {
        this.id = id;
        this.pathNum=pathnum;
        this.point = point;
        this.shelfs = shelfs;
        this.state = state;
        this.area = area;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPathNum() {
        return pathNum;
    }

    public void setPathNum(String pathNum) {
        this.pathNum = pathNum;
    }

    public List<String> getPoint() {
        return point;
    }

    public void setPoint(List<String> point) {
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
