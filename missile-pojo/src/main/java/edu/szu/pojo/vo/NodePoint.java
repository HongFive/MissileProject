package edu.szu.pojo.vo;

import edu.szu.pojo.OutShelfInfo;
import edu.szu.pojo.ShelfInfo;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;

public class NodePoint {

    /**
     * ID
     */

    private Integer id;

    /**
     * 路径编号
     */
    private String pathNum;

    /**
     * 点位列表
     */
    private LinkedHashMap<String,Double> pointList;


    /**
     * 该点位货架信息（如果有）
     */
    public List<OutShelfInfo> shelfs;

    /**
     * 该点位是否被占用
     * true代表被占用，false代表没有
     */
    private Integer state;

    /**
     * 该点位转载区信息（如果有）
     */
    public ReprintArea area;

    public NodePoint(Integer id, String pathnum, LinkedHashMap<String,Double> pointlist, List<OutShelfInfo> shelfs, Integer state, ReprintArea area) {
        this.id = id;
        this.pathNum=pathnum;
        this.pointList = pointlist;
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

    public Integer getId() {
        return id;
    }

    public LinkedHashMap<String, Double> getPointList() {
        return pointList;
    }

    public void setPointList(LinkedHashMap<String, Double> pointList) {
        this.pointList = pointList;
    }

    public Integer getState() {
        return state;
    }

    public List<OutShelfInfo> getShelfs() {
        return shelfs;
    }

    public void setShelfs(List<OutShelfInfo> shelfs) {
        this.shelfs = shelfs;
    }

    public Integer isState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public ReprintArea getArea() {
        return area;
    }

    public void setArea(ReprintArea area) {
        this.area = area;
    }
}
