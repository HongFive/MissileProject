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

        //上装载区存在底层agv或高层agv->寻找最近货架
        if(upAgvFirstSingle != null || upAgvFirstDouble !=null){
            NodePoint up_low = null;
            NodePoint up_high = null;
            //设置visited数组
            boolean[] visited = new boolean[a1.length];
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }

            //设置pathWeight数组
            double[] pathWeight = new double[a1.length];
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }

            //底层agv DFS
            up_low = shortestNodePoint(upPoint,visited,0,Double.MAX_VALUE,pathWeight,true,5,"单");

            //未找到最短路径，并且存在高层agv
            //重置visted和pathWeight
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }
            if(up_low != null && upAgvFirstDouble!=null){
                up_high = shortestNodePoint(upPoint,visited,0,Double.MAX_VALUE,pathWeight,true,5,"双");
            }

            System.out.println("up_low is " + up_low);
            System.out.println("up_high is " + up_high);

        }

        //下装载区存在底层agv->寻找最近货架
        if(downAgvFirstSingle != null || downAgvFirstDouble != null){
            NodePoint down_low = null;
            NodePoint down_high = null;
            //设置visited数组
            boolean[] visited = new boolean[a1.length];
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }

            //设置pathWeight数组
            double[] pathWeight = new double[a1.length];
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }

            //底层agv DFS
            down_low = shortestNodePoint(upPoint,visited,0,Double.MAX_VALUE,pathWeight,false,5,"单");

            //未找到最短路径，并且存在高层agv
            //重置visted和pathWeight
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }
            for (int i = 0; i < a1.length; i++) {
                pathWeight[i] = 0;
            }
            if(down_low != null && upAgvFirstDouble!=null){
                down_high = shortestNodePoint(upPoint,visited,0,Double.MAX_VALUE,pathWeight,false,5,"双");
            }

            System.out.println("down_low is " + down_low);
            System.out.println("down_high is " + down_high);

        }

        //寻找第一阶段 第234...tasks
        //建立路径栈
        Stack<NodePoint> nodePoints = new Stack<>();


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

    public NodePoint shortestNodePoint(NodePoint src,boolean[] visited,double curWeight,double minWeight,double[] pathWeight,boolean isup,int limit,String type){

        //如果已经访问，直接return
        if(visited[hm.get(src)]) return null;

        //如果当前weight已经大于最小weight 进行剪枝
        if(curWeight >= minWeight) return null;

        //将当前节点设为已访问
        visited[hm.get(src)] = true;
        pathWeight[hm.get(src)] = curWeight;

        NodePoint tempPoint = null;

        //非货架节点
        if(src.getShelfs() == null){
            for (NodePoint connectedNode : getConnectedNodes(src)) {
                //返回下个节点的结果
                NodePoint nextPoint = shortestNodePoint(connectedNode, visited, curWeight + matrix[hm.get(src)][hm.get(connectedNode)], minWeight, pathWeight, isup,limit,type);
                //判断下个节点返回结果是否为null
                if(nextPoint != null){
                    //若tempPoint为null或weigth更小，更新tempPoint
                    if(tempPoint==null || pathWeight[hm.get(nextPoint)] < pathWeight[hm.get(src)]){
                        tempPoint = nextPoint;
                    }
                }
            }
        }

        //货架节点
        else{
            //得到该货架节点的货架列表
            List<ShelfInfo> shelfs = src.getShelfs();
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
                            return src;
                        }
                    }
                }
                //高层货架
                else{
                    if(shelf.getLayer() == 2 || shelf.getLayer() == 3) return src;
                }
            }
        }
        return tempPoint;
    }
}
