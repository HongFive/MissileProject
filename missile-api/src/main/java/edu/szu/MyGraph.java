package edu.szu;


import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;

import java.util.ArrayList;

/**
 * 初始化图
 */
public class MyGraph {

    private NodePoint[] arrayV;//节点数组
    private double[][] Matrix;//边矩阵

    public void inintialNode(){
        ReprintArea area_1=new ReprintArea(1,new ArrayList<AgvInfo>()); //上转载区
        ReprintArea area_2=new ReprintArea(2,new ArrayList<AgvInfo>()); //上转载区
        NodePoint node_1=new NodePoint(1,"1",new ArrayList<>(),null,false,area_1);

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
