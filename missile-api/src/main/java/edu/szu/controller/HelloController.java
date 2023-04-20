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
}
