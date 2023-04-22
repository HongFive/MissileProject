package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.vo.NodePoint;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AgvThreadPool {

    public static final Integer Go=1;
    public static final Integer Back=0;
    private static class ThreadNode{
        AgvInfo agv;
        List<NodePoint> path;
        NodePoint target;

        Integer state;

        public ThreadNode(AgvInfo agv, List<NodePoint> path, NodePoint target,int state) {
            this.agv = agv;
            this.path = path;
            this.target = target;
            this.state=state;
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

        public Integer getState() {
            return state;
        }

        public void setState(Integer state) {
            this.state = state;
        }
    }
    Stack<ThreadNode> upTransStack=new Stack<>();
    public void LetsGo(MyGraph graph, LinkedHashMap<AgvInfo,NodePoint> targets, LinkedHashMap<AgvInfo,List<NodePoint>> paths, int areaId){


        List<ThreadNode> movingAgvs=new ArrayList<>();

        //分配agv任务
        for (Map.Entry<AgvInfo,NodePoint> entry: targets.entrySet()){
            ThreadNode temp=new ThreadNode(entry.getKey(),null,entry.getValue(),Go);
            movingAgvs.add(temp);
            graph.arrayV[areaId].getArea().getAgvInfoList().remove(entry.getKey());//出动的agv从转载区移出
        }

        //设定回程顺序
        for (Map.Entry<AgvInfo,List<NodePoint>> entry: paths.entrySet()){
            for (ThreadNode temp:movingAgvs){
                if (entry.getKey()==temp.agv){
                    temp.setPath(entry.getValue());
                }
            }
        }
        for (int i = 1; i < movingAgvs.size(); i++) {
            upTransStack.push(movingAgvs.get(i));
        }
        upTransStack.push(movingAgvs.get(0));

        //启动agv
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(movingAgvs.size());
        for (int i = 0; i < movingAgvs.size(); i++) {
//            final int index = i;
            ThreadNode agv=movingAgvs.get(i);
            executorService.schedule(() -> {
                for (NodePoint nodePoint:agv.getPath()) {
                    try {
                            //进行装载
                            if (agv.getAgv().getType().equals("单")&&nodePoint==agv.target){
                                Timestamp beginTime=new Timestamp(System.currentTimeMillis());
                                System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+nodePoint.getShelfs().get(0).getLocation()+"  action:"+"transplant"+"  beginTime:"+beginTime);
                                Thread.sleep(5*60*1000);
                                while (!upTransStack.isEmpty()&&upTransStack.peek()!=agv) Thread.sleep(1000);
                                //装载完成
                                upTransStack.pop();
                                agv.setState(Back);
                                Timestamp endTime=new Timestamp(System.currentTimeMillis());
                                System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+nodePoint.getShelfs().get(0).getLocation()+"  action:"+"transplant"+"  endTime:"+endTime);
                            }else if (agv.getAgv().getType().equals("双")&&nodePoint==agv.target){
                                Timestamp beginTime=new Timestamp(System.currentTimeMillis());
                                System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+nodePoint.getShelfs().get(0).getLocation()+"  action:"+"transplant"+"  beginTime:"+beginTime);
                                Thread.sleep(7*60*1000);
                                while (!upTransStack.isEmpty()&&upTransStack.peek()!=agv) Thread.sleep(1000);
                                //装载完成
                                upTransStack.pop();
                                agv.setState(Back);
                                Timestamp endTime=new Timestamp(System.currentTimeMillis());
                                System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+nodePoint.getShelfs().get(0).getLocation()+"  action:"+"transplant"+"  endTime:"+endTime);
                            }else if (agv.getState().equals(Back)){//准备返程点位逆序遍历
                                List<Map.Entry<String, Double>> list = new ArrayList<>(nodePoint.getPointList().entrySet());
                                Collections.reverse(list);
                                for (Map.Entry<String,Double> entry:list){
                                    Timestamp beginTime=new Timestamp(System.currentTimeMillis());
                                    System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+entry.getKey()+"  beginTime:"+beginTime);
                                    double sleepTime=(entry.getValue()/0.8)*1000;
                                    Thread.sleep((long) sleepTime);
                                    Timestamp endTime=new Timestamp(System.currentTimeMillis());
                                    System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+entry.getKey()+"  endTime:"+endTime);
                                }
                            }else {//正常行进
                                for (Map.Entry<String,Double> entry: nodePoint.getPointList().entrySet()){
                                    Timestamp beginTime=new Timestamp(System.currentTimeMillis());
                                    System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+entry.getKey()+"  beginTime:"+beginTime);
                                    double sleepTime=(entry.getValue()/0.8)*1000;
                                    Thread.sleep((long) sleepTime);
                                    Timestamp endTime=new Timestamp(System.currentTimeMillis());
                                    System.out.println("agvname:"+agv.getAgv().getAgvName()+"  point:"+entry.getKey()+"  endTime:"+endTime);
                                }
                            }
                        graph.arrayV[areaId].getArea().getAgvInfoList().add(agv.getAgv());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, 25 * i, TimeUnit.SECONDS);
        }
        executorService.shutdown();
    }
}
