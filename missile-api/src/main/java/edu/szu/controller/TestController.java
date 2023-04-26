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
@RequestMapping("/test")
public class TestController {
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
    public JSONResult agvPath(int taskNum){
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();
        List<AgvInfo> agvInfoList=agvService.queryAgvList();
//        MyGraph graph = new MyGraph(118,true);
//        graph = graph.initialGraph(agvInfoList,shelfInfoList);

        /**
         * 联调代码
         */
        for (int i = 0; i < shelfInfoList.size(); i++) {
            if (!shelfInfoList.get(i).getState()) {
                shelfInfoList.get(i).setState(true);
            }
        }

        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);
        a1 = graph.arrayV;
        matrix = graph.Matrix;
        hm = graph.map;

        a1[31].getShelfs().get(0).setState(false);
        a1[31].getShelfs().get(1).setState(false);

        a1[3].getShelfs().get(0).setState(false);
        a1[3].getShelfs().get(1).setState(false);

        HashMap<String,LinkedHashMap<AgvInfo,NodePoint>> map = taskRecommend(a1,matrix,hm,taskNum);

        LinkedHashMap<AgvInfo,NodePoint> upToUP = map.get("uptoup");
        LinkedHashMap<AgvInfo,NodePoint> upToDown = map.get("upToDown");
        LinkedHashMap<AgvInfo,NodePoint> downToDown = map.get("downToDown");
        LinkedHashMap<AgvInfo,NodePoint> downToUp = map.get("downToUp");


//        LinkedHashMap<AgvInfo,NodePoint> dest=new LinkedHashMap<>();
//        dest.put(graph.arrayV[0].getArea().agvInfoList.get(0),graph.arrayV[31]);
//        dest.put(graph.arrayV[0].getArea().agvInfoList.get(1),graph.arrayV[6]);
//        dest.put(graph.arrayV[0].getArea().agvInfoList.get(2),graph.arrayV[5]);
        LinkedHashMap<AgvInfo, List<NodePoint>> upToUP_paths= graph.findShortPaths(graph.Matrix, upToUP,0);
        LinkedHashMap<AgvInfo, List<NodePoint>> upToDown_paths= graph.findShortPaths(graph.Matrix, upToDown,0);
        LinkedHashMap<AgvInfo, List<NodePoint>> downToDown_paths= graph.findShortPaths(graph.Matrix, downToDown,0);
        LinkedHashMap<AgvInfo, List<NodePoint>> downToUp_paths= graph.findShortPaths(graph.Matrix, downToUp,0);


