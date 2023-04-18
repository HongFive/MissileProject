
import edu.szu.GraphOfMatrix;
import edu.szu.pojo.AgvInfo;
import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.vo.NodePoint;
import edu.szu.pojo.vo.ReprintArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GraphTest {
    private NodePoint[] arrayV;//节点数组
    private double[][] Matrix;//邻接矩阵
    public static NodePoint[] inintialNode(){
        ReprintArea area_1=new ReprintArea(1,new ArrayList<AgvInfo>()); //上转载区
        ReprintArea area_2=new ReprintArea(2,new ArrayList<AgvInfo>()); //上转载区
        NodePoint node_1=new NodePoint(1,"1",new ArrayList<String>(),null,false,area_1);
        NodePoint node_2=new NodePoint(2,"2",new ArrayList<String>(),null,false,null);

        ShelfInfo shelf_4_1=new ShelfInfo(1,"1-1",1,"内",false,"4-1");
        ShelfInfo shelf_4_2=new ShelfInfo(1,"1-2",1,"外",false,"4-1");
        List<ShelfInfo> shelfInfos= Arrays.asList(shelf_4_1,shelf_4_2);
        NodePoint node_3=new NodePoint(3,"4",new ArrayList<String>(),shelfInfos,false,null);

        NodePoint node_4=new NodePoint(4,"23",new ArrayList<String>(),null,false,area_2);
        NodePoint[] arrayV=new NodePoint[]{node_1,node_2,node_3,node_4};
        return arrayV;
    }


    public static void main(String[] args) {
        NodePoint[] arrayV=inintialNode();
        GraphOfMatrix graph=new GraphOfMatrix(arrayV.length,true);
        HashMap<NodePoint, Integer> map=new HashMap<>();
        graph.initArray(arrayV);

        graph.addEdg(arrayV[0],arrayV[1],142.5);
        graph.addEdg(arrayV[1],arrayV[2],40.2881);
        graph.addEdg(arrayV[2],arrayV[3],164.5);

        graph.printGraph();
        System.out.println(graph.getDevOfV(arrayV[0]));
    }
}
