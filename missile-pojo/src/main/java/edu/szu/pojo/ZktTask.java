package edu.szu.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "zkt_task")
public class ZktTask {
    /**
     * 主键
     */
    @Id
    private Long id;

    /**
     * 任务类型：0-出库，1-入库，2-库内调度，3-保障任务
     */
    private String type;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 任务来源
     */
    @Column(name = "task_source")
    private String taskSource;

    /**
     * 任务说明
     */
    @Column(name = "task_explain")
    private String taskExplain;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 完成时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 装备型号
     */
    @Column(name = "eq_version")
    private String eqVersion;

    /**
     * 装备数量
     */
    private String nums;

    /**
     * 状态：1-待编排，2-进行中，3-完成，4-已取消
     */
    private String status;

    /**
     * 是否加急：0-否，1-是
     */
    private String urgent;

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
     * 获取任务类型：0-出库，1-入库，2-库内调度，3-保障任务
     *
     * @return type - 任务类型：0-出库，1-入库，2-库内调度，3-保障任务
     */
    public String getType() {
        return type;
    }

    /**
     * 设置任务类型：0-出库，1-入库，2-库内调度，3-保障任务
     *
     * @param type 任务类型：0-出库，1-入库，2-库内调度，3-保障任务
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取任务名称
     *
     * @return task_name - 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务名称
     *
     * @param taskName 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取任务来源
     *
     * @return task_source - 任务来源
     */
    public String getTaskSource() {
        return taskSource;
    }

    /**
     * 设置任务来源
     *
     * @param taskSource 任务来源
     */
    public void setTaskSource(String taskSource) {
        this.taskSource = taskSource;
    }

    /**
     * 获取任务说明
     *
     * @return task_explain - 任务说明
     */
    public String getTaskExplain() {
        return taskExplain;
    }

    /**
     * 设置任务说明
     *
     * @param taskExplain 任务说明
     */
    public void setTaskExplain(String taskExplain) {
        this.taskExplain = taskExplain;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取完成时间
     *
     * @return end_time - 完成时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置完成时间
     *
     * @param endTime 完成时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取装备型号
     *
     * @return eq_version - 装备型号
     */
    public String getEqVersion() {
        return eqVersion;
    }

    /**
     * 设置装备型号
     *
     * @param eqVersion 装备型号
     */
    public void setEqVersion(String eqVersion) {
        this.eqVersion = eqVersion;
    }

    /**
     * 获取装备数量
     *
     * @return nums - 装备数量
     */
    public String getNums() {
        return nums;
    }

    /**
     * 设置装备数量
     *
     * @param nums 装备数量
     */
    public void setNums(String nums) {
        this.nums = nums;
    }

    /**
     * 获取状态：1-待编排，2-进行中，3-完成，4-已取消
     *
     * @return status - 状态：1-待编排，2-进行中，3-完成，4-已取消
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态：1-待编排，2-进行中，3-完成，4-已取消
     *
     * @param status 状态：1-待编排，2-进行中，3-完成，4-已取消
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取是否加急：0-否，1-是
     *
     * @return urgent - 是否加急：0-否，1-是
     */
    public String getUrgent() {
        return urgent;
    }

    /**
     * 设置是否加急：0-否，1-是
     *
     * @param urgent 是否加急：0-否，1-是
     */
    public void setUrgent(String urgent) {
        this.urgent = urgent;
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