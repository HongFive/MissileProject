package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "zkt_warehouse_shelf")
public class ZktWarehouseShelf {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 库位编号
     */
    @Column(name = "warehouse_num")
    private String warehouseNum;

    /**
     * 区域
     */
    private String area;

    /**
     * 货架编号
     */
    @Column(name = "shelf_num")
    private String shelfNum;

    /**
     * 层号:1层，2层，3层
     */
    @Column(name = "layer_num")
    private String layerNum;

    /**
     * 位置：0-内，1-外
     */
    private String position;

    /**
     * 状态 ：0-未使用；1-使用
     */
    private String status;

    /**
     * 健康状态：0-故障，1-健康
     */
    @Column(name = "health_status")
    private String healthStatus;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取库位编号
     *
     * @return warehouse_num - 库位编号
     */
    public String getWarehouseNum() {
        return warehouseNum;
    }

    /**
     * 设置库位编号
     *
     * @param warehouseNum 库位编号
     */
    public void setWarehouseNum(String warehouseNum) {
        this.warehouseNum = warehouseNum;
    }

    /**
     * 获取区域
     *
     * @return area - 区域
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区域
     *
     * @param area 区域
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取货架编号
     *
     * @return shelf_num - 货架编号
     */
    public String getShelfNum() {
        return shelfNum;
    }

    /**
     * 设置货架编号
     *
     * @param shelfNum 货架编号
     */
    public void setShelfNum(String shelfNum) {
        this.shelfNum = shelfNum;
    }

    /**
     * 获取层号:1层，2层，3层
     *
     * @return layer_num - 层号:1层，2层，3层
     */
    public String getLayerNum() {
        return layerNum;
    }

    /**
     * 设置层号:1层，2层，3层
     *
     * @param layerNum 层号:1层，2层，3层
     */
    public void setLayerNum(String layerNum) {
        this.layerNum = layerNum;
    }

    /**
     * 获取位置：0-内，1-外
     *
     * @return position - 位置：0-内，1-外
     */
    public String getPosition() {
        return position;
    }

    /**
     * 设置位置：0-内，1-外
     *
     * @param position 位置：0-内，1-外
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * 获取状态 ：0-未使用；1-使用
     *
     * @return status - 状态 ：0-未使用；1-使用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态 ：0-未使用；1-使用
     *
     * @param status 状态 ：0-未使用；1-使用
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取健康状态：0-故障，1-健康
     *
     * @return health_status - 健康状态：0-故障，1-健康
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * 设置健康状态：0-故障，1-健康
     *
     * @param healthStatus 健康状态：0-故障，1-健康
     */
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    /**
     * 获取创建者
     *
     * @return create_by - 创建者
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建者
     *
     * @param createBy 创建者
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新者
     *
     * @return update_by - 更新者
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新者
     *
     * @param updateBy 更新者
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}