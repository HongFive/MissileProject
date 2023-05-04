package edu.szu.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "out_task_recommend_result_info")
public class TaskRecommendResultInfo {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 任务名称
     */
    @Column(name = "t_name")
    private String tName;

    /**
     * 推荐装备列表
     */
    @Column(name = "equipment_recommend")
    private String equipmentRecommend;

    /**
     * 使用装备列表
     */
    @Column(name = "equipment_used")
    private String equipmentUsed;

    /**
     * 推荐货架列表
     */
    @Column(name = "shelf_recommend")
    private String shelfRecommend;

    /**
     * 使用货架列表
     */
    @Column(name = "shelf_used")
    private String shelfUsed;

    /**
     * 推荐agv列表
     */
    @Column(name = "agv_recommend")
    private String agvRecommend;

    /**
     * 使用agv列表
     */
    @Column(name = "agv_used")
    private String agvUsed;

    /**
     * 推荐转载区列表
     */
    @Column(name = "reprint_area_recommend")
    private String reprintAreaRecommend;

    /**
     * 使用转载区列表
     */
    @Column(name = "reprint_area_used")
    private String reprintAreaUsed;

    /**
     * 推荐发射车列表
     */
    @Column(name = "car_recommend")
    private String carRecommend;

    /**
     * 使用发射车列表
     */
    @Column(name = "car_used")
    private String carUsed;

    /**
     * 停车位
     */
    private String park;

    /**
     * 编排好的路径
     */
    private String path;

    /**
     * 入表时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:m:s",timezone="GMT+8")
    @Column(name = "insert_timestamp")
    private Date insertTimestamp;

    public TaskRecommendResultInfo(Integer id, String tName, String shelfRecommend, String shelfUsed, String agvRecommend, String agvUsed, String reprintAreaRecommend, String reprintAreaUsed, String carRecommend, String carUsed, String park, String path, Date insertTimestamp) {
        this.id = id;
        this.tName = tName;
        this.shelfRecommend = shelfRecommend;
        this.shelfUsed = shelfUsed;
        this.agvRecommend = agvRecommend;
        this.agvUsed = agvUsed;
        this.reprintAreaRecommend = reprintAreaRecommend;
        this.reprintAreaUsed = reprintAreaUsed;
        this.carRecommend = carRecommend;
        this.carUsed = carUsed;
        this.park = park;
        this.path = path;
        this.insertTimestamp = insertTimestamp;
    }

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
     * 获取任务名称
     *
     * @return t_name - 任务名称
     */
    public String gettName() {
        return tName;
    }

    /**
     * 设置任务名称
     *
     * @param tName 任务名称
     */
    public void settName(String tName) {
        this.tName = tName;
    }

    /**
     * 获取推荐装备列表
     *
     * @return equipment_recommend - 推荐装备列表
     */
    public String getEquipmentRecommend() {
        return equipmentRecommend;
    }

    /**
     * 设置推荐装备列表
     *
     * @param equipmentRecommend 推荐装备列表
     */
    public void setEquipmentRecommend(String equipmentRecommend) {
        this.equipmentRecommend = equipmentRecommend;
    }

    /**
     * 获取使用装备列表
     *
     * @return equipment_used - 使用装备列表
     */
    public String getEquipmentUsed() {
        return equipmentUsed;
    }

    /**
     * 设置使用装备列表
     *
     * @param equipmentUsed 使用装备列表
     */
    public void setEquipmentUsed(String equipmentUsed) {
        this.equipmentUsed = equipmentUsed;
    }

    /**
     * 获取推荐货架列表
     *
     * @return shelf_recommend - 推荐货架列表
     */
    public String getShelfRecommend() {
        return shelfRecommend;
    }

    /**
     * 设置推荐货架列表
     *
     * @param shelfRecommend 推荐货架列表
     */
    public void setShelfRecommend(String shelfRecommend) {
        this.shelfRecommend = shelfRecommend;
    }

    /**
     * 获取使用货架列表
     *
     * @return shelf_used - 使用货架列表
     */
    public String getShelfUsed() {
        return shelfUsed;
    }

    /**
     * 设置使用货架列表
     *
     * @param shelfUsed 使用货架列表
     */
    public void setShelfUsed(String shelfUsed) {
        this.shelfUsed = shelfUsed;
    }

    /**
     * 获取推荐agv列表
     *
     * @return agv_recommend - 推荐agv列表
     */
    public String getAgvRecommend() {
        return agvRecommend;
    }

    /**
     * 设置推荐agv列表
     *
     * @param agvRecommend 推荐agv列表
     */
    public void setAgvRecommend(String agvRecommend) {
        this.agvRecommend = agvRecommend;
    }

    /**
     * 获取使用agv列表
     *
     * @return agv_used - 使用agv列表
     */
    public String getAgvUsed() {
        return agvUsed;
    }

    /**
     * 设置使用agv列表
     *
     * @param agvUsed 使用agv列表
     */
    public void setAgvUsed(String agvUsed) {
        this.agvUsed = agvUsed;
    }

    /**
     * 获取推荐转载区列表
     *
     * @return reprint_area_recommend - 推荐转载区列表
     */
    public String getReprintAreaRecommend() {
        return reprintAreaRecommend;
    }

    /**
     * 设置推荐转载区列表
     *
     * @param reprintAreaRecommend 推荐转载区列表
     */
    public void setReprintAreaRecommend(String reprintAreaRecommend) {
        this.reprintAreaRecommend = reprintAreaRecommend;
    }

    /**
     * 获取使用转载区列表
     *
     * @return reprint_area_used - 使用转载区列表
     */
    public String getReprintAreaUsed() {
        return reprintAreaUsed;
    }

    /**
     * 设置使用转载区列表
     *
     * @param reprintAreaUsed 使用转载区列表
     */
    public void setReprintAreaUsed(String reprintAreaUsed) {
        this.reprintAreaUsed = reprintAreaUsed;
    }

    /**
     * 获取推荐发射车列表
     *
     * @return car_recommend - 推荐发射车列表
     */
    public String getCarRecommend() {
        return carRecommend;
    }

    /**
     * 设置推荐发射车列表
     *
     * @param carRecommend 推荐发射车列表
     */
    public void setCarRecommend(String carRecommend) {
        this.carRecommend = carRecommend;
    }

    /**
     * 获取使用发射车列表
     *
     * @return car_used - 使用发射车列表
     */
    public String getCarUsed() {
        return carUsed;
    }

    /**
     * 设置使用发射车列表
     *
     * @param carUsed 使用发射车列表
     */
    public void setCarUsed(String carUsed) {
        this.carUsed = carUsed;
    }

    /**
     * 获取停车位
     *
     * @return park - 停车位
     */
    public String getPark() {
        return park;
    }

    /**
     * 设置停车位
     *
     * @param park 停车位
     */
    public void setPark(String park) {
        this.park = park;
    }

    /**
     * 获取编排好的路径
     *
     * @return path - 编排好的路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置编排好的路径
     *
     * @param path 编排好的路径
     */
    public void setPath(String path) {
        this.path = path;
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
}