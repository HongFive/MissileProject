package edu.szu.controller;

import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.vo.NodePoint;

import java.util.*;
import java.util.stream.Collectors;

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
     * 获取agv往返安全路径
     * 路径寻找顺序，第一优先，随后，倒车的路径先进后出，非倒车的先进先出
     */
    public LinkedHashMap<AgvInfo,List<NodePoint>> findShortPathsSafe(LinkedHashMap<AgvInfo,NodePoint> agvShelf){

        //将车与货架分为 四个部分 上出上回 上出下回 下出下回 下出上回
        LinkedHashMap<AgvInfo,NodePoint> uptoup=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> uptodown=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downtodown=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downtoup=new LinkedHashMap<>();

        for (Map.Entry<AgvInfo,NodePoint> entry:agvShelf.entrySet()){
            String[] loc=entry.getKey().getLocation().split("-");
            if (loc[0].equals("25")&&(entry.getValue().getPathNum().equals("4")||entry.getValue().getPathNum().equals("9"))){
                uptoup.put(entry.getKey(), entry.getValue());
            }else if(loc[0].equals("25")&&(entry.getValue().getPathNum().equals("15")||entry.getValue().getPathNum().equals("22"))){
                    uptodown.put(entry.getKey(), entry.getValue());
                }else if (loc[0].equals("26")&&(entry.getValue().getPathNum().equals("15")||entry.getValue().getPathNum().equals("22"))){
                downtodown.put(entry.getKey(), entry.getValue());
            }else if (loc[0].equals("26")&&(entry.getValue().getPathNum().equals("4")||entry.getValue().getPathNum().equals("9"))){
                downtoup.put(entry.getKey(),entry.getValue());
            }
        }

        //四个部分 设定发车顺序 第一个任务优先 其余由内而外发车
        uptoup=uptoup.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, LinkedHashMap::new));

        LinkedHashMap<AgvInfo, NodePoint> newMap = new LinkedHashMap<>();
        Map.Entry<AgvInfo,NodePoint> lastEntry=null;
        for (Map.Entry<AgvInfo,NodePoint> entry:uptoup.entrySet()){
            lastEntry=entry;
        }
        assert lastEntry != null;
        uptoup.remove(lastEntry.getKey());
        newMap.put(lastEntry.getKey(),lastEntry.getValue());
        newMap.putAll(uptoup);
        uptoup=newMap;

        uptodown=uptodown.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, LinkedHashMap::new));

        downtodown=downtodown.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        LinkedHashMap<AgvInfo, NodePoint> newMap_ = new LinkedHashMap<>();
        lastEntry=null;
        for (Map.Entry<AgvInfo,NodePoint> entry:downtodown.entrySet()){
            lastEntry=entry;
        }
        assert lastEntry != null;
        downtodown.remove(lastEntry.getKey());
        newMap_.put(lastEntry.getKey(),lastEntry.getValue());
        newMap_.putAll(downtodown);
        downtodown=newMap_;

        downtoup=downtoup.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //设定四个部分的回程顺序  由外到内地依次出车回程
        Stack<Map.Entry<AgvInfo,NodePoint>> rightRoadStack= new Stack();
        Stack<Map.Entry<AgvInfo,NodePoint>> leftRoadStack=new Stack();

        //右货架通道入栈
        Map.Entry<AgvInfo,NodePoint> upFirst=uptoup.entrySet().iterator().next();
        LinkedHashMap<AgvInfo,NodePoint> upRun=new LinkedHashMap<>(uptoup);
        upRun.putAll(uptodown);
        List<Map.Entry<AgvInfo, NodePoint>> tempList = new ArrayList<>(downtoup.entrySet());
        Collections.reverse(tempList);
        for (Map.Entry<AgvInfo, NodePoint> entry:tempList){
            rightRoadStack.push(entry);
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:uptoup.entrySet()){
            if (entry==upFirst) continue;
            rightRoadStack.push(entry);
        }
        rightRoadStack.push(upFirst);

        //左货架通道入栈
        Map.Entry<AgvInfo,NodePoint> downFirst=downtodown.entrySet().iterator().next();
        LinkedHashMap<AgvInfo,NodePoint> downRun=new LinkedHashMap<>(downtodown);
        downRun.putAll(downtoup);
        tempList = new ArrayList<>(uptodown.entrySet());
        Collections.reverse(tempList);
        for (Map.Entry<AgvInfo, NodePoint> entry:tempList){
            leftRoadStack.push(entry);
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:downtodown.entrySet()){
            if (entry==downFirst) continue;
            leftRoadStack.push(entry);
        }
        leftRoadStack.push(downFirst);


        //寻找路径
        int upArea=121;
        int upReprintArea=0;
        int downArea=122;
        int downReprintArea=123;
        LinkedHashMap<AgvInfo,List<NodePoint>> upPaths= new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,List<NodePoint>> downPaths= new LinkedHashMap<>();

        //遍历目标节点数组，对每个目标节点寻找最短路径
        for (Map.Entry<AgvInfo,NodePoint> entry:upRun.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                continue;//如果目标节点忙碌中，跳过该节点
            }
            int target=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,upArea,target);
            upPaths.put(entry.getKey(),path);
            if (path.size()>0){
                //设置该节点为忙碌状态  可能出现点位取不到值的情况
                try {
                    Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                    String[] index=first.getKey().split("-");
                    if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                    }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }else {//货架前后保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        //遍历目标节点数组，对每个目标节点寻找最短路径
        for (Map.Entry<AgvInfo,NodePoint> entry:downRun.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                continue;//如果目标节点忙碌中，跳过该节点
            }
            int target=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,downArea,target);
            downPaths.put(entry.getKey(),path);
            if (path.size()>0){
                //设置该节点为忙碌状态  可能出现点位取不到值的情况
                try {
                    Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                    String[] index=first.getKey().split("-");
                    if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                    }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }else {//货架前后保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        while (!rightRoadStack.isEmpty()){
            Map.Entry<AgvInfo,NodePoint> entry=rightRoadStack.peek();
            NodePoint node=entry.getValue();
            //目标节点工作结束，释放节点
            if (this.arrayV[node.getId()].getState()==1){
                this.arrayV[node.getId()].setState(0);
                this.arrayV[node.getId()+1].setState(0);
                this.arrayV[node.getId()-1].setState(0);
            }
            int src=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,src, upReprintArea);
            if (path.size()>0){
                path.remove(0);
                if (upPaths.containsKey(entry.getKey())&&upPaths.get(entry.getKey()).size()>0){
                    upPaths.get(entry.getKey()).addAll(path);//合并路径
                }else if (downPaths.containsKey(entry.getKey())&&downPaths.get(entry.getKey()).size()>0){
                    downPaths.get(entry.getKey()).addAll(path);
                }
            }
            rightRoadStack.pop();
        }

        while (!leftRoadStack.isEmpty()){
            Map.Entry<AgvInfo,NodePoint> entry=leftRoadStack.peek();
            NodePoint node=entry.getValue();
            //目标节点工作结束，释放节点
            if (this.arrayV[node.getId()].getState()==1){
                this.arrayV[node.getId()].setState(0);
                this.arrayV[node.getId()+1].setState(0);
                this.arrayV[node.getId()-1].setState(0);
            }
            int src=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,src, downReprintArea);
            if (path.size()>0){
                path.remove(0);
                if (upPaths.containsKey(entry.getKey())&&upPaths.get(entry.getKey()).size()>0){
                    upPaths.get(entry.getKey()).addAll(path);//合并路径
                }else if (downPaths.containsKey(entry.getKey())&&downPaths.get(entry.getKey()).size()>0){
                    downPaths.get(entry.getKey()).addAll(path);
                }
            }
            leftRoadStack.pop();
        }


