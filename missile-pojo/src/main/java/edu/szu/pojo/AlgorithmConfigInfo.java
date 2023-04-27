package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "out_algorithm_config_info")
public class AlgorithmConfigInfo {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * agv直行额定速度, m/min
     */
    @Column(name = "agv_rated_speed")
    private Float agvRatedSpeed;

    /**
     * agv转弯速度, m/min
     */
    @Column(name = "agv_turn_speed")
    private Float agvTurnSpeed;

    /**
     * agv装载耗时，min
     */
    @Column(name = "agv_load_time")
    private Float agvLoadTime;

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
     * 获取agv直行额定速度, m/min
     *
     * @return agv_rated_speed - agv直行额定速度, m/min
     */
    public Float getAgvRatedSpeed() {
        return agvRatedSpeed;
    }

    /**
     * 设置agv直行额定速度, m/min
     *
     * @param agvRatedSpeed agv直行额定速度, m/min
     */
    public void setAgvRatedSpeed(Float agvRatedSpeed) {
        this.agvRatedSpeed = agvRatedSpeed;
    }

    /**
     * 获取agv转弯速度, m/min
     *
     * @return agv_turn_speed - agv转弯速度, m/min
     */
    public Float getAgvTurnSpeed() {
        return agvTurnSpeed;
    }

    /**
     * 设置agv转弯速度, m/min
     *
     * @param agvTurnSpeed agv转弯速度, m/min
     */
    public void setAgvTurnSpeed(Float agvTurnSpeed) {
        this.agvTurnSpeed = agvTurnSpeed;
    }

    /**
     * 获取agv装载耗时，min
     *
     * @return agv_load_time - agv装载耗时，min
     */
    public Float getAgvLoadTime() {
        return agvLoadTime;
    }

    /**
     * 设置agv装载耗时，min
     *
     * @param agvLoadTime agv装载耗时，min
     */
    public void setAgvLoadTime(Float agvLoadTime) {
        this.agvLoadTime = agvLoadTime;
    }
}