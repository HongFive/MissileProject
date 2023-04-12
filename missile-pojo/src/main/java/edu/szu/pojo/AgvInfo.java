package edu.szu.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "agv_info")
public class AgvInfo {
    @Id
    private Integer id;

    /**
     * agv name
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
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取agv name
     *
     * @return agv_name - agv name
     */
    public String getAgvName() {
        return agvName;
    }

    /**
     * 设置agv name
     *
     * @param agvName agv name
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