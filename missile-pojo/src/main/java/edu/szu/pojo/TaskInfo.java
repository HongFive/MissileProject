package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "out_task_info")
public class TaskInfo {
    /**
     * 主键
     */
    @Id
    private Integer id;

    /**
     * 任务名称
     */
    @Id
    @Column(name = "t_name")
    private String tName;

    /**
     * 任务类型，出库、入库
     */
    @Column(name = "t_type")
    private String tType;

    /**
     * 装备型号
     */
    @Column(name = "t_model")
    private String tModel;

    /**
     * 装备数量
     */
    @Column(name = "t_num")
    private Integer tNum;

    /**
     * 装载区位置
     */
    @Column(name = "t_reprintarea")
    private String tReprintarea;

    /**
     * 人员
     */
    @Column(name = "t_person")
    private String tPerson;

    /**
     * 目的地
     */
    @Column(name = "t_add")
    private String tAdd;

    /**
     * 开始时间
     */
    @Column(name = "t_starttime")
    private Date tStarttime;

    /**
     * 结束时间
     */
    @Column(name = "t_endtime")
    private Date tEndtime;

    /**
     * 任务来源
     */
    @Column(name = "t_source")
    private String tSource;

    /**
     * 是否紧急
     */
    private Byte urgent;

    /**
     * 创建时间
     */
    @Column(name = "t_inserttime")
    private Date tInserttime;

    /**
     * 介绍
     */
    @Column(name = "t_info")
    private String tInfo;

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
     * 获取任务类型，出库、入库
     *
     * @return t_type - 任务类型，出库、入库
     */
    public String gettType() {
        return tType;
    }

    /**
     * 设置任务类型，出库、入库
     *
     * @param tType 任务类型，出库、入库
     */
    public void settType(String tType) {
        this.tType = tType;
    }

    /**
     * 获取装备型号
     *
     * @return t_model - 装备型号
     */
    public String gettModel() {
        return tModel;
    }

    /**
     * 设置装备型号
     *
     * @param tModel 装备型号
     */
    public void settModel(String tModel) {
        this.tModel = tModel;
    }

    /**
     * 获取装备数量
     *
     * @return t_num - 装备数量
     */
    public Integer gettNum() {
        return tNum;
    }

    /**
     * 设置装备数量
     *
     * @param tNum 装备数量
     */
    public void settNum(Integer tNum) {
        this.tNum = tNum;
    }

    /**
     * 获取装载区位置
     *
     * @return t_reprintarea - 装载区位置
     */
    public String gettReprintarea() {
        return tReprintarea;
    }

    /**
     * 设置装载区位置
     *
     * @param tReprintarea 装载区位置
     */
    public void settReprintarea(String tReprintarea) {
        this.tReprintarea = tReprintarea;
    }

    /**
     * 获取人员
     *
     * @return t_person - 人员
     */
    public String gettPerson() {
        return tPerson;
    }

    /**
     * 设置人员
     *
     * @param tPerson 人员
     */
    public void settPerson(String tPerson) {
        this.tPerson = tPerson;
    }

    /**
     * 获取目的地
     *
     * @return t_add - 目的地
     */
    public String gettAdd() {
        return tAdd;
    }

    /**
     * 设置目的地
     *
     * @param tAdd 目的地
     */
    public void settAdd(String tAdd) {
        this.tAdd = tAdd;
    }

    /**
     * 获取开始时间
     *
     * @return t_starttime - 开始时间
     */
    public Date gettStarttime() {
        return tStarttime;
    }

    /**
     * 设置开始时间
     *
     * @param tStarttime 开始时间
     */
    public void settStarttime(Date tStarttime) {
        this.tStarttime = tStarttime;
    }

    /**
     * 获取结束时间
     *
     * @return t_endtime - 结束时间
     */
    public Date gettEndtime() {
        return tEndtime;
    }

    /**
     * 设置结束时间
     *
     * @param tEndtime 结束时间
     */
    public void settEndtime(Date tEndtime) {
        this.tEndtime = tEndtime;
    }

    /**
     * 获取任务来源
     *
     * @return t_source - 任务来源
     */
    public String gettSource() {
        return tSource;
    }

    /**
     * 设置任务来源
     *
     * @param tSource 任务来源
     */
    public void settSource(String tSource) {
        this.tSource = tSource;
    }

    /**
     * 获取是否紧急
     *
     * @return urgent - 是否紧急
     */
    public Byte getUrgent() {
        return urgent;
    }

    /**
     * 设置是否紧急
     *
     * @param urgent 是否紧急
     */
    public void setUrgent(Byte urgent) {
        this.urgent = urgent;
    }

    /**
     * 获取创建时间
     *
     * @return t_inserttime - 创建时间
     */
    public Date gettInserttime() {
        return tInserttime;
    }

    /**
     * 设置创建时间
     *
     * @param tInserttime 创建时间
     */
    public void settInserttime(Date tInserttime) {
        this.tInserttime = tInserttime;
    }

    /**
     * 获取介绍
     *
     * @return t_info - 介绍
     */
    public String gettInfo() {
        return tInfo;
    }

    /**
     * 设置介绍
     *
     * @param tInfo 介绍
     */
    public void settInfo(String tInfo) {
        this.tInfo = tInfo;
    }
}