        AgvThreadPool agvThreadPool=new AgvThreadPool();
        agvThreadPool.LetsGo(graph,upToUP,upToUP_paths,0);
        return JSONResult.ok(graph.arrayV);
    }

    /**
     * 任务推荐
     *
     * @return
     */
    @GetMapping("/resource")
    public JSONResult resourceRecommend(int taskNum) {
        List<ShelfInfo> shelfInfoList = shelfService.queryShelfList();
        List<AgvInfo> agvInfoList = agvService.queryAgvList();

        //测试案例1，货架满仓，小车上下各6辆均匀分布
        for (int i = 0; i < shelfInfoList.size(); i++) {
            if (!shelfInfoList.get(i).getState()) {
                shelfInfoList.get(i).setState(true);
            }
        }

        MyGraph graph = new MyGraph(118,true);
        graph = graph.initialGraph(agvInfoList,shelfInfoList);
        a1 = graph.arrayV;
        matrix = graph.Matrix;
        hm = graph.map;

        a1[31].getShelfs().get(0).setState(false);
        a1[31].getShelfs().get(1).setState(false);

        a1[3].getShelfs().get(0).setState(false);
        a1[3].getShelfs().get(1).setState(false);

        HashMap<String,LinkedHashMap<AgvInfo,NodePoint>> map = taskRecommend(a1,matrix,hm,taskNum);

        for (String str : map.keySet()) {
            System.out.println(str);
            for(AgvInfo agv : map.get(str).keySet()){
                System.out.println(hm.get(map.get(str).get(agv)));
            }

        }
        return JSONResult.ok(map);
    }

    //第一阶段前12个任务节点推荐
    public  HashMap<String,LinkedHashMap<AgvInfo,NodePoint>> taskRecommend(NodePoint[] a1, double[][] matrix, HashMap<NodePoint, Integer> hm, int taskNum){

        //关闭下装载区到上装载区的通道
        matrix[29][28] = Integer.MAX_VALUE;

        //设定返回值
        HashMap<String,LinkedHashMap<AgvInfo,NodePoint>> sblhw= new HashMap<>();

        //返回HashMap
        LinkedHashMap<AgvInfo,NodePoint> upToUP = new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> upToDown = new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downToDown = new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downToUp = new LinkedHashMap<>();

        LinkedHashMap<AgvInfo, NodePoint> resultMap = new LinkedHashMap<AgvInfo,NodePoint>();

        //获取初始节点
        NodePoint upPoint = a1[0];
        NodePoint downPoint = a1[115];

        //上下装载区
        ReprintArea upReprint = upPoint.getArea();
        ReprintArea downReprint = downPoint.getArea();

        //上下装载区agv列表
        List<AgvInfo> upAgvInfoList = upReprint.getAgvInfoList();
        List<AgvInfo> downAgvInfoList = downReprint.getAgvInfoList();

        //预占用点位
        ArrayList<NodePoint> usedNodePoint = new ArrayList<>();
        ArrayList<NodePoint> upUsedNodePoints = new ArrayList<>();
        ArrayList<NodePoint> downUsedNodePoints = new ArrayList<>();
        ArrayList<NodePoint> downToUpNodePoints = new ArrayList<>();
        ArrayList<NodePoint> upToDownNodePoints = new ArrayList<>();

        boolean[] visited = new boolean[a1.length];
        double[] pathWeight = new double[a1.length];

        //寻找任务
        boolean upTask = true;

        //第一个任务，逻辑不同
        boolean upfirst = true;
        boolean downfirst = true;

        //上转载区第一个任务的行号
        int upTaskLimit = -1;

        //下转载区第一个任务的行号
        int downTaskLimit = 26;

        while(taskNum != 0){

            taskNum -= 2;
            //按照上->下顺序分配2个任务
            //分为三种情况
            //1. 该装载区agv数量足够
            //2. 该装载区数量差一个
            //3. 该装载区无agv

            if(upTask){
                //装载区数量足够，直接分配
                if(upAgvInfoList.size()>=2){
                    for (int i = 0; i < 2; i++) {
                        //找到agv,有底层agv优先分配
                        AgvInfo upAgvFirstSingle = findFirstAgvSingle(upAgvInfoList);
                        AgvInfo upAgvFirstDouble = findFirstAgvDouble(upAgvInfoList);

                        //设置参数
                        setParam(visited,pathWeight,usedNodePoint);

                        //找到最近NodePoint
                        NodePoint src = null;
                        if(upUsedNodePoints.size() == 0) src = upPoint;
                        else src = upUsedNodePoints.get(upUsedNodePoints.size()-1);

                        //找到最短路径
                        int limit = upfirst? 5 : 25;

                        NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,true,limit,"单",upTaskLimit,downTaskLimit);
                        //寻找底层agv最近节点
                        if(temp != null && upAgvFirstSingle != null){
                            //如果找到
                            //1. 加入到返回值
                            resultMap.put(upAgvFirstSingle,temp);
                            upToUP.put(upAgvFirstSingle,temp);
                            //将点位加入到已使用点位
                            usedNodePoint.add(temp);
                            upUsedNodePoints.add(temp);
                            //将该agv出列
                            upAgvInfoList.remove(upAgvFirstSingle);
                        }
                        else{
                            //底层未找到，寻找高层
                            setParam(visited,pathWeight,usedNodePoint);

                            temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,true,limit,"双",upTaskLimit,downTaskLimit);
                            if(temp != null){
                                //如果找到
                                //1. 加入到返回值
                                resultMap.put(upAgvFirstDouble,temp);
                                upToUP.put(upAgvFirstDouble,temp);
                                //2. 加入到已使用点位
                                usedNodePoint.add(temp);
                                upUsedNodePoints.add(temp);
                                //将该agv出列
                                upAgvInfoList.remove(upAgvFirstDouble);
                            }
                        }

                        //如果找到的是上转载区的第一个任务，设置行数限制
                        if(limit == 5){

                            upTaskLimit = Integer.parseInt(upUsedNodePoints.get(0).getShelfs().get(0).getLocation().split("-")[1]);
                            System.out.println("上转载区第一个任务的行数是 " + upTaskLimit);
                        }

                        upfirst = false;
                    }
                }
                //上装载区agv数量为1，从下装载区调用
                else if(upAgvInfoList.size() == 1){
                    //找到唯一的agv
                    AgvInfo agv = upAgvInfoList.get(0);

                    setParam(visited,pathWeight,usedNodePoint);

                    int limit = upfirst? 5:25;

                    NodePoint src = null;
                    if(upUsedNodePoints.size() == 0) src = upPoint;
                    else src = upUsedNodePoints.get(upUsedNodePoints.size()-1);

                    //找到最近NodePoint
                    NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,true,limit,agv.getType(),upTaskLimit,downTaskLimit);

                    //将其加入到返回值
                    resultMap.put(agv,temp);
                    upToUP.put(agv,temp);
                    //加入到已使用点位
                    usedNodePoint.add(temp);
                    upUsedNodePoints.add(temp);
                    //agv出列
                    upAgvInfoList.remove(agv);

                    //如果找到的是上转载区的第一个任务，设置行数限制
                    if(limit == 5){

                        upTaskLimit = Integer.parseInt(upUsedNodePoints.get(0).getShelfs().get(0).getLocation().split("-")[1]);
                        System.out.println("上转载区第一个任务的行数是 " + upTaskLimit);
                    }

                    // 从下装载区调用一辆agv
                    agv = downAgvInfoList.get(0);

                    // 打开通道
                    matrix[29][28] = 33.281;

                    if(downToUpNodePoints.size() == 0) src = downPoint;
                    else src = downToUpNodePoints.get(downToUpNodePoints.size()-1);

                    setParam(visited,pathWeight,usedNodePoint);

                    temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,false,25,agv.getType(),-1,26);

                    //将其加入到返回值
                    resultMap.put(agv,temp);
                    downToUp.put(agv,temp);
                    //加入到已使用点位
                    usedNodePoint.add(temp);
                    downToUpNodePoints.add(temp);
                    //agv出列
                    downAgvInfoList.remove(agv);

                    //关闭通道
                    matrix[29][28] = Integer.MAX_VALUE;

                    upfirst = false;

                } else if (upAgvInfoList.size() == 0) {
                    //上装载区无agv 分为两种情况
                    //1. 最后两个任务 无需调用agv
                    if(taskNum == 0){
                        taskNum += 2;
                    }else{
                        //非最后两个任务，调用两个agv
                        // 打开通道
                        matrix[29][28] = 33.281;
                        for (int i = 0; i < 2; i++) {

                            setParam(visited,pathWeight,usedNodePoint);

                            AgvInfo agv = downAgvInfoList.get(0);

                            NodePoint src = null;
                            if(downToUpNodePoints.size() == 0) src = downPoint;
                            else src = downToUpNodePoints.get(downToUpNodePoints.size()-1);

                            //找到NodePoint
                            NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,false,25,agv.getType(),-1,26);

                            //将其加入到返回值
                            resultMap.put(agv,temp);
                            downToUp.put(agv,temp);
                            //加入到已使用点位
                            usedNodePoint.add(temp);
                            downToUpNodePoints.add(temp);
                            //agv出列
                            downAgvInfoList.remove(agv);
                        }
                        //关闭通道
                        matrix[29][28] = Integer.MAX_VALUE;
                    }
                }

            }
            else{
                //分为三种情况
                //1. 下装载区agv足够，
                //2. 下装载区只有1辆
                //3. 下装载区无agv

                if(downAgvInfoList.size() >= 2){
                    for (int i = 0; i < 2; i++) {
                        //找到agv,有底层agv优先分配
                        AgvInfo downAgvFirstSingle = findFirstAgvSingle(downAgvInfoList);
                        AgvInfo downAgvFirstDouble = findFirstAgvDouble(downAgvInfoList);

                        //设置参数
                        setParam(visited,pathWeight,usedNodePoint);

                        //找到最近NodePoint
                        NodePoint src = null;
                        if(downUsedNodePoints.size() == 0) src = downPoint;
                        else src = downUsedNodePoints.get(downUsedNodePoints.size()-1);

                        //找到最短路径
                        int limit = downfirst? 5 : 25;

                        NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,false,limit,"单",upTaskLimit,downTaskLimit);
                        //寻找底层agv最近节点
                        if(temp != null && downAgvFirstSingle != null){
                            //如果找到
                            //1. 加入到返回值
                            resultMap.put(downAgvFirstSingle,temp);
                            downToDown.put(downAgvFirstSingle,temp);
                            //将点位加入到已使用点位
                            usedNodePoint.add(temp);
                            downUsedNodePoints.add(temp);
                            //将该agv出列
                            downAgvInfoList.remove(downAgvFirstSingle);
                        }
                        else{
                            //底层未找到，寻找高层
                            setParam(visited,pathWeight,usedNodePoint);

                            temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,false,limit,"双",upTaskLimit,downTaskLimit);
                            if(temp != null){
                                //如果找到
                                //1. 加入到返回值
                                resultMap.put(downAgvFirstDouble,temp);
                                downToDown.put(downAgvFirstDouble,temp);
                                //2. 加入到已使用点位
                                usedNodePoint.add(temp);
                                downUsedNodePoints.add(temp);
                                //将该agv出列
                                downAgvInfoList.remove(downAgvFirstDouble);
                            }
                        }

                        //如果找到的是下转载区的第一个任务，设置行数限制
                        if(limit == 5){

                            downTaskLimit = Integer.parseInt(downUsedNodePoints.get(0).getShelfs().get(0).getLocation().split("-")[1]);
                            System.out.println("下转载区第一个任务的行数是 " + downTaskLimit);
                        }
                        downfirst = false;
                    }
                }
                else if (downAgvInfoList.size() == 1){
                    //差一辆，从上装载区调用一辆
                    //找到唯一的agv
                    AgvInfo agv = downAgvInfoList.get(0);

                    setParam(visited,pathWeight,usedNodePoint);

                    int limit = downfirst? 5:25;

                    NodePoint src = null;
                    if(downUsedNodePoints.size() == 0) src = downPoint;
                    else src = downUsedNodePoints.get(downUsedNodePoints.size()-1);

                    //找到最近NodePoint
                    NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,false,limit,agv.getType(),upTaskLimit,downTaskLimit);

                    //将其加入到返回值
                    resultMap.put(agv,temp);
                    downToDown.put(agv,temp);
                    //加入到已使用点位
                    usedNodePoint.add(temp);
                    downUsedNodePoints.add(temp);
                    //agv出列
                    downAgvInfoList.remove(agv);

                    //如果找到的是下转载区的第一个任务，设置行数限制
                    if(limit == 5){

                        downTaskLimit = Integer.parseInt(downUsedNodePoints.get(0).getShelfs().get(0).getLocation().split("-")[1]);
                        System.out.println("下转载区第一个任务的行数是 " + downTaskLimit);
                    }

                    // 从上装载区调用一辆agv
                    agv = upAgvInfoList.get(0);

                    // 关闭通道
                    matrix[57][30] = Integer.MAX_VALUE;

                    if(upToDownNodePoints.size() == 0) src = upPoint;
                    else src = upToDownNodePoints.get(upToDownNodePoints.size()-1);

                    setParam(visited,pathWeight,usedNodePoint);

                    temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,true,25,agv.getType(),-1,26);

                    //将其加入到返回值
                    resultMap.put(agv,temp);
                    upToDown.put(agv,temp);
                    //加入到已使用点位
                    usedNodePoint.add(temp);
                    upToDownNodePoints.add(temp);
                    //agv出列
                    upAgvInfoList.remove(agv);

                    //打开通道
                    matrix[57][30] = 40.2881;

                }
                else if(downAgvInfoList.size() == 0){
                    //下方agv数量为0
                    //从上装载区调用两个agv
                    // 关闭通道
                    matrix[57][30] = Integer.MAX_VALUE;
                    for (int i = 0; i < 2; i++) {

                        setParam(visited,pathWeight,usedNodePoint);

                        AgvInfo agv = upAgvInfoList.get(0);

                        NodePoint src = null;
                        if(upToDownNodePoints.size() == 0) src = upPoint;
                        else src = upToDownNodePoints.get(upToDownNodePoints.size()-1);

                        //找到NodePoint
                        NodePoint temp = shortestNodePoint(src,src,visited,0,Double.MAX_VALUE,pathWeight,true,25,agv.getType(),-1,26);

                        //将其加入到返回值
                        resultMap.put(agv,temp);
                        upToDown.put(agv,temp);
                        //加入到已使用点位
                        usedNodePoint.add(temp);
                        upToDownNodePoints.add(temp);
                        //agv出列
                        upAgvInfoList.remove(agv);
                    }
                    //打开通道
                    matrix[57][30] = 40.2881;
                }
            }
            upTask = !upTask;
        }

        sblhw.put("uptoup",upToUP);
        sblhw.put("uptodown",upToDown);
        sblhw.put("downtodown",downToDown);
        sblhw.put("downtoup",downToUp);

        return sblhw;
    }
    //第一阶段剩余任务节点推荐
    public NodePoint findSecondStageShort(AgvInfo agv,boolean isup,NodePoint[] a1, double[][] matrix, HashMap<NodePoint, Integer> hm){

        this.a1 = a1;
        this.matrix = matrix;
        this.hm = hm;

        //寻找最近NodePoint

        //设置srcNodePoint
        NodePoint srcNodePoint = null;
        //srcNodePoint为上装载区
        if(isup){
            srcNodePoint = a1[0];
        }else {
            srcNodePoint = a1[115];
        }

        //设置visited数组并置为false
        boolean visited[] = new boolean[a1.length];
        for (int i = 0; i < a1.length; i++) {
            visited[i] = false;
        }

        //设置curWeight数组，并置为0
        double[] pathWeight = new double[a1.length];
        for (int i = 0; i < a1.length; i++) {
            pathWeight[i] = 0;
        }
        return shortestNodePoint(srcNodePoint,srcNodePoint,visited,0,Double.MAX_VALUE,pathWeight,isup,25, agv.getType(),-1,26);


    }

    /**
     * 获取相邻节点
     */
    public ArrayList<NodePoint> getConnectedNodes(NodePoint src) {
        ArrayList<NodePoint> connectedPoints = new ArrayList<>();
        int temp = hm.get(src);
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[temp][i] != Integer.MAX_VALUE){
                connectedPoints.add(a1[i]);
            }
        }

        return connectedPoints;
    }

    /*
     * type:agv类型
     * limit:最远距离
     * isup:判断是否为上装载区
     * pathWeight:记录路径weight
     * */

    public NodePoint shortestNodePoint(NodePoint src,NodePoint nowPoint,boolean[] visited,double curWeight,double minWeight,double[] pathWeight,boolean isup,int limit,String type,int upTaskLimit,int downTaskLimit){


        //如果已经访问，直接return
        //若src等于当前节点，未出发，不判断visited
        if(visited[hm.get(nowPoint)] && !(src==nowPoint)) {
            if(curWeight >= pathWeight[hm.get(nowPoint)]){
                return null;
            }else{
                return nowPoint;
            }

        }

        //如果当前weight已经大于最小weight 进行剪枝
        if(curWeight >= minWeight) return null;

        //将当前节点设为已访问
        visited[hm.get(nowPoint)] = true;
        pathWeight[hm.get(nowPoint)] = curWeight;

        NodePoint tempPoint = null;

        //非货架节点或未出发,返回下个节点结果
        if(nowPoint.getShelfs() == null || src==nowPoint){
            for (NodePoint connectedNode : getConnectedNodes(nowPoint)) {
                //返回下个节点的结果
                NodePoint nextPoint = shortestNodePoint(src,connectedNode, visited, curWeight + matrix[hm.get(nowPoint)][hm.get(connectedNode)], minWeight, pathWeight, isup,limit,type,upTaskLimit,downTaskLimit);
                //判断下个节点返回结果是否为null
                if(nextPoint != null){
                    //若tempPoint为null或weigth更小，更新tempPoint
                    if(tempPoint==null || pathWeight[hm.get(nextPoint)] < minWeight){
                        minWeight = pathWeight[hm.get(nextPoint)];
                        tempPoint = nextPoint;
                    }
                }
            }
        }

        //货架节点
        else{
            //得到该货架节点的货架列表
            List<ShelfInfo> shelfs = nowPoint.getShelfs();
            //遍历货架列表
            for (ShelfInfo shelf : shelfs) {
                //若超出limit 进行剪枝
                //上装载区情况
                if(isup && Integer.parseInt(shelf.getLocation().split("-")[1]) >limit) return null;
                //下装载区情况
                if(!isup && (maxDown - Integer.parseInt(shelf.getLocation().split("-")[1])) > limit) return null;

                //若行号超过限制
                if(isup && Integer.parseInt(shelf.getLocation().split("-")[1]) < upTaskLimit) return null;

                if(!isup && Integer.parseInt(shelf.getLocation().split("-")[1]) > downTaskLimit) return null;

                //底层货架
                if(type.equals("单")){
                    if(shelf.getLayer()==1 && shelf.getState()){
                        if(curWeight < minWeight){
                            return nowPoint;
                        }
                    }
                }
                //高层货架
                else{
                    if((shelf.getLayer() == 2 || shelf.getLayer() == 3) && shelf.getState()) return nowPoint;
                }
            }

            //若都没找到
            for (NodePoint connectedNode : getConnectedNodes(nowPoint)) {
                //返回下个节点的结果
                NodePoint nextPoint = shortestNodePoint(src,connectedNode, visited, curWeight + matrix[hm.get(nowPoint)][hm.get(connectedNode)], minWeight, pathWeight, isup,limit,type,upTaskLimit,downTaskLimit);
                //判断下个节点返回结果是否为null
                if(nextPoint != null){
                    //若tempPoint为null或weigth更小，更新tempPoint
                    if(tempPoint==null || pathWeight[hm.get(nextPoint)] < minWeight){
                        minWeight = pathWeight[hm.get(nextPoint)];
                        tempPoint = nextPoint;
                    }
                }
            }
        }
        return tempPoint;
    }

    public void setParam(boolean[] visited,double[] pathWeight,ArrayList<NodePoint> usedNodePoint){
        for (int i = 0; i < a1.length; i++) {
            visited[i] = false;
        }
        for (int i = 0; i < a1.length; i++) {
            pathWeight[i] = 0;
        }
        for (NodePoint nodePoint : usedNodePoint) {
            visited[hm.get(nodePoint)] = true;
        }
    }

    public AgvInfo findFirstAgvSingle(List<AgvInfo> agvInfoList){
        for (AgvInfo agvInfo : agvInfoList) {
            if(agvInfo.getType().equals("单")) return agvInfo;
        }
        return null;
    }

    public AgvInfo findFirstAgvDouble(List<AgvInfo> agvInfoList){
        for (AgvInfo agvInfo : agvInfoList) {
            if(agvInfo.getType().equals("双")) return agvInfo;
        }
        return null;
    }
}
