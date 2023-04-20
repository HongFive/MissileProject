package edu.szu.controller;



import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;


/**
 * 初始化图
 */
public class MyGraph extends GraphOfMatrix{


//    private static MyGraph myGraphController;
//
//    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
//    public void init() {
//        myGraphController = this;
//        myGraphController.shelfService = this.shelfService;
//        // 初使化时将已静态化的testService实例化
//    }
    public static final Integer FREE=0;
    public static final Integer WORKING=1;

    public MyGraph(int size, boolean isDirect) {
        super(size, isDirect);
    }

    public NodePoint[] inintialNode(List<AgvInfo> agvInfoList,List<ShelfInfo> shelfInfoList){

        ArrayList<NodePoint> arrayV=new ArrayList<>();

        // TODO 根据agv位置安排转载区
        //转载区信息
        ReprintArea area_1 = new ReprintArea(1,new ArrayList<AgvInfo>()); //上转载区
        ReprintArea area_2 = new ReprintArea(2,new ArrayList<AgvInfo>()); //下转载区

        //初始化节点信息
        NodePoint node_0 = new NodePoint(0,"1",new ArrayList<String>(),null,FREE,area_1);
        NodePoint node_1 = new NodePoint(1,"2",new ArrayList<String>(),null,FREE,null);
        NodePoint node_2 = new NodePoint(2,"3",new ArrayList<String>(),null,FREE,null);
        arrayV.add(node_0);
        arrayV.add(node_1);
        arrayV.add(node_2);

        //路径4货架信息初始化
        ArrayList<ShelfInfo> shelfs=new ArrayList<>();
        ArrayList<ShelfInfo> remove_list=new ArrayList<>();
        int i=1;
        int count=1;
        int idNum=3;
        for (ShelfInfo shelf:shelfInfoList){
            String loc="4-"+i;
            shelf.setLocation(loc);
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                NodePoint node_4_=new NodePoint(idNum++,"4",new ArrayList<>(),new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_4_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        NodePoint node_28 = new NodePoint(28,"5",new ArrayList<String>(),null,FREE,null);
        NodePoint node_29 = new NodePoint(29,"6",new ArrayList<String>(),null,FREE,null);
        NodePoint node_30 = new NodePoint(30,"7",new ArrayList<String>(),null,FREE,null);
        arrayV.add(node_28);
        arrayV.add(node_29);
        arrayV.add(node_30);

        //路径8货架信息初始化

        i=1;
        count=1;
        idNum=31;
        for (ShelfInfo shelf:shelfInfoList){
            String loc="8-"+i;
            shelf.setLocation(loc);
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                NodePoint node_8_=new NodePoint(idNum++,"8",new ArrayList<>(),new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_8_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        NodePoint node_56 = new NodePoint(56,"9",new ArrayList<String>(),null,FREE,null);
        NodePoint node_57 = new NodePoint(57,"11",new ArrayList<String>(),null,FREE,null);
        NodePoint node_58 = new NodePoint(58,"12",new ArrayList<String>(),null,FREE,null);
        NodePoint node_59 = new NodePoint(59,"13",new ArrayList<String>(),null,FREE,null);
        arrayV.add(node_56);
        arrayV.add(node_57);
        arrayV.add(node_58);
        arrayV.add(node_59);
        //路径14货架信息初始化
        i=1;
        count=1;
        idNum=60;
        for (ShelfInfo shelf:shelfInfoList){
            String loc="14-"+i;
            shelf.setLocation(loc);
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                NodePoint node_14_=new NodePoint(idNum++,"14",new ArrayList<>(),new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_14_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        NodePoint node_85 = new NodePoint(85,"15",new ArrayList<String>(),null,FREE,null);
        NodePoint node_86 = new NodePoint(86,"16",new ArrayList<String>(),null,FREE,null);
        NodePoint node_87 = new NodePoint(87,"17",new ArrayList<String>(),null,FREE,null);
        NodePoint node_88 = new NodePoint(88,"18",new ArrayList<String>(),null,FREE,null);
        arrayV.add(node_85);
        arrayV.add(node_86);
        arrayV.add(node_87);
        arrayV.add(node_88);


        //路径19货架信息初始化
        i=1;
        count=1;
        idNum=89;
        for (ShelfInfo shelf:shelfInfoList){
            String loc="19-"+i;
            shelf.setLocation(loc);
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                NodePoint node_19_=new NodePoint(idNum++,"19",new ArrayList<>(),new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_19_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        NodePoint node_114 = new NodePoint(114,"20",new ArrayList<String>(),null,FREE,null);
        NodePoint node_115 = new NodePoint(115,"29",new ArrayList<String>(),null,FREE,area_2);
        NodePoint node_116 = new NodePoint(116,"31",new ArrayList<String>(),null,FREE,null);
        NodePoint node_117 = new NodePoint(117,"32",new ArrayList<String>(),null,FREE,null);
        arrayV.add(node_114);
        arrayV.add(node_115);
        arrayV.add(node_116);
        arrayV.add(node_117);

        int size=arrayV.size();
        NodePoint[] array = (NodePoint[])arrayV.toArray(new NodePoint[size]);
        return array;
    }


    public MyGraph initialGraph(List<AgvInfo> agvInfoList,List<ShelfInfo> shelfInfoList){
        NodePoint[] arrayV=inintialNode(agvInfoList,shelfInfoList);
        MyGraph graph=new MyGraph(arrayV.length,true);
        graph.arrayV=arrayV;
        graph.initArray(arrayV);
        //node_0
        graph.addEdg(arrayV[0],arrayV[57],142.5);
        //node_1
        graph.addEdg(arrayV[1],arrayV[0],142.5);
        //node_2
        graph.addEdg(arrayV[2],arrayV[1],40.2881);
        graph.addEdg(arrayV[2],arrayV[3],4.0);
        //node_3
        graph.addEdg(arrayV[3],arrayV[2],4.0);
        graph.addEdg(arrayV[3],arrayV[3+28],10.0);
        graph.addEdg(arrayV[3],arrayV[3+1],10.0);

        //路径编号4 边初始化
        for (int i=4;i<27;i++){
            graph.addEdg(arrayV[i],arrayV[i-1],10.0);
            graph.addEdg(arrayV[i],arrayV[i+1],10.0);
            graph.addEdg(arrayV[i],arrayV[i+28],10.0);
        }

        //node_27
        graph.addEdg(arrayV[27],arrayV[27-1],10.0);
        graph.addEdg(arrayV[27],arrayV[27+28],10.0);
        //node_28
        graph.addEdg(arrayV[28],arrayV[27],4.0);
        graph.addEdg(arrayV[28],arrayV[56],4.0);
        //node_29
        graph.addEdg(arrayV[29],arrayV[28],33.281);
        graph.addEdg(arrayV[29],arrayV[117],51.6);
        //node_30
        graph.addEdg(arrayV[30],arrayV[2],9.0);
        graph.addEdg(arrayV[30],arrayV[31],4.0);
        //node_31
        graph.addEdg(arrayV[31],arrayV[31-1],4.0);
        graph.addEdg(arrayV[31],arrayV[31+1],10.0);
        graph.addEdg(arrayV[31],arrayV[31-28],10.0);

        //路径编号8 边初始化
        for (int i=32;i<55;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i - 28], 10.0);
        }

        //node_55
        graph.addEdg(arrayV[55], arrayV[55 - 1], 10.0);
        graph.addEdg(arrayV[55], arrayV[55 - 28], 10.0);
        //node_56
        graph.addEdg(arrayV[56], arrayV[55], 4.0);
        //node_57
        graph.addEdg(arrayV[57],arrayV[30],40.2881);
        graph.addEdg(arrayV[57],arrayV[58],59.5);
        //node_58
        graph.addEdg(arrayV[58],arrayV[88],58.952);
        //node_59
        graph.addEdg(arrayV[59],arrayV[60],7.5);
        //node_60
        graph.addEdg(arrayV[60],arrayV[60+1],10.0);
        graph.addEdg(arrayV[60],arrayV[60+29],10.0);

        //路径编号14 边初始化
        for (int i=61;i<84;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 29], 10.0);
        }

        //node_84
        graph.addEdg(arrayV[84],arrayV[84-1],10.0);
        graph.addEdg(arrayV[84],arrayV[84+29],10.0);
        graph.addEdg(arrayV[84],arrayV[84+1],7.5);
        //node_85
        graph.addEdg(arrayV[85],arrayV[84],7.5);
        graph.addEdg(arrayV[85],arrayV[114],9.0);
        //node_86
        graph.addEdg(arrayV[86],arrayV[87],51.6);
        //node_87
        graph.addEdg(arrayV[87],arrayV[115],358.0);
        //node_88
        graph.addEdg(arrayV[88],arrayV[59],9.0);
        graph.addEdg(arrayV[88],arrayV[89],7.5);
        //node_89
        graph.addEdg(arrayV[89],arrayV[89+1],10.0);
        graph.addEdg(arrayV[89],arrayV[89-29],10.0);

        //路径编号19 边初始化
        for (int i=90;i<113;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i - 29], 10.0);
        }

        //node_113
        graph.addEdg(arrayV[113],arrayV[113-29],10.0);
        graph.addEdg(arrayV[113],arrayV[113-1],10.0);
        graph.addEdg(arrayV[113],arrayV[113+1],7.5);
        //node_114
        graph.addEdg(arrayV[114],arrayV[86],63.6963);
        //node_115
        graph.addEdg(arrayV[115],arrayV[29],358.0);
        //node_116
        //node_117
        graph.addEdg(arrayV[117],arrayV[85],63.6963);
        return graph;
    }

}
