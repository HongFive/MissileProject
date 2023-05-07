package edu.szu.controller;

import edu.szu.*;
import edu.szu.pojo.*;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ResultBody;
import edu.szu.pojo.vo.TaskResultBody;

import edu.szu.utils.AgvTransferUtils;
import edu.szu.utils.JSONResult;
import edu.szu.utils.ShelfTransferUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/recommend")
public class HelloController {

    @Autowired
    private OutShelfService shelfService;

    @Autowired
    private OutAgvService agvService;

//    @Autowired
//    private OutTaskService taskService;

    //将原有的outTask表改为zkt_task
    @Autowired
    private ZktTaskSercive zktTaskSercive;

    //ZktDeviceSercive
    @Autowired
    private ZktDeviceSercive zktDeviceSercive;

    @Autowired
    private ZktWarehouseShelfSercive zktWarehouseShelfSercive;

    @Autowired
    private TaskPlanResultService taskPlanResultService;

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
    public JSONResult agvPath(@PathVariable String id) throws JSONException {


        List<ShelfInfo> shelfInfoList = new ArrayList<>();
        List<ZktWarehouseShelf> zktWarehouseShelfs = zktWarehouseShelfSercive.queryZktWarehouseShelfList();
        for (ZktWarehouseShelf zktWarehouseShelf : zktWarehouseShelfs) {
            ShelfInfo shelf = ShelfTransferUtils.convert(zktWarehouseShelf);
//            shelf.setId(tmp_id++);
            shelfInfoList.add(shelf);
        }
//        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
//        List<AgvInfo> agvInfoList = agvService.queryAgvList();
        List<AgvInfo> agvInfoList = new ArrayList<>();
        List<ZktDevice> zktDevices = zktDeviceSercive.queryZktDevice("AGV");
        for (ZktDevice zktDevice : zktDevices) {
            AgvInfo agv = AgvTransferUtils.convert(zktDevice);
            agvInfoList.add(agv);
        }
        ZktTask zktTask = zktTaskSercive.queryTask(id);
        PlanRecommend recommend=new PlanRecommend();
        LinkedHashMap<String,LinkedHashMap<AgvInfo,NodePoint>> result= recommend.resourceRecommend(zktTask,shelfInfoList,agvInfoList);

        System.out.println("--------------------");

        shelfInfoList = new ArrayList<>();
        for (ZktWarehouseShelf zktWarehouseShelf : zktWarehouseShelfs) {
            ShelfInfo shelf = ShelfTransferUtils.convert(zktWarehouseShelf);
//            shelf.setId(tmp_id++);
            shelfInfoList.add(shelf);
        }
        MyGraph graph = new MyGraph(124, true);
        graph = graph.initialGraph(agvInfoList, shelfInfoList);

        List<TaskResultBody> taskRecommendResultList=new ArrayList<>();
        for (Map.Entry<String, LinkedHashMap<AgvInfo, NodePoint>> entry : result.entrySet()) {
            LinkedHashMap<AgvInfo, NodePoint> lhw = entry.getValue();
            for (AgvInfo link_agvInfo : lhw.keySet()) {
                TaskResultBody taskResultBody = new TaskResultBody();
                taskResultBody.setAgvRecommend_id(link_agvInfo.getId());

//                taskResultBody.setShelfRecommend_id(res.getShelf().getId());
//                taskResultBody.setShelfName(ShelfTransferUtils.convert(res.getShelf()));
                taskResultBody.setAgvName(link_agvInfo.getAgvName());
                for (ShelfInfo shelf : graph.arrayV[lhw.get(link_agvInfo).getId()].getShelfs()) {
                    if (link_agvInfo.getType().equals("单") && shelf.getLayer() == 1 && shelf.getState() == true) {
                        taskResultBody.setShelfName(shelf.getShelfName());
                        taskResultBody.setShelfRecommend_id(shelf.getId());
                        shelf.setState(false);
                        break;
                    } else if (link_agvInfo.getType().equals("双") && (shelf.getLayer() == 2 || shelf.getLayer() == 3) && shelf.getState() == true) {
                        taskResultBody.setShelfName(shelf.getShelfName());
                        taskResultBody.setShelfRecommend_id(shelf.getId());
                        shelf.setState(false);
                        break;
                    }
                }
                taskRecommendResultList.add(taskResultBody);
            }

        }




//        ZktTask zktTask = zktTaskSercive.queryTask(id);
//        PlanRecommend recommend=new PlanRecommend();
//        LinkedHashMap<String,LinkedHashMap<AgvInfo,NodePoint>> result= recommend.resourceRecommend(zktTask,shelfInfoList,agvInfoList);

        //第一阶段agv与路径结果
//        LinkedHashMap<AgvInfo, List<NodePoint>> paths = graph.findShortPathsRecommend(result);
//        List<ResultBody> resultList=new ArrayList<>();
//        for (Map.Entry<AgvInfo, List<NodePoint>> entry:paths.entrySet()){
//            ResultBody tempTar=new ResultBody(entry.getKey(),entry.getValue());;
//            for (Map.Entry<String,LinkedHashMap<AgvInfo,NodePoint>> entry1:result.entrySet()){
//                for (Map.Entry<AgvInfo,NodePoint> entry2:entry1.getValue().entrySet()){
//                    if (entry2.getKey()==entry.getKey()){
//                        tempTar.setNode(entry2.getValue());
//                        break;
//                    }
//                }
//            }
//            resultList.add(tempTar);
//        }
//
//        //第二阶段agv与路径结果
//        int k=0;
//        for (Map.Entry<String,LinkedHashMap<AgvInfo,NodePoint>> entry:result.entrySet()){
//            if (k>=4){
//                LinkedHashMap<AgvInfo,NodePoint> tempMap=result.get(entry.getKey());
//                for (Map.Entry<AgvInfo,NodePoint> nextStage:tempMap.entrySet()){
//                    Map.Entry<AgvInfo,List<NodePoint>> res=graph.searchForOne(nextStage);
//                    ResultBody newRes=new ResultBody(res.getKey(),nextStage.getValue(),res.getValue());
//                    resultList.add(newRes);
//                }
//            }
//            k++;
//        }
//
//
////        List<TaskRecommendResultInfo> taskRecommendResultInfoList=new ArrayList<>();
//        shelfInfoList = new ArrayList<>();
//        for (ZktWarehouseShelf zktWarehouseShelf : zktWarehouseShelfs) {
//            ShelfInfo shelf = ShelfTransferUtils.convert(zktWarehouseShelf);
////            shelf.setId(tmp_id++);
//            shelfInfoList.add(shelf);
//        }
//        graph = new MyGraph(124, true);
//        graph = graph.initialGraph(agvInfoList, shelfInfoList);
//
//        List<TaskResultBody> taskRecommendResultList=new ArrayList<>();
//
//
//
//        for (ResultBody res:resultList){
//            for (ShelfInfo shelf:graph.arrayV[res.getNode().getId()].getShelfs()){
//                if (res.getAgvInfo().getType().equals("单")&&shelf.getLayer()==1&&shelf.getState()==true){
//                    res.setShelf(shelf);
//                    shelf.setState(false);
//                    break;
//                }
//                else if (res.getAgvInfo().getType().equals("双")&&(shelf.getLayer()==2||shelf.getLayer()==3)&&shelf.getState()==true){
//                    res.setShelf(shelf);
//                    shelf.setState(false);
//                    break;
//                }
//            }
//            NodePoint dest=res.getPath().get(res.getPath().size()-1);
//            List<String> path=new ArrayList<>();
//            for (NodePoint node:res.getPath()){
//                if (path.size()>0&&path.get(path.size()-1).equals(node.getPathNum())) continue;
//                path.add(node.getPathNum());
//            }
////            TaskRecommendResultInfo tRRI=new TaskRecommendResultInfo(j++,zktTask.getTaskName(),String.valueOf(res.getShelf().getId()),String.valueOf(res.getShelf().getId()),res.getAgvInfo().getAgvName(),res.getAgvInfo().getAgvName(),
////                    dest.getPathNum(),dest.getPathNum(),res.getAgvInfo().getAgvName(),res.getAgvInfo().getAgvName(),res.getAgvInfo().getLocation(),path.toString(),new Date());
////            taskRecommendResultInfoList.add(tRRI);
//
//            System.out.println(res.getNode().getId());
//
//            TaskResultBody taskResultBody = new TaskResultBody();
//            taskResultBody.setAgvRecommend_id(res.getAgvInfo().getId());
//            taskResultBody.setShelfRecommend_id(res.getShelf().getId());
//            taskResultBody.setShelfName(ShelfTransferUtils.convert(res.getShelf()));
//            taskResultBody.setAgvName(res.getAgvInfo().getAgvName());
//            taskRecommendResultList.add(taskResultBody);
//        }

        return JSONResult.ok(taskRecommendResultList);
    }


