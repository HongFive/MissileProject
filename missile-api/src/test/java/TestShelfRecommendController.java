import edu.szu.AgvService;
import edu.szu.ShelfService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class TestShelfRecommendController {

    @Autowired
    private static ShelfService shelfService;

    @Autowired
    private static AgvService agvService;

    NodePoint[] a1;
    double[][] matrix;
    HashMap<NodePoint,Integer> hm;
    int maxDown = 25;

    //上装载区从左边道运行路径长度
    double upLeftPathWeight = 1122;

    //下装载区从左边道路运行路径长度
    double downLeftPathWeight = 1005;

    public void taskRecommend(NodePoint[] a1, double[][] matrix, HashMap<NodePoint, Integer> hm){
        /**
         * 分为两步，
         * 第一步找到最近的节点
         * 第二步在第一个节点回去的路径不能有节点阻拦
         * 输入：NodePoint[]（所有节点信息），Matrix[][]（节点距离），Map（用于查找界定啊）
         * 输出：推荐的货架信息 一个list
         */

        this.a1 = a1;
        this.matrix = matrix;
        this.hm = hm;


        //获取初始节点
        NodePoint upPoint = a1[0];
        NodePoint downPoint = a1[a1.length-1];

        //上下装载区
        ReprintArea upReprint = upPoint.getArea();
        ReprintArea downReprint = downPoint.getArea();

        //上下装载区agv列表
        List<AgvInfo> upAgvInfoList = upReprint.getAgvInfoList();
        List<AgvInfo> downAgvInfoList = downReprint.getAgvInfoList();

        //获取上装载区最近Agv
        AgvInfo upAgvFirstSingle = null;
        AgvInfo upAgvFirstDouble = null;
        //获取上装载区第一个底层agv
        for (AgvInfo agvInfo : upAgvInfoList) {
            if(agvInfo.getType().equals("单") && upAgvFirstSingle==null){
                upAgvFirstSingle = agvInfo;
            }
            if(agvInfo.getType().equals("双") && upAgvFirstDouble==null){
                upAgvFirstDouble = agvInfo;
            }
        }

        //获取下装载区最近Agv
        AgvInfo downAgvFirstSingle = null;
        AgvInfo downAgvFirstDouble = null;
        //获取下装载区第一个底层agv
        for (AgvInfo agvInfo : downAgvInfoList) {
            if(agvInfo.getType().equals("单") && downAgvFirstSingle==null){
                downAgvFirstSingle = agvInfo;
            }
            if(agvInfo.getType().equals("双") && downAgvFirstDouble==null){
                downAgvFirstDouble = agvInfo;
            }
        }

        //上下装载区第一个任务NodePoint
        NodePoint upFirstNodePoint = null;
        NodePoint downFirstNodePoint = null;


        boolean[] visited = new boolean[a1.length];
        double[] pathWeight = new double[a1.length];

        //上装载区存在底层agv或高层agv->寻找最近货架
        if(upAgvFirstSingle != null || upAgvFirstDouble !=null){
            NodePoint up_low = null;
            NodePoint up_high = null;
            //设置visited数组
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }

            //设置pathWeight数组
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }

            //底层agv DFS
            up_low = shortestNodePoint(upPoint,upPoint,visited,0,Double.MAX_VALUE,pathWeight,true,5,"单");

            //找到最短路径
            if(up_low != null){
                upFirstNodePoint = up_low;
                //将上装载区第一个底层agv出列
                upAgvInfoList.remove(upAgvFirstSingle);
            }

            //未找到最短路径，并且存在高层agv

            if(up_low == null && upAgvFirstDouble!=null){
                //重置visted和pathWeight
                for (int i = 0; i < a1.length; i++) {
                    visited[i] = false;
                }
                for (int i = 0; i < a1.length; i++) {
                    pathWeight[i] = 0;
                }
                up_high = shortestNodePoint(upPoint,upPoint,visited,0,Double.MAX_VALUE,pathWeight,true,5,"双");
                //上装载区最短路径为高层agv最短路径
                if(up_high !=null){
                    upFirstNodePoint = up_high;
                    //上装载区第一个高层agv出列
                    upAgvInfoList.remove(upAgvFirstDouble);
                }
            }

            System.out.println("up_low is " + up_low);
            System.out.println("up_high is " + up_high);
            System.out.println("upFirstNodePoint is " + upFirstNodePoint);

        }

        //下装载区存在底层agv->寻找最近货架
        if(downAgvFirstSingle != null || downAgvFirstDouble != null){
            NodePoint down_low = null;
            NodePoint down_high = null;
            //设置visited数组
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }

            //设置pathWeight数组
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }

            //底层agv DFS
            down_low = shortestNodePoint(downPoint,downPoint,visited,0,Double.MAX_VALUE,pathWeight,false,5,"单");

            //找到最短路径
            if(down_low != null){
                downFirstNodePoint = down_low;
                //将下层第一个底层agv出列
                downAgvInfoList.remove(downAgvFirstSingle);
            }

            //未找到最短路径，并且存在高层agv

            if(down_low == null && upAgvFirstDouble!=null){
                //重置visted和pathWeight
                for (int i = 0; i < a1.length; i++) {
                    visited[i] = false;
                }
                for (int i = 0; i < a1.length; i++) {
                    pathWeight[i] = 0;
                }
                down_high = shortestNodePoint(downPoint,downPoint,visited,0,Double.MAX_VALUE,pathWeight,false,5,"双");
                if(down_high != null){
                    downFirstNodePoint = down_high;
                    //将下层第一个高层agv出列
                }
            }

            System.out.println("down_low is " + down_low);
            System.out.println("down_high is " + down_high);
            System.out.println("downFirstNodePoint is " + downFirstNodePoint);

        }

        //寻找第一阶段 第234...tasks
        //建立上装载区节点栈
        Stack<NodePoint> upNodePoints = new Stack<>();

        //建立下装载区节点栈
        Stack<NodePoint> downNodePoints = new Stack<>();

        //已预占用点位
        ArrayList<NodePoint> upUsedNodePoints = new ArrayList<>();
        ArrayList<NodePoint> downUsedNodePoints = new ArrayList<>();
        upUsedNodePoints.add(upFirstNodePoint);
        downUsedNodePoints.add(downFirstNodePoint);


        //为上装载区剩余agv分配任务
        //直到所有agv分配完任务
        while(!upAgvInfoList.isEmpty()){
            upAgvFirstSingle = null;
            upAgvFirstDouble = null;

            //找到第一个底层agv
            for (AgvInfo agvInfo : upAgvInfoList) {
                if(agvInfo.getType().equals("单")){
                    upAgvFirstSingle = agvInfo;
                    //找到底层agv，退出循环
                    continue;
                }
            }
            for (int i = 0; i < visited.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < pathWeight.length; i++) {
                pathWeight[i] = 0;
            }
            NodePoint srcPoint = upUsedNodePoints.get(upUsedNodePoints.size()-1);
            if(upAgvFirstSingle != null){
                NodePoint temp = shortestNodePoint(srcPoint, srcPoint, visited, 0, Double.MAX_VALUE, pathWeight, true, 25, "单");
                //节点入列
                upUsedNodePoints.add(temp);
                //将agv移除
                upAgvInfoList.remove(upAgvFirstSingle);
                continue;
            }

            //高层agv
            //找到第一个高层agv
            for (AgvInfo agvInfo : upAgvInfoList) {
                if(agvInfo.getType().equals("双")){
                    upAgvFirstDouble = agvInfo;
                    //找到底层agv，退出循环
                    continue;
                }
            }
            NodePoint temp = shortestNodePoint(srcPoint, srcPoint, visited, 0, Double.MAX_VALUE, pathWeight, true, 25, "双");
            //节点入列
            upUsedNodePoints.add(temp);
            //将agv移除
            upAgvInfoList.remove(upAgvFirstSingle);
            continue;
        }

        //为下装载区剩余agv分配任务
        //直到所有agv分配完任务
        while(!downAgvInfoList.isEmpty()){
            downAgvFirstSingle = null;
            downAgvFirstDouble = null;

            //找到第一个底层agv
            for (AgvInfo agvInfo : downAgvInfoList) {
                if(agvInfo.getType().equals("单")){
                    downAgvFirstSingle = agvInfo;
                    //找到底层agv，退出循环
                    continue;
                }
            }
            for (int i = 0; i < visited.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < pathWeight.length; i++) {
                pathWeight[i] = 0;
            }
            NodePoint srcPoint = downUsedNodePoints.get(downUsedNodePoints.size()-1);
            if(downAgvFirstSingle != null){
                NodePoint temp = shortestNodePoint(srcPoint, srcPoint, visited, 0, Double.MAX_VALUE, pathWeight, false, 25, "单");
                //节点入列
                downUsedNodePoints.add(temp);
                //将agv移除
                downAgvInfoList.remove(downAgvFirstSingle);
                continue;
            }

            //高层agv
            //找到第一个高层agv
            for (AgvInfo agvInfo : downAgvInfoList) {
                if(agvInfo.getType().equals("双")){
                    downAgvFirstDouble = agvInfo;
                    //找到底层agv，退出循环
                    continue;
                }
            }
            NodePoint temp = shortestNodePoint(srcPoint, srcPoint, visited, 0, Double.MAX_VALUE, pathWeight, false, 25, "双");
            //节点入列
            downUsedNodePoints.add(temp);
            //将agv移除
            downAgvInfoList.remove(downAgvFirstSingle);
            continue;
        }

    }

    /**
     * 获取相邻节点
     */
    public ArrayList<NodePoint> getConnectedNodes(NodePoint src) {
        ArrayList<NodePoint> connectedPoints =null;
        int temp = hm.get(src);
        for (int i = 0; i < matrix[0].length; i++) {
            if (matrix[temp][i] != Integer.MIN_VALUE){
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

    public NodePoint shortestNodePoint(NodePoint src,NodePoint nowPoint,boolean[] visited,double curWeight,double minWeight,double[] pathWeight,boolean isup,int limit,String type){


        //如果已经访问，直接return
        //若src等于当前节点，未出发，不判断visited
        if(visited[hm.get(nowPoint)] && !(src==nowPoint)) return null;

        //如果当前weight已经大于最小weight 进行剪枝
        if(curWeight >= minWeight) return null;

        //将当前节点设为已访问
        visited[hm.get(nowPoint)] = true;
        pathWeight[hm.get(nowPoint)] = curWeight;

        NodePoint tempPoint = null;

        //非货架节点或未出发
        if(nowPoint.getShelfs() == null || src==nowPoint){
            for (NodePoint connectedNode : getConnectedNodes(nowPoint)) {
                //返回下个节点的结果
                NodePoint nextPoint = shortestNodePoint(src,connectedNode, visited, curWeight + matrix[hm.get(nowPoint)][hm.get(connectedNode)], minWeight, pathWeight, isup,limit,type);
                //判断下个节点返回结果是否为null
                if(nextPoint != null){
                    //若tempPoint为null或weigth更小，更新tempPoint
                    if(tempPoint==null || pathWeight[hm.get(nextPoint)] < pathWeight[hm.get(nowPoint)]){
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

                //底层货架
                if(type.equals("单")){
                    if(shelf.getLayer()==1){
                        if(curWeight < minWeight){
                            return nowPoint;
                        }
                    }
                }
                //高层货架
                else{
                    if(shelf.getLayer() == 2 || shelf.getLayer() == 3) return nowPoint;
                }
            }
        }
        return tempPoint;
    }
}
