package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "out_agv_info")
public class OutAgvInfo {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * agv名称
     */
    @Column(name = "agv_name")
    private String agvName;

    /**
     * 单、双层
     */
    private String type;

    /**
     * 电量
     */
    private Float power;

    /**
     * 位置
     */
    private String location;

    /**
     * 释放时间
     */
    @Column(name = "release_timestamp")
    private Date releaseTimestamp;

    /**
     * 占用时间
     */
    @Column(name = "employ_timestamp")
    private Date employTimestamp;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取agv名称
     *
     * @return agv_name - agv名称
     */
    public String getAgvName() {
        return agvName;
    }

    /**
     * 设置agv名称
     *
     * @param agvName agv名称
     */
    public void setAgvName(String agvName) {
        this.agvName = agvName;
    }

    /**
     * 获取单、双层
     *
     * @return type - 单、双层
     */
    public String getType() {
        return type;
    }

    /**
     * 设置单、双层
     *
     * @param type 单、双层
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取电量
     *
     * @return power - 电量
     */
    public Float getPower() {
        return power;
    }

    /**
     * 设置电量
     *
     * @param power 电量
     */
    public void setPower(Float power) {
        this.power = power;
    }

    /**
     * 获取位置
     *
     * @return location - 位置
     */
    public String getLocation() {
        return location;
    }

    /**
     * 设置位置
     *
     * @param location 位置
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * 获取释放时间
     *
     * @return release_timestamp - 释放时间
     */
    public Date getReleaseTimestamp() {
        return releaseTimestamp;
    }

    /**
     * 设置释放时间
     *
     * @param releaseTimestamp 释放时间
     */
    public void setReleaseTimestamp(Date releaseTimestamp) {
        this.releaseTimestamp = releaseTimestamp;
    }

    /**
     * 获取占用时间
     *
     * @return employ_timestamp - 占用时间
     */
    public Date getEmployTimestamp() {
        return employTimestamp;
    }

    /**
     * 设置占用时间
     *
     * @param employTimestamp 占用时间
     */
    public void setEmployTimestamp(Date employTimestamp) {
        this.employTimestamp = employTimestamp;
    }
}