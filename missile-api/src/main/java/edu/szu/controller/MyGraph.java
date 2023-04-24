package edu.szu.controller;



import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 初始化图
 */
public class MyGraph extends GraphOfMatrix{

    public static final Integer FREE=0;
    public static final Integer WORKING=1;

    public MyGraph(int size, boolean isDirect) {
        super(size, isDirect);
    }

    public NodePoint[] inintialNode(List<AgvInfo> agvInfoList,List<ShelfInfo> shelfInfoList){

        ArrayList<NodePoint> arrayV=new ArrayList<>();

        //转载区信息
        ArrayList<AgvInfo> agvs_up=new ArrayList<>();
        ArrayList<AgvInfo> agvs_down=new ArrayList<>();
        for (AgvInfo agv:agvInfoList){
            String[] temp=agv.getLocation().split("-");
            if (temp[0].equals("21")) agvs_up.add(agv);
            if (temp[0].equals("23")) agvs_down.add(agv);
        }
        ReprintArea area_1 = new ReprintArea(21,agvs_up); //上转载区
        ReprintArea area_2 = new ReprintArea(23,agvs_down); //下转载区

        //初始化节点信息
        LinkedHashMap<String,Double> map0=new LinkedHashMap<>();
        for (int j = 1; j <=16; j++) {
            map0.put("1-"+j,10.0);
        }
        NodePoint node_0 = new NodePoint(0,"1",map0,null,FREE,area_1);

        LinkedHashMap<String,Double> map1=new LinkedHashMap<>();
        for (int j = 1; j <=14; j++) {
            map1.put("2-"+j,10.0);
        }
        NodePoint node_1 = new NodePoint(1,"2",map1,null,FREE,null);

        LinkedHashMap<String,Double> map2=new LinkedHashMap<>();
        for (int j = 1; j <=5; j++) {
            map2.put("3-"+j,8.0);
        }
        NodePoint node_2 = new NodePoint(2,"3",map2,null,FREE,null);
        arrayV.add(node_0);
        arrayV.add(node_1);
        arrayV.add(node_2);

        //路径4货架信息初始化
        ArrayList<ShelfInfo> shelfs=new ArrayList<>();
        ArrayList<ShelfInfo> remove_list=new ArrayList<>();
        int i=1;
        int t=1;
        int count=1;
        int idNum=3;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map4_=new LinkedHashMap<>();
                if (i==1){
                    map4_.put("4-"+t,6.0);
                    t++;
                    map4_.put("4-"+t,10.0);
                }
                else if (i==25){
                    map4_.put("4-"+t,10.0);
                    t++;
                    map4_.put("4-"+t,6.0);
                }
                else map4_.put("4-"+t,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("4-"+t);
                }
                NodePoint node_4_=new NodePoint(idNum++,"4",map4_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_4_);
                shelfs.clear();
                i++;
                t++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map28=new LinkedHashMap<>();
        for (int j = 4; j >=1; j--) {
            map28.put("5-"+j,7.5);
        }
        NodePoint node_28 = new NodePoint(28,"5",map28,null,FREE,null);

        LinkedHashMap<String,Double> map29=new LinkedHashMap<>();
        map29.put("6-17",15.0);
        for (int j = 16; j >=1; j--) {
            map29.put("6-"+j,10.0);
        }
        NodePoint node_29 = new NodePoint(29,"6",map29,null,FREE,null);

        LinkedHashMap<String,Double> map30=new LinkedHashMap<>();
        for (int j = 1; j <=4; j++) {
            map30.put("7-"+j,8.0);
        }
        NodePoint node_30 = new NodePoint(30,"7",map30,null,FREE,null);
        arrayV.add(node_28);
        arrayV.add(node_29);
        arrayV.add(node_30);

        //路径8货架信息初始化

        i=1;
        t=1;
        count=1;
        idNum=31;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map8_=new LinkedHashMap<>();
                if (i==1){
                    map8_.put("8-"+t,6.0);
                    t++;
                    map8_.put("8-"+t,10.0);
                }
                else if (i==25){
                    map8_.put("8-"+t,10.0);
                    t++;
                    map8_.put("8-"+t,6.0);
                }
                else map8_.put("8-"+t,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("8-"+t);
                }
                NodePoint node_8_=new NodePoint(idNum++,"8",map8_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_8_);
                shelfs.clear();
                i++;
                t++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map56=new LinkedHashMap<>();
        for (int j = 4; j >=1; j--) {
            map56.put("9-"+j,7.5);
        }
        NodePoint node_56 = new NodePoint(56,"9",map56,null,FREE,null);

        LinkedHashMap<String,Double> map57=new LinkedHashMap<>();
        for (int j = 1; j <=13; j++) {
            map57.put("11-"+j,10.0);
        }
        map57.put("11-14",14.0);
        NodePoint node_57 = new NodePoint(57,"11",map57,null,FREE,null);

        LinkedHashMap<String,Double> map58=new LinkedHashMap<>();
        for (int j = 1; j <=7; j++) {
            map58.put("12-"+j,8.6);
        }
        NodePoint node_58 = new NodePoint(58,"12",map58,null,FREE,null);

        LinkedHashMap<String,Double> map59=new LinkedHashMap<>();
        for (int j = 1; j <=4; j++) {
            map59.put("13-"+j,12.5);
        }
        map59.put("13-6",10.0);
        NodePoint node_59 = new NodePoint(59,"13",map59,null,FREE,null);
        arrayV.add(node_56);
        arrayV.add(node_57);
        arrayV.add(node_58);
        arrayV.add(node_59);
        //路径14货架信息初始化
        i=1;
        t=1;
        count=1;
        idNum=60;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map14_=new LinkedHashMap<>();
                if (i==1){
                    map14_.put("14-"+t,7.5);
                    t++;
                    map14_.put("14-"+t,10.0);
                }
                else if (i==25){
                    map14_.put("14-"+t,10.0);
                    t++;
                    map14_.put("14-"+t,7.5);
                }
                else map14_.put("14-"+t,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("14-"+t);
                }
                NodePoint node_14_=new NodePoint(idNum++,"14",map14_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_14_);
                shelfs.clear();
                i++;
                t++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map85=new LinkedHashMap<>();
        map85.put("15-7",10.0);
        for (int j = 6; j >=1; j--) {
            map85.put("15-"+j,10.0);
        }
        NodePoint node_85 = new NodePoint(85,"15",map85,null,FREE,null);

        LinkedHashMap<String,Double> map86=new LinkedHashMap<>();
        map86.put("16-6",15.0);
        for (int j = 5; j >=1; j--) {
            map86.put("16-"+j,10.0);
        }
        NodePoint node_86 = new NodePoint(86,"16",map86,null,FREE,null);

        LinkedHashMap<String,Double> map87=new LinkedHashMap<>();
        for (int j = 17; j >=1; j--) {
            map87.put("17-"+j,10.0);
        }
        NodePoint node_87 = new NodePoint(87,"17",map87,null,FREE,null);

        LinkedHashMap<String,Double> map88=new LinkedHashMap<>();
        for (int j = 1; j <=6; j++) {
            map88.put("18-"+j,10.0);
        }
        NodePoint node_88 = new NodePoint(88,"18",map88,null,FREE,null);
        arrayV.add(node_85);
        arrayV.add(node_86);
        arrayV.add(node_87);
        arrayV.add(node_88);


        //路径19货架信息初始化
        i=1;
        t=1;
        count=1;
        idNum=89;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map19_=new LinkedHashMap<>();
                if (i==1){
                    map19_.put("19-"+t,7.5);
                    t++;
                    map19_.put("19-"+t,10.0);
                }
                else if (i==25){
                    map19_.put("19-"+t,10.0);
                    t++;
                    map19_.put("19-"+t,7.5);
                }
                else map19_.put("19-"+t,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("19-"+t);
                }
                NodePoint node_19_=new NodePoint(idNum++,"19",map19_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_19_);
                shelfs.clear();
                i++;
                t++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map114=new LinkedHashMap<>();
        for (int j = 7; j >=1; j--) {
            map114.put("20-"+j,10.0);
        }
        NodePoint node_114 = new NodePoint(114,"20",map114,null,FREE,null);

        LinkedHashMap<String,Double> map115=new LinkedHashMap<>();
        for (int j = 18; j >=1; j--) {
            map115.put("29-"+j,10.0);
        }
        NodePoint node_115 = new NodePoint(115,"29",map115,null,FREE,area_2);

        LinkedHashMap<String,Double> map116=new LinkedHashMap<>();
        for (int j = 1; j <=6; j++) {
            map116.put("31-"+j,10.0);
        }
        NodePoint node_116 = new NodePoint(116,"31",map116,null,FREE,null);

        LinkedHashMap<String,Double> map117=new LinkedHashMap<>();
        for (int j = 5; j >=1; j--) {
            map117.put("32-"+j,10.0);
        }
        NodePoint node_117 = new NodePoint(117,"32",map117,null,FREE,null);
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
        graph.addEdg(arrayV[0],arrayV[57],1440.);
        //node_1
        graph.addEdg(arrayV[1],arrayV[0],144.0);
        //node_2
        graph.addEdg(arrayV[2],arrayV[1],40.0);
        graph.addEdg(arrayV[2],arrayV[3],6.0);
        //node_3
        graph.addEdg(arrayV[3],arrayV[2],6.0);
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
        graph.addEdg(arrayV[28],arrayV[27],6.0);
        graph.addEdg(arrayV[28],arrayV[56],6.0);
        //node_29
        graph.addEdg(arrayV[29],arrayV[28],30.0);
        graph.addEdg(arrayV[29],arrayV[117],65.0);
        //node_30
        graph.addEdg(arrayV[30],arrayV[2],8.2);
        graph.addEdg(arrayV[30],arrayV[31],6.0);
        //node_31
        graph.addEdg(arrayV[31],arrayV[31-1],6.0);
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
        graph.addEdg(arrayV[56], arrayV[55], 6.0);
        //node_57
        graph.addEdg(arrayV[57],arrayV[30],40.0);
        graph.addEdg(arrayV[57],arrayV[58],60.0);
        //node_58
        graph.addEdg(arrayV[58],arrayV[88],60.0);
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
        graph.addEdg(arrayV[85],arrayV[114],8.2);
        //node_86
        graph.addEdg(arrayV[86],arrayV[87],65.0);
        //node_87
        graph.addEdg(arrayV[87],arrayV[115],175.0);
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
        graph.addEdg(arrayV[114],arrayV[86],70.0);
        //node_115
        graph.addEdg(arrayV[115],arrayV[29],175.0);
        //node_116
        //node_117
        graph.addEdg(arrayV[117],arrayV[85],70.0);
        return graph;
    }

}
