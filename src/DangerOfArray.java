import java.util.List;

/**
 * 介绍Array的风险，方便与List进行对照
 *
 * @author LightDance
 * @date 2018/9/19
 */
public class DangerOfArray {
    public static void main(String[] args) {
        // Fails at runtime!
        Object[] objectArray = new Long[1];
        // Throws ArrayStoreException
        objectArray[0] = "I don't fit in";
    }

    private void badFragment(){

    }
}
