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

    public NodePoint[] inintialNode(List<AgvInfo> agvInfoList, List<ShelfInfo> shelfInfoList){

        ArrayList<NodePoint> arrayV=new ArrayList<>();

        //转载区信息
        ArrayList<AgvInfo> agvs_up=new ArrayList<>();
        ArrayList<AgvInfo> agvs_down=new ArrayList<>();
        for (AgvInfo agv:agvInfoList){
            String[] temp=agv.getLocation().split("-");
            if (temp[0].equals("25")) agvs_up.add(agv);
            if (temp[0].equals("26")) agvs_down.add(agv);
        }
        ReprintArea area_1 = new ReprintArea(33,agvs_up); //上停放区
        ReprintArea area_2 = new ReprintArea(35,agvs_down); //下停放区



        //初始化节点信息
//        NodePoint node_25 = new NodePoint(0,"25",map0,null,FREE,area_1);
        //node_0 上装载区，点位27
        LinkedHashMap<String,Double> map0 = new LinkedHashMap<>();
        NodePoint node_0 = new NodePoint(0,"27",map0,null,FREE,null);
        arrayV.add(node_0);

        LinkedHashMap<String,Double> map1=new LinkedHashMap<>();
        for (int j = 1; j <=15; j++) {
            map1.put("1-"+j,10.0);
        }
        NodePoint node_1 = new NodePoint(1,"1",map1,null,FREE,null);

        LinkedHashMap<String,Double> map2=new LinkedHashMap<>();
        for (int j = 1; j <=13; j++) {
            map2.put("2-"+j,10.0);
        }
        map2.put("2-14",6.5);
        NodePoint node_2 = new NodePoint(2,"2",map2,null,FREE,null);

        LinkedHashMap<String,Double> map3=new LinkedHashMap<>();
        for (int j = 1; j <= 4; j++) {
            map2.put("3-"+j,10.0);
        }
        map2.put("3-5",9.5);
        NodePoint node_3 = new NodePoint(3,"3",map3,null,FREE,null);

        arrayV.add(node_1);
        arrayV.add(node_2);
        arrayV.add(node_3);

        //路径4货架信息初始化
        ArrayList<ShelfInfo> shelfs=new ArrayList<>();
        ArrayList<ShelfInfo> remove_list=new ArrayList<>();
        int i=1;
        int count=1;
        int idNum=4;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map4_=new LinkedHashMap<>();
                map4_.put("4-"+i,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("4-"+i);
                }
                NodePoint node_4_=new NodePoint(idNum++,"4",map4_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_4_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        ArrayList<ShelfInfo> tempShelfs = new ArrayList<>(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map29=new LinkedHashMap<>();
        for (int j = 4; j >1; j--) {
            map29.put("5-"+j,10.0);
        }
        map29.put("5-1",9.0);
        NodePoint node_29 = new NodePoint(29,"5",map29,null,FREE,null);

        LinkedHashMap<String,Double> map30=new LinkedHashMap<>();
        for (int j = 17; j >= 1; j--) {
            map30.put("6-"+j,10.0);
        }
        NodePoint node_30 = new NodePoint(30,"6",map30,null,FREE,null);

        LinkedHashMap<String,Double> map31=new LinkedHashMap<>();
        for (int j = 18; j >= 1; j--){
            map31.put("7-"+j,10.0);
        }
        NodePoint node_31 = new NodePoint(31,"7",map31,null,FREE,null);

        LinkedHashMap<String,Double> map32=new LinkedHashMap<>();
        for (int j = 1; j <= 4; j++) {
            map32.put("8-"+j,10.0);
        }
        map32.put("8-5",9.5);
        NodePoint node_32 = new NodePoint(32,"8",map32,null,FREE,null);
        arrayV.add(node_29);
        arrayV.add(node_30);
        arrayV.add(node_31);
        arrayV.add(node_32);

        //路径9货架信息初始化
        i=1;
        count=1;
        idNum=33;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map9_=new LinkedHashMap<>();
                map9_.put("9-"+i,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("9-"+i);
                }
                NodePoint node_9_=new NodePoint(idNum++,"9",map9_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_9_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        tempShelfs.addAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map58=new LinkedHashMap<>();
        for (int j = 4; j >1; j--) {
            map58.put("10-"+j,10.0);
        }
        map58.put("10-1",6.6);
        NodePoint node_58 = new NodePoint(58,"10",map58,null,FREE,null);

        LinkedHashMap<String,Double> map59=new LinkedHashMap<>();
        for (int j = 1; j <=15; j++) {
            map59.put("11-"+j,10.0);
        }
        NodePoint node_59 = new NodePoint(59,"11",map59,null,FREE,null);

        LinkedHashMap<String,Double> map60=new LinkedHashMap<>();
        for (int j = 1; j <=14; j++) {
            map60.put("12-"+j,10.0);
        }
        NodePoint node_60 = new NodePoint(60,"12",map60,null,FREE,null);

        LinkedHashMap<String,Double> map61=new LinkedHashMap<>();
        for (int j = 1; j <= 7; j++) {
            map61.put("13-"+j,10.0);
        }
        NodePoint node_61 = new NodePoint(61,"13",map61,null,FREE,null);

        LinkedHashMap<String,Double> map62=new LinkedHashMap<>();
        for (int j = 1; j <= 6; j++) {
            map62.put("14-"+j,10.0);
        }
        map62.put("14-7",12.0);
        NodePoint node_62 = new NodePoint(62,"14",map62,null,FREE,null);
        arrayV.add(node_58);
        arrayV.add(node_59);
        arrayV.add(node_60);
        arrayV.add(node_61);
        arrayV.add(node_62);

        //路径15货架信息初始化
        i=1;
        count=1;
        idNum=63;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map15_=new LinkedHashMap<>();
               if (i==25){
                    map15_.put("15-"+i,13.5);
                }
                else map15_.put("15-"+i,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("15-"+i);
                }
                NodePoint node_15_=new NodePoint(idNum++,"15",map15_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_15_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        tempShelfs.addAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map88=new LinkedHashMap<>();
        for (int j = 6; j >= 1; j--) {
            map88.put("16-"+j,10.0);
        }
        NodePoint node_88 = new NodePoint(88,"16",map88,null,FREE,null);

        LinkedHashMap<String,Double> map89=new LinkedHashMap<>();
        map89.put("17-1",15.0);
        for (int j = 6; j > 1; j--) {
            map89.put("17-"+j,10.0);
        }
        NodePoint node_89 = new NodePoint(89,"17",map89,null,FREE,null);

        LinkedHashMap<String,Double> map90=new LinkedHashMap<>();
        for (int j = 17; j >= 1; j--) {
            map90.put("18-"+j,10.0);
        }
        NodePoint node_90 = new NodePoint(90,"18",map90,null,FREE,null);

        LinkedHashMap<String,Double> map91=new LinkedHashMap<>();
        for (int j = 18; j >= 1; j--) {
            map91.put("19-"+j,10.0);
        }
        NodePoint node_91 = new NodePoint(91,"19",map91,null,FREE,null);

        LinkedHashMap<String,Double> map92=new LinkedHashMap<>();
        for (int j = 1; j <= 7; j++) {
            map92.put("20-"+j,10.0);
        }
        NodePoint node_92 = new NodePoint(92,"20",map92,null,FREE,null);

        LinkedHashMap<String,Double> map93=new LinkedHashMap<>();
        for (int j = 1; j <= 6; j++) {
            map92.put("21-"+j,10.3);
        }
        map92.put("21-7",22.3);
        NodePoint node_93 = new NodePoint(93,"21",map93,null,FREE,null);

        arrayV.add(node_88);
        arrayV.add(node_89);
        arrayV.add(node_90);
        arrayV.add(node_91);
        arrayV.add(node_92);
        arrayV.add(node_93);


        //路径22货架信息初始化
        i=1;
        count=1;
        idNum=94;
        for (ShelfInfo shelf:shelfInfoList){
            shelfs.add(shelf);
            remove_list.add(shelf);
            if (count%6==0){
                LinkedHashMap<String,Double> map22_=new LinkedHashMap<>();
                 if (i==25){
                    map22_.put("22-"+i,13.5);
                }
                else map22_.put("22-"+i,10.0);
                for (ShelfInfo tempshelf:shelfs){
                    tempshelf.setLocation("22-"+i);
                }
                NodePoint node_22_=new NodePoint(idNum++,"22",map22_,new ArrayList<>(shelfs),FREE,null);
                arrayV.add(node_22_);
                shelfs.clear();
                i++;
            }
            if (count==150) break;
            count++;
        }
        shelfInfoList.removeAll(remove_list);
        tempShelfs.addAll(remove_list);
        shelfs.clear();
        remove_list.clear();

        LinkedHashMap<String,Double> map119=new LinkedHashMap<>();
        for (int j = 6; j > 1; j--) {
            map119.put("23-"+j,10.0);
        }
        map119.put("23-1",15.0);

        NodePoint node_119 = new NodePoint(119,"23",map119,null,FREE,null);

        LinkedHashMap<String,Double> map120=new LinkedHashMap<>();
        for (int j = 6; j > 1; j--) {
            map120.put("24-"+j,10.0);
        }
        map119.put("24-1",15.0);

        NodePoint node_120 = new NodePoint(120,"24",map120,null,FREE,null);

        arrayV.add(node_119);
        arrayV.add(node_120);

        LinkedHashMap<String,Double> map121=new LinkedHashMap<>();
        for (int j = 1; j <= 15; j++) {
            map121.put("25-" + j, 10.0);
        }
        NodePoint node_121 = new NodePoint(121,"25",map121,null,FREE,area_1);
        arrayV.add(node_121);

        LinkedHashMap<String,Double> map122 = new LinkedHashMap<>();
        for (int j = 18; j >= 1; j--) {
            map122.put("26-"+j,10.0);
        }
        NodePoint node_122 = new NodePoint(122,"26",map122,null,FREE,area_2);
        arrayV.add(node_122);

        //node_123 下装载区，点位28
        LinkedHashMap<String,Double> map123 = new LinkedHashMap<>();
        NodePoint node_123 = new NodePoint(123,"28",map123,null,FREE,null);
        arrayV.add(node_123);

        int size=arrayV.size();
        shelfInfoList.addAll(tempShelfs);
        NodePoint[] array = arrayV.toArray(new NodePoint[size]);
        return array;
    }


    public MyGraph initialGraph(List<AgvInfo> agvInfoList,List<ShelfInfo> shelfInfoList){
        NodePoint[] arrayV=inintialNode(agvInfoList,shelfInfoList);
        MyGraph graph=new MyGraph(arrayV.length,true);
        graph.arrayV=arrayV;
        graph.initArray(arrayV);
        //node_0
        graph.addEdg(arrayV[0],arrayV[59],150);
        //node_1
        graph.addEdg(arrayV[1],arrayV[0],150);
        //node_2
        graph.addEdg(arrayV[2],arrayV[1],136.5);
        //node_3
        graph.addEdg(arrayV[3],arrayV[2],40.0);
        graph.addEdg(arrayV[3],arrayV[4],9.5);
        //node_4
        graph.addEdg(arrayV[4],arrayV[3],9.5);
        graph.addEdg(arrayV[4],arrayV[5],10.0);
        graph.addEdg(arrayV[4],arrayV[33],10.0);

        //路径编号4 边初始化
        for (int i=5;i<28;i++){
            graph.addEdg(arrayV[i],arrayV[i-1],10.0);
            graph.addEdg(arrayV[i],arrayV[i+1],10.0);
            graph.addEdg(arrayV[i],arrayV[i+29],10.0);
        }

        //node_28
        graph.addEdg(arrayV[28],arrayV[27],10.0);
        graph.addEdg(arrayV[28],arrayV[57],10.0);

        //node_29
        graph.addEdg(arrayV[29],arrayV[28],10.0);
        graph.addEdg(arrayV[29],arrayV[58],8.2);
        //node_30
        graph.addEdg(arrayV[30],arrayV[29],39);
        graph.addEdg(arrayV[30],arrayV[120],65);
        //node_31
        graph.addEdg(arrayV[31],arrayV[30],170.0);
        //node_32
        graph.addEdg(arrayV[32],arrayV[33],9.5);
        graph.addEdg(arrayV[32],arrayV[3],8.2);
        //node_33
        graph.addEdg(arrayV[33],arrayV[32],9.5);
        graph.addEdg(arrayV[33],arrayV[4],10.0);
        graph.addEdg(arrayV[33],arrayV[34],10.0);

        //路径编号9 边初始化
        for (int i=34;i < 57;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i - 29], 10.0);
        }

        //node_57
        graph.addEdg(arrayV[57],arrayV[28],10.0);
        graph.addEdg(arrayV[57],arrayV[56],10.0);
        //node_58
        graph.addEdg(arrayV[58],arrayV[57],10.0);
        //node_59
        graph.addEdg(arrayV[59],arrayV[60],136.5);
        graph.addEdg(arrayV[59],arrayV[121],10.0);
        //node_60
        graph.addEdg(arrayV[60],arrayV[61],70.0);
        graph.addEdg(arrayV[60],arrayV[32],52.0);
        //node_61
        graph.addEdg(arrayV[61],arrayV[62],60.0);
        //node_62
        graph.addEdg(arrayV[62],arrayV[93],9.0);
        graph.addEdg(arrayV[62],arrayV[63],12.0);
        //node_63
        graph.addEdg(arrayV[63],arrayV[94],10.0);
        graph.addEdg(arrayV[63],arrayV[64],10.0);

        //路径编号15 边初始化
        for (int i=64;i<87;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 31], 10.0);
        }

        //node_87
        graph.addEdg(arrayV[87],arrayV[86],10.0);
        graph.addEdg(arrayV[87],arrayV[118],10.0);
        graph.addEdg(arrayV[87],arrayV[88],13.5);

        //node_88
        graph.addEdg(arrayV[88],arrayV[87],13.5);
        graph.addEdg(arrayV[88],arrayV[89],60.0);
        //node_89
        graph.addEdg(arrayV[89],arrayV[90],65.0);
        //node_90
        graph.addEdg(arrayV[90],arrayV[91],170.0);
        //node_91
        graph.addEdg(arrayV[91],arrayV[31],10.0);
        graph.addEdg(arrayV[91],arrayV[122],10.0);
        graph.addEdg(arrayV[91],arrayV[123],180.0);

        //node_93
        graph.addEdg(arrayV[93],arrayV[94],12.0);
        //node_94
        graph.addEdg(arrayV[94],arrayV[63],10.0);
        graph.addEdg(arrayV[94],arrayV[95],10.0);

        //路径编号22 边初始化
        for (int i=95;i<118;i++) {
            graph.addEdg(arrayV[i], arrayV[i - 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i + 1], 10.0);
            graph.addEdg(arrayV[i], arrayV[i - 31], 10.0);
        }

        //node_118
        graph.addEdg(arrayV[118],arrayV[87],10.0);
        graph.addEdg(arrayV[118],arrayV[117],10.0);
        graph.addEdg(arrayV[118],arrayV[119],13.5);
        //node_119
        graph.addEdg(arrayV[119],arrayV[88],9.0);
        graph.addEdg(arrayV[119],arrayV[118],13.5);
        //node_120
        graph.addEdg(arrayV[120],arrayV[119],65.0);
        //node_121
        graph.addEdg(arrayV[121],arrayV[59],10.0);
        //node_122
        graph.addEdg(arrayV[122],arrayV[91],10.0);
        //node_123
        graph.addEdg(arrayV[123],arrayV[31],180.0);

        return graph;
    }

}
