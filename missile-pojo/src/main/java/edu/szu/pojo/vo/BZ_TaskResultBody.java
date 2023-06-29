package edu.szu.pojo.vo;

/*
 * @author 胡国兴
 * @version 1.0
 */
public class BZ_TaskResultBody {
    private Long userId;
    private Long shelfRecommend_id;
    private Long agvRecommend_id;
    private String shelfName;
    private String agvName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShelfRecommend_id() {
        return shelfRecommend_id;
    }

    public void setShelfRecommend_id(Long shelfRecommend_id) {
        this.shelfRecommend_id = shelfRecommend_id;
    }

    public Long getAgvRecommend_id() {
        return agvRecommend_id;
    }

    public void setAgvRecommend_id(Long agvRecommend_id) {
        this.agvRecommend_id = agvRecommend_id;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getAgvName() {
        return agvName;
    }

    public void setAgvName(String agvName) {
        this.agvName = agvName;
    }
}
