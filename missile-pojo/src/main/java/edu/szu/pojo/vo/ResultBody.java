package edu.szu.pojo.vo;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;

import java.util.List;

public class ResultBody {

    private AgvInfo agvInfo;

    private NodePoint node;

    private List<NodePoint> path;

    private ShelfInfo shelf;

    public ResultBody(AgvInfo agvInfo, NodePoint node,ShelfInfo shelf) {
        this.agvInfo = agvInfo;
        this.node = node;
        this.shelf=shelf;
    }

    public ResultBody(AgvInfo agvInfo, List<NodePoint> path) {
        this.agvInfo = agvInfo;
        this.path = path;
    }

    public ResultBody(AgvInfo agvInfo, NodePoint node, List<NodePoint> path) {
        this.agvInfo = agvInfo;
        this.node = node;
        this.path = path;
    }

    public ShelfInfo getShelf() {
        return shelf;
    }

    public void setShelf(ShelfInfo shelf) {
        this.shelf = shelf;
    }

    public AgvInfo getAgvInfo() {
        return agvInfo;
    }

    public void setAgvInfo(AgvInfo agvInfo) {
        this.agvInfo = agvInfo;
    }

    public NodePoint getNode() {
        return node;
    }

    public void setNode(NodePoint node) {
        this.node = node;
    }

    public List<NodePoint> getPath() {
        return path;
    }

    public void setPath(List<NodePoint> path) {
        this.path = path;
    }
}
