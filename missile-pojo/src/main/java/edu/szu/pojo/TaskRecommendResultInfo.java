package edu.szu.pojo;

import javax.persistence.*;
import java.util.Date;

@Table(name = "task_recommend_result_info")
public class TaskRecommendResultInfo {
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

    private String park;

    /**
     * 编排好的路径
     */
    private String path;

    /**
     * 入表时间
     */
    @Column(name = "insert_timestamp")
    private Date insertTimestamp;

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
     * @return park
     */
    public String getPark() {
        return park;
    }

    /**
     * @param park
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