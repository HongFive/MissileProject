package edu.szu.controller;

import edu.szu.OutAgvService;
import edu.szu.OutShelfService;
import edu.szu.OutTaskService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.TaskInfo;
import edu.szu.pojo.TaskRecommendResultInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ResultBody;
import edu.szu.utils.JSONResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/recommend")
public class HelloController {

    @Autowired
    private OutShelfService shelfService;

    @Autowired
    private OutAgvService agvService;

    @Autowired
    private OutTaskService taskService;

    @Autowired
    private Sid sid;

    //上装载区从左边道运行路径长度
    double upLeftPathWeight = 1017.0;

    //下装载区从左边道路运行路径长度
    double downLeftPathWeight = 825.0;



    @PostMapping("/updateTask")
    public JSONResult agvPathTest(@RequestParam("shelfIdList") List<Integer> shelfIdList) {

        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
        List<AgvInfo> agvInfoList = agvService.queryAgvList();
        MyGraph graph = new MyGraph(124, true);
        graph = graph.initialGraph(agvInfoList, shelfInfoList);

        int up_area = 121;
        int down_area = 122;
        LinkedHashMap<AgvInfo, ShelfInfo> targetAgv = new LinkedHashMap<>();


        //测试案例
        List<Integer> shelfsId=new ArrayList<>(shelfIdList);
        List<ShelfInfo> targetShelfs=new ArrayList<>();
        for (Integer id:shelfsId){
            targetShelfs.add(shelfInfoList.get(id-1));
        }


        if (targetShelfs.size()>agvInfoList.size()){
            return JSONResult.errorMsg("Not enough agvs to load shelfs");
        }


        //根据货架分配agv
        for (ShelfInfo shelf : targetShelfs) {//右货架
            if (shelf.getLayer() == 1) {//目标货架为底层
                if (shelf.getLocation().split("-")[0].equals("4") || shelf.getLocation().split("-")[0].equals("9")) {//货架在右货道
                    boolean isAdd = false;
                    for (AgvInfo agv : graph.arrayV[up_area].getArea().getAgvInfoList()) {//优先分配上停放区车辆
                        if (agv.getType().equals("单")&&!targetAgv.containsKey(agv)) {
                            targetAgv.put(agv, shelf);
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        for (AgvInfo agv : graph.arrayV[down_area].getArea().getAgvInfoList()) {//若上停放区无匹配车辆则分配下停放区车辆
                            if (agv.getType().equals("单")&&!targetAgv.containsKey(agv)) {
                                targetAgv.put(agv, shelf);
                                break;
                            }
                        }
                    }
                } else if (shelf.getLocation().split("-")[0].equals("15") || shelf.getLocation().split("-")[0].equals("22")) {
                    boolean isAdd = false;
                    for (AgvInfo agv : graph.arrayV[down_area].getArea().getAgvInfoList()) {//优先分配下停放区车辆
                        if (agv.getType().equals("单")&&!targetAgv.containsKey(agv)) {
                            targetAgv.put(agv, shelf);
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        for (AgvInfo agv : graph.arrayV[up_area].getArea().getAgvInfoList()) {//若下停放区无匹配车辆则分配上停放区车辆
                            if (agv.getType().equals("单")&&!targetAgv.containsKey(agv)) {
                                targetAgv.put(agv, shelf);
                                break;
                            }
                        }
                    }
                }
            } else if (shelf.getLayer() == 2 || shelf.getLayer() == 3) {
                if (shelf.getLocation().split("-")[0].equals("4") || shelf.getLocation().split("-")[0].equals("9")) {//货架在右货道
                    boolean isAdd = false;
                    for (AgvInfo agv : graph.arrayV[up_area].getArea().getAgvInfoList()) {//优先分配上停放区车辆
                        if (agv.getType().equals("双")&&!targetAgv.containsKey(agv)) {
                            targetAgv.put(agv, shelf);
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        for (AgvInfo agv : graph.arrayV[down_area].getArea().getAgvInfoList()) {//若上停放区无匹配车辆则分配下停放区车辆
                            if (agv.getType().equals("双")&&!targetAgv.containsKey(agv)) {
                                targetAgv.put(agv, shelf);
                                break;
                            }
                        }
                    }
                } else if (shelf.getLocation().split("-")[0].equals("15") || shelf.getLocation().split("-")[0].equals("22")) {
                    boolean isAdd = false;
                    for (AgvInfo agv : graph.arrayV[down_area].getArea().getAgvInfoList()) {//优先分配下停放区车辆
                        if (agv.getType().equals("双")&&!targetAgv.containsKey(agv)) {
                            targetAgv.put(agv, shelf);
                            isAdd = true;
                            break;
                        }
                    }
                    if (!isAdd) {
                        for (AgvInfo agv : graph.arrayV[up_area].getArea().getAgvInfoList()) {//若下停放区无匹配车辆则分配上停放区车辆
                            if (agv.getType().equals("双")&&!targetAgv.containsKey(agv)) {
                                targetAgv.put(agv, shelf);
                                break;
                            }
                        }
                    }
                }
            }
        }
        //TODO 上下分配车辆的平衡处理
        //根据货架找点位
        LinkedHashMap<AgvInfo, NodePoint> targets = new LinkedHashMap<>();
        for (Map.Entry<AgvInfo, ShelfInfo> entry : targetAgv.entrySet()) {
            switch (entry.getValue().getLocation().split("-")[0]) {
                case "4":
                    for (int i = 4; i <= 28; i++) {
                        if (graph.arrayV[i].getPointList().entrySet().iterator().next().getKey().equals(entry.getValue().getLocation())) {
                            targets.put(entry.getKey(), graph.arrayV[i]);
                            break;
                        }
                    }
                    break;
                case "9":
                    for (int i = 33; i <= 57; i++) {
                        if (graph.arrayV[i].getPointList().entrySet().iterator().next().getKey().equals(entry.getValue().getLocation())) {
                            targets.put(entry.getKey(), graph.arrayV[i]);
                            break;
                        }
                    }
                    break;
                case "15":
                    for (int i = 63; i <= 87; i++) {
                        if (graph.arrayV[i].getPointList().entrySet().iterator().next().getKey().equals(entry.getValue().getLocation())) {
                            targets.put(entry.getKey(), graph.arrayV[i]);
                            break;
                        }
                    }
                    break;
                case "22":
                    for (int i = 94; i <= 118; i++) {
                        if (graph.arrayV[i].getPointList().entrySet().iterator().next().getKey().equals(entry.getValue().getLocation())) {
                            targets.put(entry.getKey(), graph.arrayV[i]);
                            break;
                        }
                    }
                    break;
            }
        }


//        LinkedHashMap<AgvInfo,NodePoint> agvShelf=new LinkedHashMap<>();
//        agvShelf.put(graph.arrayV[up_area].getArea().agvInfoList.get(0),graph.arrayV[33]);
//        agvShelf.put(graph.arrayV[up_area].getArea().agvInfoList.get(1),graph.arrayV[35]);
//        agvShelf.put(graph.arrayV[up_area].getArea().agvInfoList.get(2),graph.arrayV[10]);
//        agvShelf.put(graph.arrayV[up_area].getArea().agvInfoList.get(3),graph.arrayV[38]);
////        LinkedHashMap<AgvInfo, List<NodePoint>> up_paths= graph.findShortPathsSafe(up_run,up_area);//上转载区出发小车路径
//
////        LinkedHashMap<AgvInfo,NodePoint> down_run=new LinkedHashMap<>();
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(0),graph.arrayV[117]);
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(1),graph.arrayV[112]);
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(2),graph.arrayV[82]);
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(3),graph.arrayV[84]);
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(4),graph.arrayV[55]);
//        agvShelf.put(graph.arrayV[down_area].getArea().agvInfoList.get(5),graph.arrayV[27]);
//        LinkedHashMap<AgvInfo, List<NodePoint>> down_paths= graph.findShortPathsSafe(down_run,down_area);
        LinkedHashMap<AgvInfo, List<NodePoint>> paths = graph.findShortPathsSafe(targets);

        List<TaskRecommendResultInfo> taskRecommendResultInfoList=new ArrayList<>();
        int j=1;
        boolean errorshelf=false;
        for (Map.Entry<AgvInfo,List<NodePoint>> entry:paths.entrySet()){
            List<String> path=new ArrayList<>();
            if (entry.getValue().size()==0){
                errorshelf=true;
                TaskRecommendResultInfo tRRI=new TaskRecommendResultInfo(j++,null,String.valueOf(targetAgv.get(entry.getKey()).getId()),String.valueOf(targetAgv.get(entry.getKey()).getId()),entry.getKey().getAgvName(),entry.getKey().getAgvName(),
                        null,null,entry.getKey().getAgvName(),entry.getKey().getAgvName(),entry.getKey().getLocation(),path.toString(),new Date());
                taskRecommendResultInfoList.add(tRRI);
            }else {
                NodePoint dest=entry.getValue().get(entry.getValue().size()-1);
                for (NodePoint node:entry.getValue()){
                    if (path.size()>0&&path.get(path.size()-1).equals(node.getPathNum())) continue;
                    path.add(node.getPathNum());
                }
                TaskRecommendResultInfo tRRI=new TaskRecommendResultInfo(j++,null,String.valueOf(targetAgv.get(entry.getKey()).getId()),String.valueOf(targetAgv.get(entry.getKey()).getId()),entry.getKey().getAgvName(),entry.getKey().getAgvName(),
                        dest.getPathNum(),dest.getPathNum(),entry.getKey().getAgvName(),entry.getKey().getAgvName(),entry.getKey().getLocation(),path.toString(),new Date());
                taskRecommendResultInfoList.add(tRRI);
            }
        }

        if (errorshelf){
            StringBuilder errorMsg= new StringBuilder("Shelfs");
            for (Map.Entry<AgvInfo,List<NodePoint>> entry:paths.entrySet()){
                if (entry.getValue().size()==0){
                    errorMsg.append(" id:").append(targetAgv.get(entry.getKey()).getId()).append(":");
                    errorMsg.append("name").append(targetAgv.get(entry.getKey()).getShelfName()).append(" ");
                }
            }
            errorMsg.append("transport paths will clash with other Agvs!");
            return JSONResult.errordata(errorMsg.toString(),taskRecommendResultInfoList);
        }else return JSONResult.ok(taskRecommendResultInfoList);

