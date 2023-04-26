package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;

import java.util.*;

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


    /**
     * 获取agv往返路径
     */
    public LinkedHashMap<AgvInfo,List<NodePoint>> findShortPaths(double[][] Matrix,LinkedHashMap<AgvInfo,NodePoint> nodes,int src){
        LinkedHashMap<AgvInfo,List<NodePoint>> paths= new LinkedHashMap<>();

        //遍历目标节点数组，对每个目标节点寻找最短路径
        for (Map.Entry<AgvInfo,NodePoint> entry:nodes.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                continue;//如果目标节点忙碌中，跳过该节点
            }
            int dest=node.getId();
            List<NodePoint> path=bfs(Matrix,this.arrayV,src,dest);
            paths.put(entry.getKey(),path);
            if (path.size()>0){
                this.arrayV[node.getId()].setState(1);//设置该节点为忙碌状态
//                node.setState(1);
            }
        }
        //装载后回程路径
        for (Map.Entry<AgvInfo,NodePoint> entry:nodes.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                this.arrayV[node.getId()].setState(0);//目标节点工作结束，释放节点
            }
        }
        for (Map.Entry<AgvInfo,NodePoint> entry:nodes.entrySet()){
            NodePoint node=entry.getValue();
            int dest=node.getId();
            if (node.getPathNum().equals("4")||node.getPathNum().equals("8")){
                src=0;
                List<NodePoint> path=bfs(Matrix,this.arrayV,dest,src);
                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
                    path.remove(0);
                    paths.get(entry.getKey()).addAll(path);//合并路径
                }
            }else if (node.getPathNum().equals("14")||node.getPathNum().equals("19")){
                src=115;
                List<NodePoint> path=bfs(Matrix,this.arrayV,dest,src);
                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
                    path.remove(0);
                    paths.get(entry.getKey()).addAll(path);//合并路径
                }
            }
        }
        return paths;
    }

    private List<NodePoint> bfs(double[][] Matrix, NodePoint[] arrayV, int src, int dest) {
        Queue<List<NodePoint>> queue=new LinkedList<>();
        int len=arrayV.length;
        boolean[] visited=new boolean[len];
        List<NodePoint> path=new ArrayList<>();
        path.add(arrayV[src]);
        queue.offer(path);

        while (!queue.isEmpty()){
            List<NodePoint> currpath=queue.poll();
            NodePoint currNode=currpath.get(currpath.size()-1);
            if (currNode.getId()==dest){
                if (currpath.size()>30){
                    return new ArrayList<>();//超出运输范围
                }
                return currpath; //找到目标节点返回最短路径
            }
            for (int i=0;i<len;i++){
                if (Matrix[currNode.getId()][i]>0 &&!visited[i] && arrayV[i].getState()==0&&Matrix[currNode.getId()][i]!=Integer.MAX_VALUE){
                    visited[i]=true;
                    List<NodePoint> newPath=new ArrayList<>(currpath);
                    newPath.add(arrayV[i]);
                    queue.offer(newPath);
                }
            }
        }

        return new ArrayList<>(); //没有找到路径则返回空路径
    }


}
