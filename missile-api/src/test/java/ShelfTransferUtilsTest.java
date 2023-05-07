import edu.szu.pojo.ShelfInfo;
import edu.szu.utils.ShelfTransferUtils;

public class ShelfTransferUtilsTest {
    public static void main(String[] args) {
        ShelfInfo shelfInfo = new ShelfInfo();
        shelfInfo.setShelfName("4-25");
        shelfInfo.setLayer(3);
        shelfInfo.setPosition("外");

        String result = ShelfTransferUtils.convert(shelfInfo);
        System.out.println(result);
    }
}
