package edu.szu.controller;

import edu.szu.AgvService;
import edu.szu.ShelfService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/planRecommend")
public class PlanRecommendController {

    @Autowired
    private ShelfService shelfService;

    @Autowired
    private AgvService agvService;

    /**
     * 任务推荐
     * @return
     */
    @GetMapping("/test1")
    public JSONResult resourceRecommend(){
        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
        List<AgvInfo> agvInfoList = agvService.queryAgvList();

        //测试案例1，货架满仓，小车上下各6辆均匀分布
        for (int i = 0; i < shelfInfoList.size(); i++) {
            if(!shelfInfoList.get(i).getState()){
                shelfInfoList.get(i).setState(true);
            }
        }
        AgvInfo agv1 = agvInfoList.get(0);
        agv1.setLocation("21-1");
        AgvInfo agv2 = agvInfoList.get(1);
        agv2.setLocation("23-1");
        AgvInfo agv3 = agvInfoList.get(2);
        agv3.setLocation("21-2");
        AgvInfo agv4 = agvInfoList.get(3);
        agv4.setLocation("23-2");
        AgvInfo agv5 = agvInfoList.get(4);
        agv5.setLocation("21-3");
        AgvInfo agv6 = agvInfoList.get(5);
        agv6.setLocation("23-3");
        AgvInfo agv7 = agvInfoList.get(6);
        agv7.setLocation("21-4");
        AgvInfo agv8 = agvInfoList.get(7);
        agv8.setLocation("23-4");
        AgvInfo agv9 = agvInfoList.get(8);
        agv9.setLocation("21-5");
        AgvInfo agv10 = agvInfoList.get(9);
        agv10.setLocation("23-5");
        AgvInfo agv11 = agvInfoList.get(10);
        agv11.setLocation("21-6");
        AgvInfo agv12 = agvInfoList.get(11);
        agv12.setLocation("23-6");


        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);
        graph.printGraph();
        return JSONResult.ok(graph.arrayV);
    }
}
