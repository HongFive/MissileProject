package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "zkt_task_flow")
public class ZktTaskFlow {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 仓库id
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 装备id
     */
    @Column(name = "eq_id")
    private Long eqId;

    /**
     * 装备编号
     */
    @Column(name = "eq_num")
    private String eqNum;

    /**
     * 装载区域
     */
    @Column(name = "reprint_area")
    private String reprintArea;

    /**
     * agv设备id
     */
    @Column(name = "agv_id")
    private Long agvId;

    /**
     * ohc天车设备id
     */
    @Column(name = "ohc_id")
    private Long ohcId;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 发射车设备id
     */
    @Column(name = "rl_id")
    private Long rlId;

    /**
     * 状态:1-待处理，2-进行中，3-已完成，4-已取消
     */
    private String status;

    /**
     * 子任务开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 子任务结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 结果百分比
     */
    @Column(name = "result_percent")
    private String resultPercent;

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
     * 获取任务id
     *
     * @return task_id - 任务id
     */
    public Long getTaskId() {
        return taskId;
    }

    /**
     * 设置任务id
     *
     * @param taskId 任务id
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取仓库id
     *
     * @return warehouse_id - 仓库id
     */
    public Long getWarehouseId() {
        return warehouseId;
    }

    /**
     * 设置仓库id
     *
     * @param warehouseId 仓库id
     */
    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * 获取装备id
     *
     * @return eq_id - 装备id
     */
    public Long getEqId() {
        return eqId;
    }

    /**
     * 设置装备id
     *
     * @param eqId 装备id
     */
    public void setEqId(Long eqId) {
        this.eqId = eqId;
    }

    /**
     * 获取装备编号
     *
     * @return eq_num - 装备编号
     */
    public String getEqNum() {
        return eqNum;
    }

    /**
     * 设置装备编号
     *
     * @param eqNum 装备编号
     */
    public void setEqNum(String eqNum) {
        this.eqNum = eqNum;
    }

    /**
     * 获取装载区域
     *
     * @return reprint_area - 装载区域
     */
    public String getReprintArea() {
        return reprintArea;
    }

    /**
     * 设置装载区域
     *
     * @param reprintArea 装载区域
     */
    public void setReprintArea(String reprintArea) {
        this.reprintArea = reprintArea;
    }

    /**
     * 获取agv设备id
     *
     * @return agv_id - agv设备id
     */
    public Long getAgvId() {
        return agvId;
    }

    /**
     * 设置agv设备id
     *
     * @param agvId agv设备id
     */
    public void setAgvId(Long agvId) {
        this.agvId = agvId;
    }

    /**
     * 获取ohc天车设备id
     *
     * @return ohc_id - ohc天车设备id
     */
    public Long getOhcId() {
        return ohcId;
    }

    /**
     * 设置ohc天车设备id
     *
     * @param ohcId ohc天车设备id
     */
    public void setOhcId(Long ohcId) {
        this.ohcId = ohcId;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取发射车设备id
     *
     * @return rl_id - 发射车设备id
     */
    public Long getRlId() {
        return rlId;
    }

    /**
     * 设置发射车设备id
     *
     * @param rlId 发射车设备id
     */
    public void setRlId(Long rlId) {
        this.rlId = rlId;
    }

    /**
     * 获取状态:1-待处理，2-进行中，3-已完成，4-已取消
     *
     * @return status - 状态:1-待处理，2-进行中，3-已完成，4-已取消
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态:1-待处理，2-进行中，3-已完成，4-已取消
     *
     * @param status 状态:1-待处理，2-进行中，3-已完成，4-已取消
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取子任务开始时间
     *
     * @return start_time - 子任务开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置子任务开始时间
     *
     * @param startTime 子任务开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取子任务结束时间
     *
     * @return end_time - 子任务结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置子任务结束时间
     *
     * @param endTime 子任务结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取结果百分比
     *
     * @return result_percent - 结果百分比
     */
    public String getResultPercent() {
        return resultPercent;
    }

    /**
     * 设置结果百分比
     *
     * @param resultPercent 结果百分比
     */
    public void setResultPercent(String resultPercent) {
        this.resultPercent = resultPercent;
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