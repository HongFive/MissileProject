package edu.szu.controller;

import edu.szu.AgvService;
import edu.szu.ShelfService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;
import edu.szu.utils.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private ShelfService shelfService;

    @Autowired
    private AgvService agvService;


    NodePoint[] a1;
    double[][] matrix;
    HashMap<NodePoint,Integer> hm;
    int maxDown = 25;

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

    @GetMapping("/path")
    public JSONResult agvPath(){
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();
        List<AgvInfo> agvInfoList=agvService.queryAgvList();
        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);

        int up_area=0;
        int down_area=115;

        LinkedHashMap<AgvInfo,NodePoint> up_run=new LinkedHashMap<>();
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(0),graph.arrayV[31]);
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(1),graph.arrayV[6]);
        up_run.put(graph.arrayV[up_area].getArea().agvInfoList.get(2),graph.arrayV[66]);
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

        AgvThreadPool agvThreadPool=new AgvThreadPool();
        agvThreadPool.RuningAgvs(graph,up_run,up_paths,down_run,down_paths);
//        agvThreadPool.LetsGo(graph,up_run,up_paths,up_area);
        return JSONResult.ok(graph.arrayV);
    }

}