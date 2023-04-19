import edu.szu.AgvService;
import edu.szu.ShelfService;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestShelfRecommendController {

    @Autowired
    private static ShelfService shelfService;

    @Autowired
    private static AgvService agvService;

    static NodePoint[] a1;
    static double[][] matrix;
    static HashMap<NodePoint,Integer> hm;
    static int maxDown = 25;

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
        List<AgvInfo> downAgvInfoList1 = downReprint.getAgvInfoList();

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
        //获取上装载区第一个底层agv
        for (AgvInfo agvInfo : upAgvInfoList) {
            if(agvInfo.getType().equals("单") && downAgvFirstSingle==null){
                downAgvFirstSingle = agvInfo;
            }
            if(agvInfo.getType().equals("双") && downAgvFirstDouble==null){
                downAgvFirstDouble = agvInfo;
            }
        }

        //上装载区存在底层agv->寻找最近货架
        if(upAgvFirstSingle != null || upAgvFirstDouble !=null){
            List<NodePoint> up_lowpath = null;
            List<NodePoint> up_highpath = null;
            boolean[] visited = new boolean[a1.length];
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }
            //最短路径 DFS
            if(upAgvFirstSingle == null) {
                up_lowpath = getShortestPath(upPoint, visited, "单", 5, 0, new ArrayList<NodePoint>(), new ArrayList<NodePoint>(), Double.MAX_VALUE);
            }
            //未找到最短路径 并且存在高层agv
            if(up_lowpath == null && upAgvFirstDouble !=null){

                up_highpath = getShortestPath(upPoint, visited, "双", 5, 0, new ArrayList<NodePoint>(), new ArrayList<NodePoint>(), Double.MAX_VALUE);
            }
            System.out.println(up_lowpath);
            System.out.println(up_highpath);
        }

        //下装载区存在底层agv->寻找最近货架
        if(downAgvFirstSingle != null || downAgvFirstDouble != null){
            List<NodePoint> down_lowpath = null;
            List<NodePoint> down_highpath = null;
            boolean[] visited = new boolean[a1.length];
            for (int i = 0; i < a1.length; i++) {
                visited[i] = false;
            }
            //最短路径 DFS
            if(downAgvFirstSingle == null){
                down_lowpath = getShortestPath(upPoint, visited, "单", 5, 0, new ArrayList<NodePoint>(), new ArrayList<NodePoint>(), Double.MAX_VALUE);
            }

            //未找到最短路径 并且存在高层agv
            if(down_lowpath == null && upAgvFirstDouble !=null){

                down_highpath = getShortestPath(upPoint, visited, "双", 5, 0, new ArrayList<NodePoint>(), new ArrayList<NodePoint>(), Double.MAX_VALUE);
            }
            System.out.println(down_lowpath);
            System.out.println(down_highpath);
        }



    }

    /**
     * 获取相邻节点
     */
    public static ArrayList<NodePoint> getConnectedNodes(NodePoint src) {
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
    * */
    static List<NodePoint> getShortestPath(NodePoint src, boolean[] visited,String type,int limit,double weight,List<NodePoint> curPath,List<NodePoint> shortestPath,double shortestPathWeight){
        if(visited[hm.get(src)] == true) return null;
        //如果当前weight已经大于最小weight 进行剪枝
        if(weight>shortestPathWeight)
            visited[hm.get(src)] = true;
        //非货架节点
        if(src.getShelfs() == null){
            for (NodePoint connectedNode : getConnectedNodes(src)) {
                curPath.add(connectedNode);
                getShortestPath(connectedNode,visited,type,limit,weight+ matrix[hm.get(src)][hm.get(connectedNode)],curPath,shortestPath,shortestPathWeight);
            }
        }
        //货架节点
        else{
            List<ShelfInfo> shelfs = src.getShelfs();
            Integer areaId = src.getArea().getAreaId();
            for (ShelfInfo shelf : shelfs) {

                if(areaId == 0 && Integer.parseInt(shelf.getLocation().split("-")[1]) >limit) return shortestPath;
                if(areaId == 1 && (maxDown - Integer.parseInt(shelf.getLocation().split("-")[1])) > limit) return shortestPath;
                //底层货架
                if(type.equals("单")){
                    if(shelf.getLayer()==1){
                        if(weight < shortestPathWeight){
                            shortestPath = curPath;
                            shortestPathWeight = weight;
                        }
                    }
                }
                //高层货架
                else{
                    if(type.equals("双")){
                        if(shelf.getLayer()==2){
                            if(weight < shortestPathWeight){
                                shortestPath = curPath;
                                shortestPathWeight = weight;
                            }
                        }
                    }
                    //第三层货架
                    if(type.equals("双")){
                        if(shelf.getLayer()==3){
                            if(weight < shortestPathWeight){
                                shortestPath = curPath;
                                shortestPathWeight = weight;
                            }
                        }
                    }
                }
            }
            for (NodePoint connectedNode : getConnectedNodes(src)) {
                curPath.add(connectedNode);
                getShortestPath(connectedNode,visited,type,limit,weight+ matrix[hm.get(src)][hm.get(connectedNode)],curPath,shortestPath,shortestPathWeight);
            }
        }
        return shortestPath;
    }

}
