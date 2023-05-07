package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;

import java.util.ArrayList;
import java.util.List;

/*
 * @author 胡国兴
 * @version 1.0
 */
public class ThreadNode{
    AgvInfo agv;
    List<NodePoint> path;
    NodePoint target;

    ShelfInfo shelf;
    Integer state;

    List<String> newPath;
    Boolean isStart;

    public ThreadNode(AgvInfo agv, List<NodePoint> path, NodePoint target,ShelfInfo shelf,int state,boolean isStart) {
        this.agv = agv;
        this.path = path;
        this.target = target;
        this.shelf=shelf;
        this.state=state;
        this.isStart=isStart;
        this.newPath=new ArrayList<>();
    }

    public Boolean getStart() {
        return isStart;
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public AgvInfo getAgv() {
        return agv;
    }

    public void setAgv(AgvInfo agv) {
        this.agv = agv;
    }

    public List<NodePoint> getPath() {
        return path;
    }

    public void setPath(List<NodePoint> path) {
        this.path = path;
    }

    public NodePoint getTarget() {
        return target;
    }

    public void setTarget(NodePoint target) {
        this.target = target;
    }

    public ShelfInfo getShelf() {
        return shelf;
    }

    public void setShelf(ShelfInfo shelf) {
        this.shelf = shelf;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}