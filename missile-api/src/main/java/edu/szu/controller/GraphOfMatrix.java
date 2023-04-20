package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GraphOfMatrix {
    public NodePoint[] arrayV;//节点数组
    public double[][] Matrix;//邻接矩阵
    public boolean isDirect;//是否是有向图
    public HashMap<NodePoint, Integer> map=new HashMap<>();//优化版的写法 , 目的是建立节点数组与其下标之间的映射关系

    //构造节点数组和邻接矩阵 size表示当前节点的个数
    public GraphOfMatrix(int size, boolean isDirect) {
        arrayV = new NodePoint[size];
        Matrix = new double[size][size];
        //将邻接矩阵的每一位都初始化为无穷大
        for (int i = 0; i < size; i++) {
            Arrays.fill(Matrix[i], Integer.MAX_VALUE);
        }
        this.isDirect = isDirect;
    }

    /**
     * 初始化节点数组
     *
     * @param array
     */

    public void initArray(NodePoint[] array) {

        for (int i = 0; i < array.length; i++) {
            //要么初始化节点数组 , 要么建立映射关系.二选一
            map.put(array[i], i);
//            arrayV[i] = array[i];
        }
    }

    /**
     * 添加边
     *
     * @param src    起始节点
     * @param dest   终止节点
     * @param weight 权值
     */
    public void addEdg(NodePoint src, NodePoint dest, double weight) {
        //首先要确定起始节点和终止节点在矩阵中的位置
        int srcIndex = getIndexOfV(src);
        int destIndex = getIndexOfV(dest);
        //将节点和节点之间的关系存储在矩阵中
        Matrix[srcIndex][destIndex] = weight;
        //如果是无向图 , 矩阵对称的位置同样需要赋值
        if (!isDirect) {
            Matrix[destIndex][srcIndex] = weight;
        }
    }

    /**
     * 获取节点数组的下标
     *
     * @param n
     * @return
     */
    public Integer getIndexOfV(NodePoint n) {
        //同样两种写法二选一
        return map.get(n);
//        for (int i = 0; i < arrayV.length; i++) {
//            if (arrayV[i]==v){
//                return i;
//            }
//        }
//        return -1;
    }

    /**
     * 获取顶点的度
     *
     * @param n 有向图 = 入度+出度
     * @return
     */
    public int getDevOfV(NodePoint n) {
        int count = 0;
        int srcIndex = getIndexOfV(n);
        for (int i = 0; i < Matrix.length; i++) {
            if (Matrix[srcIndex][i] != Integer.MIN_VALUE) {
                count++;
            }
        }
        //计算有向图的出度
        if (isDirect) {
            for (int i = 0; i < Matrix[0].length; i++) {
                if (Matrix[i][srcIndex] != Integer.MIN_VALUE) {
                    count++;
                }
            }
        }
        return count;
    }

    //打印邻接表
    public void printGraph() {
        for (int i = 0; i < Matrix.length; i++) {
            for (int j = 0; j < Matrix[0].length; j++) {
                if (Matrix[i][j] != Integer.MAX_VALUE) {
                    System.out.print(Matrix[i][j] + " ");
                } else {
                    System.out.print("∞ ");
                }
            }
            System.out.println();
        }
    }

    public void inintialNode(){
//        ReprintArea area_1=new ReprintArea(1,new ArrayList<AgvInfo>()); //上转载区
//        ReprintArea area_2=new ReprintArea(2,new ArrayList<AgvInfo>()); //上转载区
//        NodePoint node_1=new NodePoint(1,"1",new ArrayList<String>(),null,false,area_1);
//        NodePoint node_2=new NodePoint(2,"2",new ArrayList<String>(),null,false,null);
//
//        ShelfInfo shelf_4_1=new ShelfInfo(1,"1-1",1,"内",false,"4-1");
//        ShelfInfo shelf_4_2=new ShelfInfo(1,"1-2",1,"外",false,"4-1");
//        List<ShelfInfo> shelfInfos= Arrays.asList(shelf_4_1,shelf_4_2);
//        NodePoint node_3=new NodePoint(3,"4",new ArrayList<String>(),shelfInfos,false,null);
//
//        NodePoint node_4=new NodePoint(4,"23",new ArrayList<String>(),null,false,area_2);
//        NodePoint[] arrayV=new NodePoint[]{node_1,node_2,node_3,node_4};
//        return arrayV;
    }



}
