package edu.szu;


import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 初始化图
 */
public class MyGraph {

    @Autowired
    private static ShelfService shelfService;

    @Autowired
    private static AgvService agvService;

    public static NodePoint[] inintialNode(){

        //agv信息
        List<AgvInfo> agvInfoList = agvService.queryAgvList();

        //货架信息
        List<ShelfInfo> shelfInfoList=shelfService.queryShelfList();

        // TODO 根据agv位置安排转载区
        //转载区信息
        ReprintArea area_1 = new ReprintArea(1,new ArrayList<AgvInfo>()); //上转载区
        ReprintArea area_2 = new ReprintArea(2,new ArrayList<AgvInfo>()); //下转载区

        //初始化节点信息
        NodePoint node_1 = new NodePoint(1,"1",new ArrayList<String>(),null,false,area_1);
        NodePoint node_2 = new NodePoint(2,"2",new ArrayList<String>(),null,false,null);

        ShelfInfo shelf_4_1=new ShelfInfo(1,"1-1",1,"内",false,"4-1");
        ShelfInfo shelf_4_2=new ShelfInfo(1,"1-2",1,"外",false,"4-1");
        List<ShelfInfo> shelfInfos= Arrays.asList(shelf_4_1,shelf_4_2);
        NodePoint node_3=new NodePoint(3,"4",new ArrayList<String>(),shelfInfos,false,null);

        NodePoint node_4=new NodePoint(4,"23",new ArrayList<String>(),null,false,area_2);
        NodePoint[] arrayV=new NodePoint[]{node_1,node_2,node_3,node_4};
        return arrayV;
    }

    public static void main(String[] args) {
        NodePoint[] arrayV = inintialNode();
        GraphOfMatrix graph = new GraphOfMatrix(arrayV.length,true);
        graph.initArray(arrayV);

        graph.addEdg(arrayV[0],arrayV[1],142.5);
        graph.addEdg(arrayV[1],arrayV[2],40.2881);
        graph.addEdg(arrayV[2],arrayV[3],164.5);

        graph.printGraph();
        System.out.println(graph.getDevOfV(arrayV[0]));
    }
//    public static GraphOfMatrix initial(){
//        int size = 3;
//        GraphOfMatrix graph = new GraphOfMatrix(size,true);
//
//        /**
//         * 初始化节点列表
//         */
//        List<String> l1 = new ArrayList();
//        l1.add("1-1");
//        l1.add("1-2");
//        l1.add("1-3");
////        List<AgvInfo> agv1 = new ArrayList<>();
////        List<AgvInfo> agv2 = new ArrayList<>();
//        ReprintArea reprint1 = null;
//        NodePoint n1 = new NodePoint(1,"1", l1,null,true,reprint1);
//
//        List<String> l2 = new ArrayList();
//        l2.add("1-1");
//        l2.add("1-2");
//        l2.add("1-3");
//
////        ReprintArea reprint1 = null;
//        NodePoint n2 = new NodePoint(1,"1", l2,null,true,null);
//
//        List<String> l3 = new ArrayList();
//        l3.add("1-1");
//        l3.add("1-2");
//        l3.add("1-3");
//
////        ReprintArea reprint1 = null;
//        NodePoint n3 = new NodePoint(1,"1", l3,null,true,null);
//
//        graph.addEdg(n1,n2,1);
//        graph.addEdg(n1,n3,2);
//
//        return  graph;
//    }
}
