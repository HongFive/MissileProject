package edu.szu.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class GraphOfMatrix {
    private char[] arrayV;//节点数组
    private int[][] Matrix;//邻接矩阵
    private boolean isDirect;//是否是有向图
    HashMap<Character, Integer> map;//优化版的写法 , 目的是建立节点数组与其下标之间的映射关系

    //构造节点数组和邻接矩阵 size表示当前节点的个数
    public GraphOfMatrix(int size, boolean isDirect) {
        arrayV = new char[size];
        Matrix = new int[size][size];
        //将邻接矩阵的每一位都初始化为无穷大
        for (int i = 0; i < size; i++) {
            Arrays.fill(Matrix[i], Integer.MIN_VALUE);
        }
        this.isDirect = isDirect;
    }

    /**
     * 初始化节点数组
     *
     * @param array
     */
    public void initArray(char[] array) {

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
    public void addEdg(char src, char dest, int weight) {
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
     * @param v
     * @return
     */
    public int getIndexOfV(char v) {
        //同样两种写法二选一
        return map.get(v);
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
     * @param v 有向图 = 入度+出度
     * @return
     */
    public int getDevOfV(char v) {
        int count = 0;
        int srcIndex = getIndexOfV(v);
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
                if (Matrix[i][j] != Integer.MIN_VALUE) {
                    System.out.print(Matrix[i][j] + " ");
                } else {
                    System.out.print("∞ ");
                }
            }
            System.out.println();
        }
    }


}
