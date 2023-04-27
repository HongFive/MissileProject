package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "zkt_device")
public class ZktDevice {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 设备编码
     */
    @Column(name = "device_num")
    private String deviceNum;

    /**
     * 设备ip
     */
    @Column(name = "device_ip")
    private String deviceIp;

    /**
     * 设备类型：AGV-agv车，ME-监控设备，UWB-定位手环，OHC-天车，EXR-交换机，RL-发射车，RS-温湿度传感器
     */
    @Column(name = "device_type")
    private String deviceType;

    /**
     * 设备型号
     */
    @Column(name = "device_version")
    private String deviceVersion;

    /**
     * 状态：1-空闲，2-任务中，3-故障，4-维修，5-充电中
     */
    private String status;

    /**
     * 故障码
     */
    @Column(name = "fault_code")
    private String faultCode;

    /**
     * 故障时间
     */
    @Column(name = "fault_time")
    private Date faultTime;

    /**
     * 维修时间
     */
    @Column(name = "repair_time")
    private Date repairTime;

    /**
     * 是否网络设备:0-否，1-是
     */
    @Column(name = "whether_network_device")
    private String whetherNetworkDevice;

    /**
     * 设备参数
     */
    @Column(name = "device_params")
    private String deviceParams;

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
     * 获取设备编码
     *
     * @return device_num - 设备编码
     */
    public String getDeviceNum() {
        return deviceNum;
    }

    /**
     * 设置设备编码
     *
     * @param deviceNum 设备编码
     */
    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    /**
     * 获取设备ip
     *
     * @return device_ip - 设备ip
     */
    public String getDeviceIp() {
        return deviceIp;
    }

    /**
     * 设置设备ip
     *
     * @param deviceIp 设备ip
     */
    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    /**
     * 获取设备类型：AGV-agv车，ME-监控设备，UWB-定位手环，OHC-天车，EXR-交换机，RL-发射车，RS-温湿度传感器
     *
     * @return device_type - 设备类型：AGV-agv车，ME-监控设备，UWB-定位手环，OHC-天车，EXR-交换机，RL-发射车，RS-温湿度传感器
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 设置设备类型：AGV-agv车，ME-监控设备，UWB-定位手环，OHC-天车，EXR-交换机，RL-发射车，RS-温湿度传感器
     *
     * @param deviceType 设备类型：AGV-agv车，ME-监控设备，UWB-定位手环，OHC-天车，EXR-交换机，RL-发射车，RS-温湿度传感器
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 获取设备型号
     *
     * @return device_version - 设备型号
     */
    public String getDeviceVersion() {
        return deviceVersion;
    }

    /**
     * 设置设备型号
     *
     * @param deviceVersion 设备型号
     */
    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    /**
     * 获取状态：1-空闲，2-任务中，3-故障，4-维修，5-充电中
     *
     * @return status - 状态：1-空闲，2-任务中，3-故障，4-维修，5-充电中
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：1-空闲，2-任务中，3-故障，4-维修，5-充电中
     *
     * @param status 状态：1-空闲，2-任务中，3-故障，4-维修，5-充电中
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取故障码
     *
     * @return fault_code - 故障码
     */
    public String getFaultCode() {
        return faultCode;
    }

    /**
     * 设置故障码
     *
     * @param faultCode 故障码
     */
    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    /**
     * 获取故障时间
     *
     * @return fault_time - 故障时间
     */
    public Date getFaultTime() {
        return faultTime;
    }

    /**
     * 设置故障时间
     *
     * @param faultTime 故障时间
     */
    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
    }

    /**
     * 获取维修时间
     *
     * @return repair_time - 维修时间
     */
    public Date getRepairTime() {
        return repairTime;
    }

    /**
     * 设置维修时间
     *
     * @param repairTime 维修时间
     */
    public void setRepairTime(Date repairTime) {
        this.repairTime = repairTime;
    }

    /**
     * 获取是否网络设备:0-否，1-是
     *
     * @return whether_network_device - 是否网络设备:0-否，1-是
     */
    public String getWhetherNetworkDevice() {
        return whetherNetworkDevice;
    }

    /**
     * 设置是否网络设备:0-否，1-是
     *
     * @param whetherNetworkDevice 是否网络设备:0-否，1-是
     */
    public void setWhetherNetworkDevice(String whetherNetworkDevice) {
        this.whetherNetworkDevice = whetherNetworkDevice;
    }

    /**
     * 获取设备参数
     *
     * @return device_params - 设备参数
     */
    public String getDeviceParams() {
        return deviceParams;
    }

    /**
     * 设置设备参数
     *
     * @param deviceParams 设备参数
     */
    public void setDeviceParams(String deviceParams) {
        this.deviceParams = deviceParams;
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