    @GetMapping("/RunAgv/{id}")
    public JSONResult RunAgv(@PathVariable String id) throws JSONException {
//        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
//        List<AgvInfo> agvInfoList = agvService.queryAgvList();
        List<ShelfInfo> shelfInfoList = new ArrayList<>();
        List<ZktWarehouseShelf> zktWarehouseShelfs = zktWarehouseShelfSercive.queryZktWarehouseShelfList();
        for (ZktWarehouseShelf zktWarehouseShelf : zktWarehouseShelfs) {
            ShelfInfo shelf = ShelfTransferUtils.convert(zktWarehouseShelf);
//            shelf.setId(tmp_id++);
            shelfInfoList.add(shelf);
        }
//        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
//        List<AgvInfo> agvInfoList = agvService.queryAgvList();
        List<AgvInfo> agvInfoList = new ArrayList<>();
        List<ZktDevice> zktDevices = zktDeviceSercive.queryZktDevice("AGV");
        for (ZktDevice zktDevice : zktDevices) {
            AgvInfo agv = AgvTransferUtils.convert(zktDevice);
            agvInfoList.add(agv);
        }
        MyGraph graph = new MyGraph(124, true);
        graph = graph.initialGraph(agvInfoList, shelfInfoList);
        ZktTask taskInfo = zktTaskSercive.queryTask(id);
        PlanRecommend recommend = new PlanRecommend();
        LinkedHashMap<String, LinkedHashMap<AgvInfo, NodePoint>> result = recommend.resourceRecommend(taskInfo, shelfInfoList, agvInfoList);

        //第一阶段agv与路径结果
        LinkedHashMap<AgvInfo, List<NodePoint>> paths = graph.findShortPathsRecommend(result);
        List<ResultBody> resultList = new ArrayList<>();
        for (Map.Entry<AgvInfo, List<NodePoint>> entry : paths.entrySet()) {
            ResultBody tempTar = new ResultBody(entry.getKey(), entry.getValue());
            ;
            for (Map.Entry<String, LinkedHashMap<AgvInfo, NodePoint>> entry1 : result.entrySet()) {
                for (Map.Entry<AgvInfo, NodePoint> entry2 : entry1.getValue().entrySet()) {
                    if (entry2.getKey() == entry.getKey()) {
                        tempTar.setNode(entry2.getValue());
                        break;
                    }
                }
            }
            resultList.add(tempTar);
        }


        //第二阶段agv与路径结果
        int k = 0;
        for (Map.Entry<String, LinkedHashMap<AgvInfo, NodePoint>> entry : result.entrySet()) {
            if (k >= 4) {
                LinkedHashMap<AgvInfo, NodePoint> tempMap = result.get(entry.getKey());
                for (Map.Entry<AgvInfo, NodePoint> nextStage : tempMap.entrySet()) {
                    Map.Entry<AgvInfo, List<NodePoint>> res = graph.searchForOne(nextStage);
                    ResultBody newRes = new ResultBody(res.getKey(), nextStage.getValue(), res.getValue());
                    resultList.add(newRes);
                }
            }
            k++;
        }

        for (ResultBody res : resultList) {
            for (ShelfInfo shelf : res.getNode().getShelfs()) {
                if (res.getAgvInfo().getType().equals("单") && shelf.getLayer() == 1) {
                    res.setShelf(shelf);
                } else if (res.getAgvInfo().getType().equals("双") && (shelf.getLayer() == 2 || shelf.getLayer() == 3)) {
                    res.setShelf(shelf);
                }
            }
        }

        int upOne = result.get("uptoup").size() + result.get("uptodown").size();
        int downOne = result.get("downtodown").size() + result.get("downtoup").size();
        result.remove("uptoup");
        result.remove("uptodown");
        result.remove("downtodown");
        result.remove("downtoup");
        runAgvsOne(taskInfo, graph, resultList, upOne, downOne);

//        for (int i=0;i<upOne+downOne;i++){
//            resultList.remove(0);
//        }
//        agvThreadPools.setThreadPool1Shutdown(true);
//        agvThreadPools.setThreadPool2Shutdown(true);

        if (result.size() > 0) {
            while (!resultList.isEmpty()) {
                if (isThreadPool1Shutdown() && isThreadPool2Shutdown()) {
                    Map.Entry<String, LinkedHashMap<AgvInfo, NodePoint>> entry = result.entrySet().iterator().next();
                    int up=0;
                    int down=0;
                    if (entry.getKey().contains("up")){
                        up=entry.getValue().size();
                    }else if (entry.getKey().contains("down")){
                        down=entry.getValue().size();
                    }
                    result.remove(entry.getKey());
                    if (result.size()>0){
                        entry = result.entrySet().iterator().next();
                        if (entry.getKey().contains("up")){
                            up=entry.getValue().size();
                        }else if (entry.getKey().contains("down")){
                            down=entry.getValue().size();
                        }
                        result.remove(entry.getKey());
                    }
                    runAgvsOne(taskInfo, graph, resultList, up, down);
                }
            }
        }

        return JSONResult.ok();

    }


