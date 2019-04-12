import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 希望将{@link Chooser}中繁琐的手动类型强转通过泛型省去。
 * 类中最上面注释掉的部分是最初版本，由于类型不匹配通不过编译，于是需要手动强制转化成T[]型。
 * 但由于擦除(erasure)机制，运行时元素类型会被具体类型替代掉，因此编译器并不能推测出这样写没有问题，
 * 最后给出一个warning. 不过写代码的程序员可以证明这个地方没问题，然后用@SupressWarning注解消除它，
 * 并在旁边给出注释解释其原因。
 *
 * 不过如果可能的话，最好还是修改代码，从源头上解决掉这个warning.这里可以通过ArrayList的构造方法，
 * 将choices直接当作参数保存到ArrayList成员变量里面，以代替数组
 *
 * @author LightDance
 * @date 2018/9/19
 */


public class Chooser2<T> {
    // 版本1的写法，标注***的那一行有问题，无法通过编译
/*
    private final T[] choiceArray2;

    public ChooserP(Collection<T> choices) {
        ***choiceArray = choices.toArray();
    } // choose method unchanged
*/

    private final T[] choiceArray;

    private final List<T> choiceList;


    public Chooser2(Collection<T> choices) {

        //抑制warning的方式
        //noinspection unchecked
        @SuppressWarnings("unchecked")
        T[] tempArray = (T[])choices.toArray();
        choiceArray = tempArray;


        //消除warning原因的方式
        choiceList = new ArrayList<>(choices);
    } // choose method unchanged


    public T listChoose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceList.get(rnd.nextInt(choiceList.size()));
    }

    public T arrayChoose() {
        Random rnd = ThreadLocalRandom.current();
        return choiceArray[rnd.nextInt(choiceArray.length)];
    }

}
