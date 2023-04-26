package edu.szu.controller;

import edu.szu.AgvService;
import edu.szu.ShelfService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private ShelfService shelfService;

    @Autowired
    private AgvService agvService;

    //上装载区从左边道运行路径长度
    double upLeftPathWeight = 1122;

    //下装载区从左边道路运行路径长度
    double downLeftPathWeight = 1005;

    @GetMapping("/task")
    public JSONResult Hello(){
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();
        List<AgvInfo> agvInfoList=agvService.queryAgvList();
        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);
        graph.printGraph();
        return JSONResult.ok(graph.arrayV);
    }

    @GetMapping("/pathtest")
    public JSONResult agvPathTest(){
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();
        List<AgvInfo> agvInfoList=agvService.queryAgvList();
        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);


        int up_area=0;
        int down_area=115;

        LinkedHashMap<AgvInfo,NodePoint> up_run=new LinkedHashMap<>();
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(0),graph.arrayV[31]);
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(1),graph.arrayV[6]);
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(2),graph.arrayV[5]);
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(3),graph.arrayV[92]);
        LinkedHashMap<AgvInfo, List<NodePoint>> up_paths= graph.findShortPaths(graph.Matrix, up_run,up_area);//上转载区出发小车路径

        LinkedHashMap<AgvInfo,NodePoint> down_run=new LinkedHashMap<>();
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(0),graph.arrayV[113]);
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(1),graph.arrayV[107]);
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(2),graph.arrayV[79]);
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(3),graph.arrayV[82]);
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(4),graph.arrayV[26]);
        down_run.put(graph.arrayV[down_area].getArea().agvInfoList.get(5),graph.arrayV[52]);
        LinkedHashMap<AgvInfo, List<NodePoint>> down_paths= graph.findShortPaths(graph.Matrix, down_run,down_area);

//        AgvThreadPool agvThreadPool=new AgvThreadPool();
//        agvThreadPool.LetsGo(graph,up_run,up_paths,up_area);
//        agvThreadPool.RuningAgvs(graph,up_run,up_paths,down_run,down_paths);
//        agvThreadPool.LetsGo(graph,up_run,up_paths,up_area);
        return JSONResult.ok(graph.arrayV);
    }

    @GetMapping("/path")
    public JSONResult agvPath(){
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();
        List<AgvInfo> agvInfoList=agvService.queryAgvList();
        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);

        //上下转载区
        int up_area=0;
        int down_area=115;

        PlanRecommendController222 recommend=new PlanRecommendController222();
        HashMap<String,LinkedHashMap<AgvInfo,NodePoint>> targets= recommend.targetsRecommend(shelfService.queryShelfList(),agvService.queryAgvList(),12);

        LinkedHashMap<AgvInfo,NodePoint> up_run=new LinkedHashMap<>();

        Map.Entry<AgvInfo, NodePoint> firstnode_up=targets.get("uptoup").entrySet().iterator().next();
        up_run.put(firstnode_up.getKey(),firstnode_up.getValue());
        targets.get("uptoup").remove(firstnode_up.getKey());

        List<Map.Entry<AgvInfo, NodePoint>> uptoup_list = new ArrayList<>(targets.get("uptoup").entrySet());
        List<Map.Entry<AgvInfo, NodePoint>> uptodown_list = new ArrayList<>(targets.get("uptodown").entrySet());
        Collections.reverse(uptoup_list);
        Collections.reverse(uptodown_list);
        for (Map.Entry<AgvInfo, NodePoint> entry:uptoup_list){
            up_run.put(entry.getKey(),entry.getValue());
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:uptodown_list){
            up_run.put(entry.getKey(),entry.getValue());
        }


        LinkedHashMap<AgvInfo,NodePoint> down_run=new LinkedHashMap<>();

        Map.Entry<AgvInfo, NodePoint> firstnode_d=targets.get("downtodown").entrySet().iterator().next();
        down_run.put(firstnode_d.getKey(),firstnode_d.getValue());
        targets.get("downtodown").remove(firstnode_d.getKey());

        List<Map.Entry<AgvInfo, NodePoint>> downtodown_list = new ArrayList<>(targets.get("downtodown").entrySet());
        List<Map.Entry<AgvInfo, NodePoint>> downtoup_list = new ArrayList<>(targets.get("downtoup").entrySet());
        Collections.reverse(downtodown_list);
        Collections.reverse(downtoup_list);
        for (Map.Entry<AgvInfo, NodePoint> entry:downtodown_list){
            down_run.put(entry.getKey(),entry.getValue());
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:downtoup_list){
            down_run.put(entry.getKey(),entry.getValue());
        }

        LinkedHashMap<AgvInfo, List<NodePoint>> up_paths= graph.findShortPaths(graph.Matrix, up_run,up_area);
        LinkedHashMap<AgvInfo, List<NodePoint>> down_paths= graph.findShortPaths(graph.Matrix, down_run,down_area);

//        LinkedHashMap<AgvInfo,List<String>> allPaths=new LinkedHashMap<>();
//        for (Map.Entry<AgvInfo,List<NodePoint>> entry:up_paths.entrySet()){
//            for (NodePoint node:entry.getValue()){
//                List<String> path=new ArrayList<>();
//                if (!path.contains("4")||!path.contains("8")||!path.contains("14")||){
//                    path.add(node.getPathNum())
//                }
//
//            }
//            allPaths.put()
//        }

        return JSONResult.ok();

    }

}
