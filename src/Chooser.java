import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 说明数组转换时，最好用List<E>代替E[]的原因。
 * 包含一个带有Collection类型参数的构造方法，以及一个返回随机Collection中元素的简单方法。
 * 下面是一个简单的泛型实现方式，这样就可以作为各种随机功能的数据源。
 *
 * 使用时，每次return前都要讲起转化一下，变成所需要的类型，如果转换出错，就很容易在运行时爆炸。
 * 根据item29的建议，我们将其改为泛型{@link Chooser2}
 *
 * @author LightDance
 * @date 2018/9/19
 */
public class Chooser {
    private final Object[] choiceArray;

    public Chooser(Collection choices) {
        choiceArray = choices.toArray();
    }

    public Object choose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }

    public static void main(String[] args) {
        Collection<String> list = new ArrayList<>();
        list.add("选项1");
        list.add("选项2");
        list.add("选项3");
        list.add("选项4");
        Chooser c = new Chooser(list);
        String result = (String) c.choose();
        System.out.println(result);
    }
}
