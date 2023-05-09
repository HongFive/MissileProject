package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.TaskPlanResultPointInfo;
import edu.szu.pojo.ZktTask;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ResultBody;

import java.sql.Timestamp;
import java.util.*;

public class AgvPathPlan {

    public class ThreadNode{
        AgvInfo agv;
        List<NodePoint> path;
        NodePoint target;

        ShelfInfo shelf;
        Integer state;

        Boolean isStart;

        public ThreadNode(AgvInfo agv, List<NodePoint> path, NodePoint target,ShelfInfo shelf,int state,boolean isStart) {
            this.agv = agv;
            this.path = path;
            this.target = target;
            this.shelf=shelf;
            this.state=state;
            this.isStart=isStart;
        }

        public Boolean getStart() {
            return isStart;
        }

        public void setStart(Boolean start) {
            isStart = start;
        }

        public AgvInfo getAgv() {
            return agv;
        }

        public void setAgv(AgvInfo agv) {
            this.agv = agv;
        }

        public List<NodePoint> getPath() {
            return path;
        }

        public void setPath(List<NodePoint> path) {
            this.path = path;
        }

        public NodePoint getTarget() {
            return target;
        }

        public void setTarget(NodePoint target) {
            this.target = target;
        }

        public ShelfInfo getShelf() {
            return shelf;
        }

