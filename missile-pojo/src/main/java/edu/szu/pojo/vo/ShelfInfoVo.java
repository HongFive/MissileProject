package edu.szu.pojo.vo;

import edu.szu.pojo.ZktWarehouseShelf;

import javax.persistence.Id;

public class ShelfInfoVo{

    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 单、双层
     */
    private int layer;

    /**
     * 位置
     */
    private String shelfName;

    /**
     * 内、外
     */
//    private String position;

    /**
     * 不带D、带D
     */
    private Boolean state;

    private ZktWarehouseShelf zktWarehouseShelf;

    public ShelfInfoVo(ZktWarehouseShelf zktWarehouseShelf) {
        this.layer = Integer.parseInt(zktWarehouseShelf.getLayerNum());
        this.shelfName = this.layer + "-" + Integer.parseInt(zktWarehouseShelf.getShelfNum());
        if(zktWarehouseShelf.getStatus() == "0"){
            this.state = false;
        }else{
            this.state = true;
        }
        this.zktWarehouseShelf = zktWarehouseShelf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public ZktWarehouseShelf getZktWarehouseShelf() {
        return zktWarehouseShelf;
    }

    public void setZktWarehouseShelf(ZktWarehouseShelf zktWarehouseShelf) {
        this.zktWarehouseShelf = zktWarehouseShelf;
    }
}