//        AgvThreadPool agvThreadPool=new AgvThreadPool();
//        agvThreadPool.LetsGo(graph,up_run,up_paths,up_area);
//        agvThreadPool.RuningAgvs(graph,up_run,up_paths,down_run,down_paths);
//        agvThreadPool.LetsGo(graph,up_run,up_paths,up_area);

    }

    @GetMapping("/task/{id}")
    public JSONResult agvPath(@PathVariable int id){
        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
        List<AgvInfo> agvInfoList = agvService.queryAgvList();
        MyGraph graph = new MyGraph(124, true);
        graph = graph.initialGraph(agvInfoList, shelfInfoList);
        TaskInfo taskInfo = taskService.queryTaskInfo(id);
        PlanRecommend recommend=new PlanRecommend();
        LinkedHashMap<String,LinkedHashMap<AgvInfo,NodePoint>> result= recommend.resourceRecommend(taskInfo,shelfInfoList,agvInfoList);

        //第一阶段agv与路径结果
        LinkedHashMap<AgvInfo, List<NodePoint>> paths = graph.findShortPathsRecommend(result);
        List<ResultBody> resultList=new ArrayList<>();
        for (Map.Entry<AgvInfo, List<NodePoint>> entry:paths.entrySet()){
            ResultBody tempTar=new ResultBody(entry.getKey(),entry.getValue());;
            for (Map.Entry<String,LinkedHashMap<AgvInfo,NodePoint>> entry1:result.entrySet()){
                for (Map.Entry<AgvInfo,NodePoint> entry2:entry1.getValue().entrySet()){
                    if (entry2.getKey()==entry.getKey()){
                        tempTar.setNode(entry2.getValue());
                        break;
                    }
                }
            }
            resultList.add(tempTar);
        }

        //第二阶段agv与路径结果
        int k=0;
        for (Map.Entry<String,LinkedHashMap<AgvInfo,NodePoint>> entry:result.entrySet()){
            if (k>=4){
                LinkedHashMap<AgvInfo,NodePoint> tempMap=result.get(entry.getKey());
                for (Map.Entry<AgvInfo,NodePoint> nextStage:tempMap.entrySet()){
                    Map.Entry<AgvInfo,List<NodePoint>> res=graph.searchForOne(nextStage);
                    ResultBody newRes=new ResultBody(res.getKey(),nextStage.getValue(),res.getValue());
                    resultList.add(newRes);
                }
            }
            k++;
        }


        List<TaskRecommendResultInfo> taskRecommendResultInfoList=new ArrayList<>();
        int j=1;
        for (ResultBody res:resultList){
            for (ShelfInfo shelf:res.getNode().getShelfs()){
                if (res.getAgvInfo().getType().equals("单")&&shelf.getLayer()==1){res.setShelf(shelf);}
                else if (res.getAgvInfo().getType().equals("双")&&(shelf.getLayer()==2||shelf.getLayer()==3)){res.setShelf(shelf);}
            }
            NodePoint dest=res.getPath().get(res.getPath().size()-1);
            List<String> path=new ArrayList<>();
            for (NodePoint node:res.getPath()){
                if (path.size()>0&&path.get(path.size()-1).equals(node.getPathNum())) continue;
                path.add(node.getPathNum());
            }
            TaskRecommendResultInfo tRRI=new TaskRecommendResultInfo(j++,taskInfo.gettName(),String.valueOf(res.getShelf().getId()),String.valueOf(res.getShelf().getId()),res.getAgvInfo().getAgvName(),res.getAgvInfo().getAgvName(),
                    dest.getPathNum(),dest.getPathNum(),res.getAgvInfo().getAgvName(),res.getAgvInfo().getAgvName(),res.getAgvInfo().getLocation(),path.toString(),new Date());
            taskRecommendResultInfoList.add(tRRI);
        }

        return JSONResult.ok(taskRecommendResultInfoList);
    }


}
