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

import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private ShelfService shelfService;

    @Autowired
    private AgvService agvService;


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
        LinkedHashMap<AgvInfo,NodePoint> dest=new LinkedHashMap<>();
        dest.put(graph.arrayV[0].getArea().agvInfoList.get(0),graph.arrayV[31]);
        dest.put(graph.arrayV[0].getArea().agvInfoList.get(1),graph.arrayV[6]);
        dest.put(graph.arrayV[0].getArea().agvInfoList.get(2),graph.arrayV[5]);
        LinkedHashMap<AgvInfo, List<NodePoint>> paths= graph.findShortPaths(graph.Matrix, dest,0);
        AgvThreadPool agvThreadPool=new AgvThreadPool();
        agvThreadPool.LetsGo(graph,dest,paths,0);
        return JSONResult.ok(graph.arrayV);
    }
}
