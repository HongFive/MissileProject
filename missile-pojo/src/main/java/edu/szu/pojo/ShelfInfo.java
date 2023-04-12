package edu.szu.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "shelf_info")
public class ShelfInfo {
    /**
     * 主键自增
     */
    @Id
    private Integer id;

    /**
     * 库位编号
     */
    @Column(name = "shelf_name")
    private String shelfName;

    /**
     * 层号，1、2、3
     */
    private Integer layer;

    /**
     * 内、外
     */
    private String position;

    /**
     * 不带D、带D
     */
    private Boolean state;

    /**
     * 健康、一般、故障
     */
    @Column(name = "health_degree")
    private String healthDegree;

    /**
     * 是否空置
     */
    private Boolean free;

    /**
     * 货架位置
     */
    private String location;

    /**
     * 入表时间
     */
    @Column(name = "insert_timestamp")
    private Date insertTimestamp;

    /**
     * 装备id
     */
    @Column(name = "equipment_id")
    private String equipmentId;

    /**
     * 获取主键自增
     *
     * @return id - 主键自增
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键自增
     *
     * @param id 主键自增
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取库位编号
     *
     * @return shelf_name - 库位编号
     */
    public String getShelfName() {
        return shelfName;
    }

    /**
     * 设置库位编号
     *
     * @param shelfName 库位编号
     */
    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    /**
     * 获取层号，1、2、3
     *
     * @return layer - 层号，1、2、3
     */
    public Integer getLayer() {
        return layer;
    }

    /**
     * 设置层号，1、2、3
     *
     * @param layer 层号，1、2、3
     */
    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    /**
     * 获取内、外
     *
     * @return position - 内、外
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置内、外
     *
     * @param position 内、外
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 获取不带D、带D
     *
     * @return state - 不带D、带D
     */
    public Boolean getState() {
        return state;
    }

    /**
     * 设置不带D、带D
     *
     * @param state 不带D、带D
     */
    public void setState(Boolean state) {
        this.state = state;
    }

    /**
     * 获取健康、一般、故障
     *
     * @return health_degree - 健康、一般、故障
     */
    public String getHealthDegree() {
        return healthDegree;
    }

    /**
     * 设置健康、一般、故障
     *
     * @param healthDegree 健康、一般、故障
     */
    public void setHealthDegree(String healthDegree) {
        this.healthDegree = healthDegree;
    }

    /**
     * 获取是否空置
     *
     * @return free - 是否空置
     */
    public Boolean getFree() {
        return free;
    }

    /**
     * 设置是否空置
     *
     * @param free 是否空置
     */
    public void setFree(Boolean free) {
        this.free = free;
    }

    /**
     * 获取货架位置
     *
     * @return location - 货架位置
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置货架位置
     *
     * @param location 货架位置
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取入表时间
     *
     * @return insert_timestamp - 入表时间
     */
    public Date getInsertTimestamp() {
        return insertTimestamp;
    }

    /**
     * 设置入表时间
     *
     * @param insertTimestamp 入表时间
     */
    public void setInsertTimestamp(Date insertTimestamp) {
        this.insertTimestamp = insertTimestamp;
    }

    /**
     * 获取装备id
     *
     * @return equipment_id - 装备id
     */
    public String getEquipmentId() {
        return equipmentId;
    }

    /**
     * 设置装备id
     *
     * @param equipmentId 装备id
     */
    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }
}