    public static final Integer Go=1;
    public static final Integer Back=0;
    Stack<ThreadNode> rightRoadStack=new Stack<>();
    Stack<ThreadNode> leftRoadStack=new Stack<>();

    private ScheduledExecutorService executorService1;

    private  ScheduledExecutorService executorService2;

    private  boolean isThreadPool1Shutdown = false;
    private  boolean isThreadPool2Shutdown = false;

    public boolean isThreadPool1Shutdown() {
        return isThreadPool1Shutdown;
    }

    public void setThreadPool1Shutdown(boolean threadPool1Shutdown) {
        isThreadPool1Shutdown = threadPool1Shutdown;
    }

    public boolean isThreadPool2Shutdown() {
        return isThreadPool2Shutdown;
    }

    public void setThreadPool2Shutdown(boolean threadPool2Shutdown) {
        isThreadPool2Shutdown = threadPool2Shutdown;
    }

    public void runAgvsOne(ZktTask task, MyGraph graph, List<ResultBody> resultBodyList, int upRunNum, int downRunNum) {

        int upPrintArea = 0;
        int downPrintArea = 123;

        //分配第一阶段任务
        //上任务
        List<ThreadNode> upAgvOne = new ArrayList<>();
        List<ThreadNode> downAgvOne = new ArrayList<>();
        for (int i = 0; i < upRunNum; i++) {
            ResultBody res = resultBodyList.get(i);
            ThreadNode node = new ThreadNode(res.getAgvInfo(), res.getPath(), res.getNode(), res.getShelf(), Go,false);
            upAgvOne.add(node);
        }
        //下任务
        for (int i = upRunNum; i <upRunNum+downRunNum; i++) {
            ResultBody res = resultBodyList.get(i);
            ThreadNode node = new ThreadNode(res.getAgvInfo(), res.getPath(), res.getNode(), res.getShelf(), Go,false);
            downAgvOne.add(node);
        }
        for (int i=0;i<upRunNum+downRunNum;i++){
            resultBodyList.remove(0);
        }

        //右货道栈初始化
        if (upAgvOne.size()>0){
            ThreadNode upFirst = upAgvOne.get(0);
            for (int i = downAgvOne.size() - 1; i > 0; i--) {
                if (downAgvOne.get(i).getPath().get(downAgvOne.get(i).getPath().size() - 1) == graph.arrayV[upPrintArea]) {
                    rightRoadStack.push(downAgvOne.get(i));
                }
            }
            for (ThreadNode node : upAgvOne) {
                if (node == upFirst) continue;
                if (node.getPath().get(node.getPath().size() - 1) == graph.arrayV[upPrintArea]) {
                    rightRoadStack.push(node);
                }
            }
            rightRoadStack.push(upFirst);
        }


        if (downAgvOne.size()>0){
            //左货道栈初始化
            ThreadNode downFirst = downAgvOne.get(0);
            for (int i = upAgvOne.size() - 1; i > 0; i--) {
                if (upAgvOne.get(i).getPath().get(upAgvOne.get(i).getPath().size() - 1) == graph.arrayV[downPrintArea]) {
                    leftRoadStack.push(upAgvOne.get(i));
                }
            }
            for (ThreadNode node : downAgvOne) {
                if (node == downFirst) continue;
                if (node.getPath().get(node.getPath().size() - 1) == graph.arrayV[downPrintArea]) {
                    leftRoadStack.push(node);
                }
            }
            leftRoadStack.push(downFirst);
        }


        executorService1 = Executors.newScheduledThreadPool(upAgvOne.size());
        executorService2 = Executors.newScheduledThreadPool(downAgvOne.size());
        isThreadPool1Shutdown=false;
        isThreadPool2Shutdown=false;

        //上出发
        for (int i = 0; i < upAgvOne.size(); i++) {
            ThreadNode agv = upAgvOne.get(i);
            executorService1.schedule(() -> {
                for (NodePoint np : agv.getPath()) {
                    try {
                        if (agv.getState().equals(Go)) {
                            if (np.getPathNum().equals(agv.getAgv().getLocation().split("-")[0])&&!agv.getStart()) {//从停放区出发
                                agv.setStart(true);
                                Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                Timestamp time=new Timestamp(beginTime.getTime()+(long) ((10/0.8)*1000));

//                                Timestamp endTime = new Timestamp(System.currentTimeMillis());
                                System.out.println("shelfId:"+agv.getShelf().getId()+" taskId:"+task.getId()+task.getTaskName()+" agvName:" + agv.getAgv().getAgvName() + " point:" + agv.getAgv().getLocation() + " action:" + "start" + " begintime:" + beginTime+" endTime:" + time);
//                                agv.newPath.add(agv.getAgv().getLocation());
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"start",agv.getAgv().getLocation(),beginTime,time);
                                System.out.println(temp.getAgvAction()+temp.getPoint());
                                taskPlanResultService.SaveTaskPlanResult(temp);
                                continue;
                            }
                            if (agv.getStart()) {//车辆出发
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    if (Integer.parseInt(entry.getKey().split("-")[1])<Integer.parseInt(agv.getAgv().getLocation().split("-")[1])) continue;
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                    Timestamp time=new Timestamp((long) ((entry.getValue() / 0.8)*1000));
                                    Timestamp time = new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                    Thread.sleep((long) sleepTime);
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" +" begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                                agv.setStart(false);
                                continue;
                            }
                            if (np.getPathNum().equals("8") || np.getPathNum().equals("14")) {//遇到弯道转弯
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (entry.getValue() / 0.8) * 1000;
                                    Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                    Thread.sleep((long) sleepTime);
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                                continue;
                            }
                            if (np.getPathNum().equals("3")){
                                Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                double sleepTime = (8.2 / 0.8) * 1000;
                                Timestamp time=new Timestamp(beginTime.getTime()+(long) ((8.2 / 0.8) * 1000));
//                                Thread.sleep((long) sleepTime);
//                                Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + "3-5" + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                agv.newPath.add("3-5");
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn","3-5",beginTime,time);
                                taskPlanResultService.SaveTaskPlanResult(temp);
                                continue;
                            }
                            if (np.getId().equals(agv.target.getId())) {//load
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                    int loadTime = 5 * 60 * 1000;
                                    if (agv.getShelf().getLayer() == 3) loadTime = 9 * 60 * 1000;
                                    else if (agv.getShelf().getLayer() == 2) loadTime = 7 * 60 * 1000;
                                    Timestamp time=new Timestamp(beginTime.getTime()+loadTime);
//                                    Thread.sleep(loadTime);
                                    if (agv.getPath().get(agv.getPath().size()-1)==graph.arrayV[0]){
                                        while (!rightRoadStack.isEmpty() && rightRoadStack.peek() != agv){
                                            Thread.sleep(500);
                                        }
                                        rightRoadStack.pop();
                                    }else if (agv.getPath().get(agv.getPath().size()-1)==graph.arrayV[123]){
                                        while (!leftRoadStack.isEmpty() && leftRoadStack.peek() != agv){
                                            Thread.sleep(500);
                                        }
                                        leftRoadStack.pop();
                                    }
                                    //装载完成
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "load" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"load",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                    agv.setState(Back);
                                }
                                continue;
                            }
                            //正常行驶
                            for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                Thread.sleep((long) sleepTime);
//                                Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                taskPlanResultService.SaveTaskPlanResult(temp);
                            }
                        } else if (agv.getState().equals(Back)) {//返程
                            if (np == graph.arrayV[upPrintArea]) {//转载区转载导弹
                                if (graph.arrayV[upPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                    graph.arrayV[upPrintArea].getArea().getAgvInfoList().add(agv.getAgv());
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                    int sleepTime = 8 * 60 * 1000;
                                    Timestamp time=new Timestamp(beginTime.getTime()+sleepTime);
//                                    Thread.sleep(sleepTime);
                                    graph.arrayV[upPrintArea].getArea().getAgvInfoList().remove(agv.getAgv());
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + graph.arrayV[upPrintArea].getPathNum() + "-1" + " action:" + "transplant" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(graph.arrayV[upPrintArea].getPathNum() + "-1");
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"transplant",graph.arrayV[upPrintArea].getPathNum() + "-1",beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                            }else {
                                List<Map.Entry<String, Double>> list = new ArrayList<>(np.getPointList().entrySet());
                                Collections.reverse(list);
                                if (np.getPathNum().equals("8")){
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (8.2 / 0.8) * 1000;
//                                    Thread.sleep((long) sleepTime);
                                    Timestamp time=new Timestamp(beginTime.getTime()+(long) ((8.2 / 0.8) * 1000));
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + "8-5" + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add("8-5");
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn","8-5",beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                    continue;
                                }
                                if (np.getPathNum().equals("3") || np.getPathNum().equals("16")) {//遇到弯道转弯
                                    for (Map.Entry<String, Double> entry : list) {
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                        double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                        Thread.sleep((long) sleepTime);
                                        Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                        Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                        System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                        agv.newPath.add(entry.getKey());
                                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn",entry.getKey(),beginTime,time);
                                        taskPlanResultService.SaveTaskPlanResult(temp);
                                    }
                                }else {
                                    for (Map.Entry<String, Double> entry : list) {
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                        if (entry.getKey().equals("1-1") && !graph.arrayV[upPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                            while (!graph.arrayV[upPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                                Thread.sleep(500);
                                            }
                                            Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                            Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                            System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "wait" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                            agv.newPath.add(entry.getKey());
                                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"wait",entry.getKey(),beginTime,time);
                                            taskPlanResultService.SaveTaskPlanResult(temp);
                                        } else {
//                                            double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                            Thread.sleep((long) sleepTime);
                                            Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
//                                            Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                            System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" +" begintime:" + beginTime+ " endTime:" + endTime);
//                                            agv.newPath.add(entry.getKey());
                                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                            taskPlanResultService.SaveTaskPlanResult(temp);
                                        }
                                    }
                                }
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 3 * i, TimeUnit.SECONDS);
        }

        //下出发
        for (int i = 0; i < downAgvOne.size(); i++) {
            ThreadNode agv = downAgvOne.get(i);
            executorService2.schedule(() -> {
                for (NodePoint np : agv.getPath()) {
                    try {
                        if (agv.getState().equals(Go)) {
                            //从停放区出发
                            if (np.getPathNum().equals(agv.getAgv().getLocation().split("-")[0])&&!agv.getStart()) {
                                agv.setStart(true);
                                Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                Thread.sleep((long) ((10 / 0.8) * 1000));
//                                Timestamp endTime = new Timestamp(System.currentTimeMillis());
                                Timestamp time=new Timestamp(beginTime.getTime()+(long) ((10 / 0.8) * 1000));
//                                System.out.println("shelfId:"+agv.getShelf().getId()+" taskId:"+task.getId()+task.getTaskName()+"task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + agv.getAgv().getLocation() + " action:" + "start" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                agv.newPath.add(agv.getAgv().getLocation());
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"start",agv.getAgv().getLocation(),beginTime,time);
                                taskPlanResultService.SaveTaskPlanResult(temp);
                                continue;
                            }
                            if (agv.getStart()) {//车辆出发
                                if (np.getId().equals(graph.arrayV[31].getId())){
                                    for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                        if (Integer.parseInt(entry.getKey().split("-")[1])>Integer.parseInt(agv.getAgv().getLocation().split("-")[1])) continue;
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                        double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                        Thread.sleep((long) sleepTime);
                                        Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8) * 1000));
//                                        Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                        System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                        agv.newPath.add(entry.getKey());
                                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                        taskPlanResultService.SaveTaskPlanResult(temp);
                                        agv.setStart(false);
                                    }
                                }else {
                                    for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                        if (!(entry.getKey().split("-")[1]).equals(agv.getAgv().getLocation().split("-")[1])) continue;
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                        double sleepTime = (10/0.8) * 1000;
//                                        Thread.sleep((long) sleepTime);
                                        Timestamp time=new Timestamp(beginTime.getTime()+(long) ((10/0.8) * 1000));
//                                        Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                        System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                        agv.newPath.add(entry.getKey());
                                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn",entry.getKey(),beginTime,time);
                                        taskPlanResultService.SaveTaskPlanResult(temp);
                                    }
                                }
                                continue;
                            }
                            if (np.getPathNum().equals("5") || np.getPathNum().equals("23")) {//遇到弯道转弯
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                    Thread.sleep((long) sleepTime);
                                    Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8) * 1000));
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                                continue;
                            }
                            if (np.getPathNum().equals("16")){
                                Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                double sleepTime = (9 / 0.8) * 1000;
//                                Thread.sleep((long) sleepTime);
                                Timestamp time=new Timestamp(beginTime.getTime()+(long) ((9 / 0.8) * 1000));
//                                Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + "16-1" + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                agv.newPath.add("16-1");
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn","16-1",beginTime,time);
                                taskPlanResultService.SaveTaskPlanResult(temp);
                                continue;
                            }
                            if (np.getId().equals(agv.target.getId())) {//load
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                    int loadTime = 5 * 60 * 1000;
                                    if (agv.getShelf().getLayer() == 3) loadTime = 9 * 60 * 1000;
                                    else if (agv.getShelf().getLayer() == 2) loadTime = 7 * 60 * 1000;
                                    Timestamp time=new Timestamp(beginTime.getTime()+loadTime);
//                                    Thread.sleep(loadTime);
                                    if (agv.getPath().get(agv.getPath().size()-1)==graph.arrayV[0]){
                                        while (!rightRoadStack.isEmpty() && rightRoadStack.peek() != agv){
                                            Thread.sleep(500);
                                        }
                                        rightRoadStack.pop();
                                    }else if (agv.getPath().get(agv.getPath().size()-1)==graph.arrayV[123]){
                                        while (!leftRoadStack.isEmpty() && leftRoadStack.peek() != agv){
                                            Thread.sleep(500);
                                        }
                                        leftRoadStack.pop();
                                    }
                                    //装载完成
                                    leftRoadStack.pop();
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "load" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"load",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                    agv.setState(Back);
                                }
                            } else {//正常行驶
                                for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                    Thread.sleep((long) sleepTime);
                                    Timestamp time=new Timestamp(beginTime.getTime()+(long) ((entry.getValue() / 0.8) * 1000));
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(entry.getKey());
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                            }
                        } else if (agv.getState().equals(Back)) {//返程
                            if (np == graph.arrayV[downPrintArea]) {//转载区转载导弹
                                if (graph.arrayV[downPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                    graph.arrayV[downPrintArea].getArea().getAgvInfoList().add(agv.getAgv());
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                    int sleepTime = 8 * 60 * 1000;
//                                    Thread.sleep(sleepTime);
                                    Timestamp time=new Timestamp(beginTime.getTime()+sleepTime);
                                    graph.arrayV[downPrintArea].getArea().getAgvInfoList().remove(agv.getAgv());
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + graph.arrayV[downPrintArea].getPathNum() + "-1" + " action:" + "transplant" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add(graph.arrayV[downPrintArea].getPathNum() + "-1");
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"transplant",graph.arrayV[downPrintArea].getPathNum(),beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                }
                            }else {
                                List<Map.Entry<String, Double>> list = new ArrayList<>(np.getPointList().entrySet());
                                Collections.reverse(list);
                                if (np.getPathNum().equals("23")){//回程变道
                                    Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                    double sleepTime = (9 / 0.8) * 1000;
//                                    Thread.sleep((long) sleepTime);
                                    Timestamp time=new Timestamp(beginTime.getTime()+(long) ((9 / 0.8) * 1000));
//                                    Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                    System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + "23-1" + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                    agv.newPath.add("23-1");
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn","23-1",beginTime,time);
                                    taskPlanResultService.SaveTaskPlanResult(temp);
                                    continue;
                                }
                                if (np.getPathNum().equals("3") || np.getPathNum().equals("16")) {//遇到弯道转弯
                                    for (Map.Entry<String, Double> entry :list) {
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
//                                        double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                        Thread.sleep((long) sleepTime);
                                        Timestamp time=new Timestamp(beginTime.getTime()+(long) (entry.getValue() / 0.8) * 1000);
//                                        Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                        System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "turn" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                        agv.newPath.add(entry.getKey());
                                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"turn",entry.getKey(),beginTime,time);
                                        taskPlanResultService.SaveTaskPlanResult(temp);
                                    }
                                }else {
                                    for (Map.Entry<String, Double> entry : list) {
                                        Timestamp beginTime = new Timestamp(System.currentTimeMillis());
                                        if (entry.getKey().equals("19-18") && !graph.arrayV[downPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                            while (!graph.arrayV[downPrintArea].getArea().getAgvInfoList().isEmpty()) {
                                                Thread.sleep(500);
                                            }
                                            Timestamp time=new Timestamp(beginTime.getTime()+(long) (entry.getValue() / 0.8) * 1000);
//                                            Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                            System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "wait" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                            agv.newPath.add(entry.getKey());
                                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"wait",entry.getKey(),beginTime,time);
                                            taskPlanResultService.SaveTaskPlanResult(temp);
                                        } else {
//                                            double sleepTime = (entry.getValue() / 0.8) * 1000;
//                                            Thread.sleep((long) sleepTime);
                                            Timestamp time=new Timestamp(beginTime.getTime()+(long) (entry.getValue() / 0.8) * 1000);
//                                            Timestamp endTime = new Timestamp(System.currentTimeMillis());
//                                            System.out.println("task:"+task.getTaskName()+"agvName:" + agv.getAgv().getAgvName() + " point:" + entry.getKey() + " action:" + "Straight" + " begintime:" + beginTime+ " endTime:" + endTime);
//                                            agv.newPath.add(entry.getKey());
                                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),"Straight",entry.getKey(),beginTime,time);
                                            taskPlanResultService.SaveTaskPlanResult(temp);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 3 * i, TimeUnit.SECONDS);
        }
        executorService1.shutdown();
        isThreadPool1Shutdown=true;
        executorService2.shutdown();
        isThreadPool2Shutdown=true;
        System.out.println("jieshula !");
    }

}