        public void setShelf(ShelfInfo shelf) {
            this.shelf = shelf;
        }

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }
    }

    public static final Integer Go=1;
    public static final Integer Back=0;

    Stack<ThreadNode> rightRoadStack=new Stack<>();
    Stack<ThreadNode> leftRoadStack=new Stack<>();

    public List<List<TaskPlanResultPointInfo>> AgvPathDetail(ZktTask task, MyGraph graph, List<ResultBody> resultBodyList, int upRunNum, int downRunNum){

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

        //设定上停放区出发时间
        List<Timestamp> upBeginTimes=new ArrayList<>();
        Timestamp tempTime= new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < upAgvOne.size(); i++) {
            if (i==0) upBeginTimes.add(tempTime);
            else {
                Timestamp addTime=new Timestamp(tempTime.getTime()+(60*1000));
                upBeginTimes.add(addTime);
                tempTime=addTime;
            }//每间隔60s发车一辆
        }


        //设定下停放区出发时间
        List<Timestamp> downBeginTimes=new ArrayList<>();
        tempTime= new Timestamp(System.currentTimeMillis());
        for (int i = 0; i < downAgvOne.size(); i++) {
            if (i==0) downBeginTimes.add(tempTime);
            else {
                Timestamp addTime=new Timestamp(tempTime.getTime()+(60*1000));
                downBeginTimes.add(addTime);
                tempTime=addTime;
            }//每间隔60s发车一辆
        }

        //记录所有结果
        LinkedHashMap<ThreadNode,List<TaskPlanResultPointInfo>> resultPlanList=new LinkedHashMap<>();

        //上停放区出发车辆
        LinkedHashMap<ThreadNode,Timestamp> upAfterLoadTimes=new LinkedHashMap<>();
        for (int i = 0; i < upAgvOne.size(); i++) {
            ThreadNode agv=upAgvOne.get(i);
            Timestamp begintime=upBeginTimes.get(i);
            List<TaskPlanResultPointInfo> resForOne=new ArrayList<>();
            for (NodePoint np:agv.getPath()){
                if (agv.getState().equals(Go)){
                    if (np.getPathNum().equals(agv.getAgv().getLocation().split("-")[0])&&!agv.getStart()){//从停放区出发
                        agv.setStart(true);
                        Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((10/0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "start",agv.getAgv().getLocation(),begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                        continue;
                    }
                    if (agv.getStart()){//车辆出发到道路上运行
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            if (Integer.parseInt(entry.getKey().split("-")[1])<=Integer.parseInt(agv.getAgv().getLocation().split("-")[1])) continue;
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "straight",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        agv.setStart(false);
                        continue;
                    }
                    if (np.getPathNum().equals("8") || np.getPathNum().equals("14")){//遇到弯道进行转弯
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "turn",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getPathNum().equals("3")){//发生平移位置
                        Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((8.2 / 0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "translate","3-5",begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                        continue;
                    }
                    if (np.getId().equals(agv.target.getId())){//进行装载
                         for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                    int loadTime = 5 * 60 * 1000;
                                    if (agv.getShelf().getLayer() == 3) loadTime = 9 * 60 * 1000;
                                    else if (agv.getShelf().getLayer() == 2) loadTime = 7 * 60 * 1000;
                                    Timestamp endtime=new Timestamp(begintime.getTime()+loadTime);
                                    //装载完成
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                            "load",entry.getKey(),begintime,endtime);
                                    resForOne.add(temp);
                                    upAfterLoadTimes.put(agv,endtime);
                                    agv.setState(Back);
                                }
                         continue;
                    }
                    if (np.getPathNum().equals("15")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("22")||
                            np.getPathNum().equals("22")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("15")||
                            np.getPathNum().equals("4")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("9")||
                            np.getPathNum().equals("9")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("4")){
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((8.2 / 0.8)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "translate",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                        Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "straight",entry.getKey(),begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                    }
                }
            }
            resultPlanList.put(agv,resForOne);
        }

        //下停放区出发车辆
        LinkedHashMap<ThreadNode,Timestamp> downAfterLoadTimes=new LinkedHashMap<>();
        for (int i = 0; i < downAgvOne.size(); i++) {
            ThreadNode agv=downAgvOne.get(i);
            Timestamp begintime=downBeginTimes.get(i);
            List<TaskPlanResultPointInfo> resForOne=new ArrayList<>();
            for (NodePoint np:agv.getPath()){
                if (agv.getState().equals(Go)){
                    if (np.getPathNum().equals(agv.getAgv().getLocation().split("-")[0])&&!agv.getStart()){//从停放区出发
                        agv.setStart(true);
                        Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((10/0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "start",agv.getAgv().getLocation(),begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                        continue;
                    }
                    if (agv.getStart()){//车辆出发到道路上运行
                        if (np.getId().equals(graph.arrayV[31].getId())){
                            for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                if (Integer.parseInt(entry.getKey().split("-")[1])>=Integer.parseInt(agv.getAgv().getLocation().split("-")[1])) continue;
                                Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                        "straight",entry.getKey(),begintime,endtime);
                                resForOne.add(temp);
                                agv.setStart(false);
                                begintime=endtime;
                            }
                        }else {
                            for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                                if ((entry.getKey().split("-")[1]).equals(agv.getAgv().getLocation().split("-")[1])) {
                                    Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                                    TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                            "translate",entry.getKey(),begintime,endtime);
                                    resForOne.add(temp);
                                    begintime=endtime;
                                    break;
                                }
                            }
                        }
                        continue;
                    }
                    if (np.getPathNum().equals("5") || np.getPathNum().equals("23")){//遇到弯道进行转弯
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "turn",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getPathNum().equals("16")){//发生平移位置
                        Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((9/ 0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "translate","16-1",begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                        continue;
                    }
                    if (np.getId().equals(agv.target.getId())){//进行装载
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            int loadTime = 5 * 60 * 1000;
                            if (agv.getShelf().getLayer() == 3) loadTime = 9 * 60 * 1000;
                            else if (agv.getShelf().getLayer() == 2) loadTime = 7 * 60 * 1000;
                            Timestamp endtime=new Timestamp(begintime.getTime()+loadTime);
                            //装载完成
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "load",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            downAfterLoadTimes.put(agv,endtime);
                            agv.setState(Back);
                        }
                        break;
                    }
                    if (np.getPathNum().equals("15")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("22")||
                            np.getPathNum().equals("22")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("15")||
                            np.getPathNum().equals("4")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("9")||
                            np.getPathNum().equals("9")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("4")){//货道中发生平移
                        for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                            Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((9 / 0.8)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "translate",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    for (Map.Entry<String, Double> entry : np.getPointList().entrySet()) {
                        Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.8)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "straight",entry.getKey(),begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                    }
                }
            }
            resultPlanList.put(agv,resForOne);
        }

        //回程至上转载区
        long nextWait=0;
        while(!rightRoadStack.isEmpty()){
            ThreadNode agv=rightRoadStack.pop();
            List<TaskPlanResultPointInfo> resForOne=new ArrayList<>();
            Timestamp begintime=upAfterLoadTimes.containsKey(agv)?upAfterLoadTimes.get(agv):downAfterLoadTimes.get(agv);
            boolean flag=false;
            for(NodePoint np:agv.getPath()){
                if (flag&&agv.getState().equals(Back)){//处于转载后，继续更新路径
                    List<Map.Entry<String,Double>> list=new ArrayList<>(np.getPointList().entrySet());
                    Collections.reverse(list);
                    if (!rightRoadStack.isEmpty()){//设置下一个车辆的出发时间（等待该车出发后才有资格出发）
                        if (upAfterLoadTimes.containsKey(rightRoadStack.peek())&&(begintime.getTime()>upAfterLoadTimes.get(rightRoadStack.peek()).getTime())){
                            upAfterLoadTimes.get(rightRoadStack.peek()).setTime(begintime.getTime());
                        } else if (downAfterLoadTimes.containsKey(rightRoadStack.peek())&&(begintime.getTime()>downAfterLoadTimes.get(rightRoadStack.peek()).getTime())) {
                            downAfterLoadTimes.get(rightRoadStack.peek()).setTime(begintime.getTime());
                        }
                    }
                    if (np.getPathNum().equals("15")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("22")|| np.getPathNum().equals("22")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("15")||
                            np.getPathNum().equals("4")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("9")|| np.getPathNum().equals("9")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("4")){
                        //货道中发生平移
                        for (Map.Entry<String, Double> entry : list) {
                            Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((8.2 / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "translate",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getPathNum().equals("8")){
                        Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((8.2 / 0.6)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "translate","8-5",begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                    }
                    if (np.getPathNum().equals("3") || np.getPathNum().equals("16")){
                        for (Map.Entry<String, Double> entry :list) {
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "turn",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getId()==upPrintArea){//转载
                        Timestamp endtime = new Timestamp(begintime.getTime()+8*60*1000);
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "transplant",graph.arrayV[upPrintArea].getPathNum()+"-1",begintime,endtime);
                        resForOne.add(temp);
                        nextWait=endtime.getTime();//若该车还在转载 下一辆车须等待的时间
                        break;
                    }
                    for (Map.Entry<String,Double> entry:list){
                        if (entry.getKey().equals("1-1")&&(nextWait>0)){//前方转载区有车辆在转载，此时需等待
                            long runtime=begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000);
                            if (runtime<nextWait){
                                Timestamp endtime=new Timestamp(nextWait);
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                        "wait",entry.getKey(),begintime,endtime);
                                resForOne.add(temp);
                                begintime=endtime;
                                continue;
                            }
                        }
                        //1.无需等待直接进入转载区 2.正常行驶
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "straight",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;

                    }
                }
                if (np.getId().equals(agv.target.getId())){flag=true;}
            }
            resultPlanList.get(agv).addAll(resForOne);
        }

        //回程至下转载区
        nextWait=0;
        while(!leftRoadStack.isEmpty()){
            ThreadNode agv=leftRoadStack.pop();
            List<TaskPlanResultPointInfo> resForOne=new ArrayList<>();
            Timestamp begintime=upAfterLoadTimes.containsKey(agv)?upAfterLoadTimes.get(agv):downAfterLoadTimes.get(agv);
            boolean flag=false;
            for(NodePoint np:agv.getPath()){
                if (flag&&agv.getState().equals(Back)){//处于转载后，继续更新路径
                    List<Map.Entry<String,Double>> list=new ArrayList<>(np.getPointList().entrySet());
                    Collections.reverse(list);

                    if (!leftRoadStack.isEmpty()){//设置下一个车辆的出发时间（等待该车出发后才有资格出发）
                        if (upAfterLoadTimes.containsKey(leftRoadStack.peek())&&(begintime.getTime()>upAfterLoadTimes.get(leftRoadStack.peek()).getTime())){
                            upAfterLoadTimes.get(leftRoadStack.peek()).setTime(begintime.getTime());
                        } else if (downAfterLoadTimes.containsKey(leftRoadStack.peek())&&(begintime.getTime()>downAfterLoadTimes.get(leftRoadStack.peek()).getTime())) {
                            downAfterLoadTimes.get(leftRoadStack.peek()).setTime(begintime.getTime());
                        }
                    }
                    if (np.getPathNum().equals("15")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("22")|| np.getPathNum().equals("22")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("15")||
                            np.getPathNum().equals("4")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("9")|| np.getPathNum().equals("9")&&agv.getPath().get(agv.getPath().indexOf(np)+1).getPathNum().equals("4")){
                        //货道中发生平移
                        for (Map.Entry<String, Double> entry : list) {
                            Timestamp endtime=new Timestamp(begintime.getTime()+(long) ((9 / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "translate",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getPathNum().equals("23")){
                        Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((9/ 0.6)*1000));
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "translate","23-1",begintime,endtime);
                        resForOne.add(temp);
                        begintime=endtime;
                    }
                    if (np.getPathNum().equals("3") || np.getPathNum().equals("16")){
                        for (Map.Entry<String, Double> entry :list) {
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "turn",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;
                        }
                        continue;
                    }
                    if (np.getId()==downPrintArea){//转载
                        Timestamp endtime = new Timestamp(begintime.getTime()+8*60*1000);
                        TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                "transplant",graph.arrayV[downPrintArea].getPathNum()+"-1",begintime,endtime);
                        resForOne.add(temp);
                        nextWait=endtime.getTime();//若该车还在转载 下一辆车须等待的时间
                        break;
                    }
                    for (Map.Entry<String,Double> entry:list){
                        if (entry.getKey().equals("19-18")&&(nextWait>0)){//前方转载区有车辆在转载，此时需等待
                            long runtime=begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000);
                            if (runtime<nextWait){
                                Timestamp endtime=new Timestamp(nextWait);
                                TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                        "wait",entry.getKey(),begintime,endtime);
                                resForOne.add(temp);
                                begintime=endtime;
                                continue;
                            }
                        }
                        //无需等待直接进入转载区
                            Timestamp endtime = new Timestamp(begintime.getTime()+(long) ((entry.getValue() / 0.6)*1000));
                            TaskPlanResultPointInfo temp=new TaskPlanResultPointInfo(task.getId(),agv.getAgv().getId(),task.getTaskName(),agv.getAgv().getAgvName(),agv.getShelf().getId(),
                                    "straight",entry.getKey(),begintime,endtime);
                            resForOne.add(temp);
                            begintime=endtime;

                    }
                }
                if (np.getId().equals(agv.target.getId())) {flag=true;}
            }
            resultPlanList.get(agv).addAll(resForOne);
        }
        List<List<TaskPlanResultPointInfo>> finalResult=new ArrayList<>();
        for (Map.Entry<ThreadNode,List<TaskPlanResultPointInfo>> entry: resultPlanList.entrySet()){
            finalResult.add(entry.getValue());
        }
        return finalResult;

    }
}