//        for (Map.Entry<AgvInfo,NodePoint> entry:nodes.entrySet()){
//            NodePoint node=entry.getValue();
//            //目标节点工作结束，释放节点
//            if (this.arrayV[node.getId()].getState()==1){
//                this.arrayV[node.getId()].setState(0);
//                this.arrayV[node.getId()+1].setState(0);
//                this.arrayV[node.getId()-1].setState(0);
//            }
//            src=node.getId();
//            if (node.getPathNum().equals("4")||node.getPathNum().equals("9")){
//                int dest=0;
//                List<NodePoint> path=bfs(this.Matrix,this.arrayV,src,dest);
//                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
//                    path.remove(0);
//                    paths.get(entry.getKey()).addAll(path);//合并路径
//                }
//            }else if (node.getPathNum().equals("15")||node.getPathNum().equals("22")){
//                int dest=123;
//                List<NodePoint> path=bfs(this.Matrix,this.arrayV,src,dest);
//                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
//                    path.remove(0);
//                    paths.get(entry.getKey()).addAll(path);//合并路径
//                }
//            }
//        }
        LinkedHashMap<AgvInfo,List<NodePoint>> paths=new LinkedHashMap<>(upPaths);
        paths.putAll(downPaths);
        return paths;
    }

    public LinkedHashMap<AgvInfo,List<NodePoint>> findShortPathsRecommend(LinkedHashMap<String,LinkedHashMap<AgvInfo,NodePoint>> agvShelf){

        //将车与货架分为 四个部分 上出上回 上出下回 下出下回 下出上回
        LinkedHashMap<AgvInfo,NodePoint> uptoup=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> uptodown=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downtodown=new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,NodePoint> downtoup=new LinkedHashMap<>();

        for (Map.Entry<String,LinkedHashMap<AgvInfo,NodePoint>> entry:agvShelf.entrySet()){
            if (entry.getKey().equals("uptoup")) uptoup=entry.getValue();
            if (entry.getKey().equals("uptodown")) uptodown=entry.getValue();
            if (entry.getKey().equals("downtodown")) downtodown=entry.getValue();
            if (entry.getKey().equals("downtoup")) downtoup=entry.getValue();
        }

        //四个部分 设定发车顺序 第一个任务优先 其余由内而外发车
        uptoup=uptoup.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, LinkedHashMap::new));

        LinkedHashMap<AgvInfo, NodePoint> newMap = new LinkedHashMap<>();
        Map.Entry<AgvInfo,NodePoint> lastEntry=null;
        for (Map.Entry<AgvInfo,NodePoint> entry:uptoup.entrySet()){
            lastEntry=entry;
        }
        assert lastEntry != null;
        uptoup.remove(lastEntry.getKey());
        newMap.put(lastEntry.getKey(),lastEntry.getValue());
        newMap.putAll(uptoup);
        uptoup=newMap;

        uptodown=uptodown.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> -Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> newValue, LinkedHashMap::new));

        downtodown=downtodown.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        LinkedHashMap<AgvInfo, NodePoint> newMap_ = new LinkedHashMap<>();
        lastEntry=null;
        for (Map.Entry<AgvInfo,NodePoint> entry:downtodown.entrySet()){
            lastEntry=entry;
        }
        assert lastEntry != null;
        downtodown.remove(lastEntry.getKey());
        newMap_.put(lastEntry.getKey(),lastEntry.getValue());
        newMap_.putAll(downtodown);
        downtodown=newMap_;

        downtoup=downtoup.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(e -> Integer.parseInt(e.getValue().getPointList().keySet().iterator().next().split("-")[1])))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        //设定四个部分的回程顺序  由外到内地依次出车回程
        Stack<Map.Entry<AgvInfo,NodePoint>> rightRoadStack= new Stack();
        Stack<Map.Entry<AgvInfo,NodePoint>> leftRoadStack=new Stack();

        //右货架通道入栈
        Map.Entry<AgvInfo,NodePoint> upFirst=uptoup.entrySet().iterator().next();
        LinkedHashMap<AgvInfo,NodePoint> upRun=new LinkedHashMap<>(uptoup);
        upRun.putAll(uptodown);
        List<Map.Entry<AgvInfo, NodePoint>> tempList = new ArrayList<>(downtoup.entrySet());
        Collections.reverse(tempList);
        for (Map.Entry<AgvInfo, NodePoint> entry:tempList){
            rightRoadStack.push(entry);
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:uptoup.entrySet()){
            if (entry==upFirst) continue;
            rightRoadStack.push(entry);
        }
        rightRoadStack.push(upFirst);

        //左货架通道入栈
        Map.Entry<AgvInfo,NodePoint> downFirst=downtodown.entrySet().iterator().next();
        LinkedHashMap<AgvInfo,NodePoint> downRun=new LinkedHashMap<>(downtodown);
        downRun.putAll(downtoup);
        tempList = new ArrayList<>(uptodown.entrySet());
        Collections.reverse(tempList);
        for (Map.Entry<AgvInfo, NodePoint> entry:tempList){
            leftRoadStack.push(entry);
        }
        for (Map.Entry<AgvInfo, NodePoint> entry:downtodown.entrySet()){
            if (entry==downFirst) continue;
            leftRoadStack.push(entry);
        }
        leftRoadStack.push(downFirst);


        //寻找路径
        int upArea=121;
        int upReprintArea=0;
        int downArea=122;
        int downReprintArea=123;
        LinkedHashMap<AgvInfo,List<NodePoint>> upPaths= new LinkedHashMap<>();
        LinkedHashMap<AgvInfo,List<NodePoint>> downPaths= new LinkedHashMap<>();

        //遍历目标节点数组，对每个目标节点寻找最短路径
        Map.Entry<AgvInfo,NodePoint> lastAgv=null;
        for (Map.Entry<AgvInfo,NodePoint> entry:upRun.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                continue;//如果目标节点忙碌中，跳过该节点
            }
            int target=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,upArea,target);

            //并列冲突
            if (path.size()==0){
                if (this.arrayV[lastAgv.getValue().getId()].getState()==1){
                    int tempId=lastAgv.getValue().getId();
                    this.arrayV[tempId].setState(0);
                    this.arrayV[tempId+1].setState(0);
                    this.arrayV[tempId-1].setState(0);
                }
                path=bfs(this.Matrix,this.arrayV,upArea,target);
                if (path.size()!=0){
                    upPaths.remove(lastAgv.getKey());
                    upPaths.put(entry.getKey(),path);
                    try {
                        Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                        String[] index=first.getKey().split("-");
                        if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()+1].setState(1);
                        }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()-1].setState(1);
                        }else {//货架前后保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()+1].setState(1);
                            this.arrayV[node.getId()-1].setState(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    List<NodePoint> lastPath=bfs(this.Matrix,this.arrayV,upArea,lastAgv.getValue().getId());
                    upPaths.put(lastAgv.getKey(),lastPath);
                    try {
                        int tempId=lastAgv.getValue().getId();
                        Map.Entry<String, Double> first=lastAgv.getValue().getPointList().entrySet().iterator().next();
                        String[] index=first.getKey().split("-");
                        if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId+1].setState(1);
                        }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId-1].setState(1);
                        }else {//货架前后保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId+1].setState(1);
                            this.arrayV[tempId-1].setState(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    upPaths.put(entry.getKey(),path);
                }
            } else{//不发生冲突，正常行进
                upPaths.put(entry.getKey(),path);
                //设置该节点为忙碌状态  可能出现点位取不到值的情况
                try {
                    Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                    String[] index=first.getKey().split("-");
                    if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                    }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }else {//货架前后保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            lastAgv=entry;
        }

        lastAgv=null;
        //遍历目标节点数组，对每个目标节点寻找最短路径
        for (Map.Entry<AgvInfo,NodePoint> entry:downRun.entrySet()){
            NodePoint node=entry.getValue();
            if (this.arrayV[node.getId()].getState()==1){
                continue;//如果目标节点忙碌中，跳过该节点
            }
            int target=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,downArea,target);

            //并列冲突
            if (path.size()==0){
                if (this.arrayV[lastAgv.getValue().getId()].getState()==1){
                    int tempId=lastAgv.getValue().getId();
                    this.arrayV[tempId].setState(0);
                    this.arrayV[tempId+1].setState(0);
                    this.arrayV[tempId-1].setState(0);
                }
                path=bfs(this.Matrix,this.arrayV,downArea,target);
                if (path.size()!=0){
                    upPaths.remove(lastAgv.getKey());
                    upPaths.put(entry.getKey(),path);
                    try {
                        Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                        String[] index=first.getKey().split("-");
                        if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()+1].setState(1);
                        }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()-1].setState(1);
                        }else {//货架前后保证安全距离
                            this.arrayV[node.getId()].setState(1);
                            this.arrayV[node.getId()+1].setState(1);
                            this.arrayV[node.getId()-1].setState(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    List<NodePoint> lastPath=bfs(this.Matrix,this.arrayV,downArea,lastAgv.getValue().getId());
                    upPaths.put(lastAgv.getKey(),lastPath);
                    try {
                        int tempId=lastAgv.getValue().getId();
                        Map.Entry<String, Double> first=lastAgv.getValue().getPointList().entrySet().iterator().next();
                        String[] index=first.getKey().split("-");
                        if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId+1].setState(1);
                        }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId-1].setState(1);
                        }else {//货架前后保证安全距离
                            this.arrayV[tempId].setState(1);
                            this.arrayV[tempId+1].setState(1);
                            this.arrayV[tempId-1].setState(1);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    upPaths.put(entry.getKey(),path);
                }
            } else {
                downPaths.put(entry.getKey(),path);
                //设置该节点为忙碌状态  可能出现点位取不到值的情况
                try {
                    Map.Entry<String, Double> first=node.getPointList().entrySet().iterator().next();
                    String[] index=first.getKey().split("-");
                    if (index[1].equals("1")){//第一个货架的后一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                    }else if (index[1].equals("25")){//最后一个货架的前一个货架保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }else {//货架前后保证安全距离
                        this.arrayV[node.getId()].setState(1);
                        this.arrayV[node.getId()+1].setState(1);
                        this.arrayV[node.getId()-1].setState(1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        while (!rightRoadStack.isEmpty()){
            Map.Entry<AgvInfo,NodePoint> entry=rightRoadStack.peek();
            NodePoint node=entry.getValue();
            //目标节点工作结束，释放节点
            if (this.arrayV[node.getId()].getState()==1){
                this.arrayV[node.getId()].setState(0);
                this.arrayV[node.getId()+1].setState(0);
                this.arrayV[node.getId()-1].setState(0);
            }
            int src=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,src, upReprintArea);
            if (path.size()>0){
                path.remove(0);
                if (upPaths.containsKey(entry.getKey())&&upPaths.get(entry.getKey()).size()>0){
                    upPaths.get(entry.getKey()).addAll(path);//合并路径
                }else if (downPaths.containsKey(entry.getKey())&&downPaths.get(entry.getKey()).size()>0){
                    downPaths.get(entry.getKey()).addAll(path);
                }
            }
            rightRoadStack.pop();
        }

        while (!leftRoadStack.isEmpty()){
            Map.Entry<AgvInfo,NodePoint> entry=leftRoadStack.peek();
            NodePoint node=entry.getValue();
            //目标节点工作结束，释放节点
            if (this.arrayV[node.getId()].getState()==1){
                this.arrayV[node.getId()].setState(0);
                this.arrayV[node.getId()+1].setState(0);
                this.arrayV[node.getId()-1].setState(0);
            }
            int src=node.getId();
            List<NodePoint> path=bfs(this.Matrix,this.arrayV,src, downReprintArea);
            if (path.size()>0){
                path.remove(0);
                if (upPaths.containsKey(entry.getKey())&&upPaths.get(entry.getKey()).size()>0){
                    upPaths.get(entry.getKey()).addAll(path);//合并路径
                }else if (downPaths.containsKey(entry.getKey())&&downPaths.get(entry.getKey()).size()>0){
                    downPaths.get(entry.getKey()).addAll(path);
                }
            }
            leftRoadStack.pop();
        }


//        for (Map.Entry<AgvInfo,NodePoint> entry:nodes.entrySet()){
//            NodePoint node=entry.getValue();
//            //目标节点工作结束，释放节点
//            if (this.arrayV[node.getId()].getState()==1){
//                this.arrayV[node.getId()].setState(0);
//                this.arrayV[node.getId()+1].setState(0);
//                this.arrayV[node.getId()-1].setState(0);
//            }
//            src=node.getId();
//            if (node.getPathNum().equals("4")||node.getPathNum().equals("9")){
//                int dest=0;
//                List<NodePoint> path=bfs(this.Matrix,this.arrayV,src,dest);
//                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
//                    path.remove(0);
//                    paths.get(entry.getKey()).addAll(path);//合并路径
//                }
//            }else if (node.getPathNum().equals("15")||node.getPathNum().equals("22")){
//                int dest=123;
//                List<NodePoint> path=bfs(this.Matrix,this.arrayV,src,dest);
//                if (path.size()>0&&paths.get(entry.getKey()).size()>0){
//                    path.remove(0);
//                    paths.get(entry.getKey()).addAll(path);//合并路径
//                }
//            }
//        }
        LinkedHashMap<AgvInfo,List<NodePoint>> paths=new LinkedHashMap<>(upPaths);
        paths.putAll(downPaths);
        return paths;
    }

    public Map.Entry<AgvInfo,List<NodePoint>> searchForOne(Map.Entry<AgvInfo,NodePoint> target){
        LinkedHashMap<AgvInfo,List<NodePoint>> result=new LinkedHashMap<>();
        int src,dest=0;
        //确定起始位置与目标位置
        if (target.getKey().getLocation().split("-")[0].equals("25")){
            src=121;
        }else src=122;
        if (target.getValue().getPathNum().equals("4")||target.getValue().getPathNum().equals("9")){
            dest=0;
        }else dest=123;
        //寻找路径
        List<NodePoint> path=bfsSafe(this.Matrix,this.arrayV,src,target.getValue().getId());
        path.remove(path.size()-1);
        path.addAll(bfs(this.Matrix,this.arrayV,target.getValue().getId(),dest));
        result.put(target.getKey(),path);
        return result.entrySet().iterator().next();
    }

    private List<NodePoint> bfsSafe(double[][] Matrix, NodePoint[] arrayV, int src, int dest) {
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


    /**
     * 获取agv往返路径
     */
    public LinkedHashMap<AgvInfo,List<NodePoint>> findShortPaths(double[][] Matrix, LinkedHashMap<AgvInfo,NodePoint> nodes, int src){
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
