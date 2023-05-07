package edu.szu.utils;

import edu.szu.pojo.ShelfInfo;
import edu.szu.pojo.ZktWarehouseShelf;


public class ShelfTransferUtils {
    public static String convert(ShelfInfo shelfInfo){
        String wareName = "";
        String shelfName = shelfInfo.getShelfName();
        String[] shelfs = shelfName.split("-");
        if (shelfs[0].equals("1") || shelfs[0].equals("2")){
            wareName += "A-";
        }else{
            wareName += "B-";
        }
        int front = Integer.parseInt(shelfs[0]);
        int back = Integer.parseInt(shelfs[1]);
        if(front % 2 != 0){
            if(back/10 > 0){
                wareName += "0";
                wareName += back;
            }else{
                wareName += "00";
                wareName += back;
            }
        }else{
            wareName += "0";
            back += 25;
            wareName += back;

        }
        wareName += "-";

        int layer = shelfInfo.getLayer();
        wareName += layer;
        wareName += "-";
        String position = shelfInfo.getPosition();
        if(position.equals("内")){
            wareName += "0";
        }else{
            wareName += "1";
        }
        return wareName;
    }

    public static String reconvert(ZktWarehouseShelf zktWarehouseShelf){
        String shelfName = "";
        String wareName = zktWarehouseShelf.getWarehouseNum();
        String[] ware = wareName.split("-");
        String first = ware[0];
        int second = Integer.parseInt(ware[1]);
        if(first.equals("A") && second <= 25){
            shelfName += "1-";
        }else if(first.equals("A") && second > 25){
            shelfName += "2-";
        }else if(first.equals("B") && second <= 25){
            shelfName += "3-";
        }else if(first.equals("B") && second > 25){
            shelfName += "4-";
        }
        int hang = Integer.parseInt(zktWarehouseShelf.getShelfNum());
        hang = hang % 25 ==0 ? 25:hang%25;
        shelfName += hang;
        return shelfName;
    }
    public static ShelfInfo convert(ZktWarehouseShelf zktWarehouseShelf){
        ShelfInfo shelfInfo = new ShelfInfo();
        shelfInfo.setId(zktWarehouseShelf.getId());
        if(zktWarehouseShelf.getPosition().equals("0")){
            shelfInfo.setPosition("内");
        }else{
            shelfInfo.setPosition("外");
        }
        shelfInfo.setLayer(Integer.valueOf(zktWarehouseShelf.getLayerNum()));
        if(zktWarehouseShelf.getStatus().equals("0")){
            shelfInfo.setState(false);
        }else{
            shelfInfo.setState(true);
        }
        shelfInfo.setShelfName(ShelfTransferUtils.reconvert(zktWarehouseShelf));

        return shelfInfo;
    }
}
