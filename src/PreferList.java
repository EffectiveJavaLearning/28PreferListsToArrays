import java.util.ArrayList;
import java.util.List;

/**
 * List和Array主要区别有两点。
 *      1.数组是“协变(covariant)”的，意思是如果Sub是Super的子类，那么Sub[]也是Super[]的子类型。
 *      而相较之下，List则是不变的：对任意Type1,Type2，无论两者之间层次关系如何，List<Type1>
 *      和List<Type2>总是无关的，不存在父子类型关系。似乎看上去List存在不足之处，
 *      但实际上Array却更容易出问题，比如{@link DangerOfArray#main(String[])}编译时可能只有个不明显的警告，
 *      而运行时则发生异常；但用List的话这么干是无法通过编译的{@link #incompatibleTypesInit()}，
 *      我们当然更希望能在编译时找到问题并及时修改。
 *
 *      2.数组被“具体化(reified)”了.意思是数组在运行时知道并强制指定其元素类型，所以就像前面的例子那样，
 *      当向Long[]数组里面放String类型元素时，会扔异常出来。而泛型最后类型的确定则是通过擦除(erasure)
 *      实现的，仅在编译时强制指定其元素类型，并在运行时将元素类型信息丢弃(或称擦除erase)掉。
 *      这种机制能够让泛型与早先版本的原始类型相互操作，确保Java5引入泛型前后的代码协同运行时不出问题。
 *
 * 这些差异导致两者不太兼容：创建泛型类型数组，参数化泛型类型数组，或者泛型类型参数的数组都是非法的，
 * 例如new List<E>[] ,new List<String>[] ,new E[]这些，都会在编译时报出错误。
 *
 * 无法创建泛型数组的原因在于，这么做不是类型安全的(not typesafe)。假使这样合法，
 * 编译器在其他正常代码里面生成的强转也可能会在运行时扔出{@link ClassCastException}.
 * 这会违反泛型系统所提供的基本承诺。例如这些代码片段:
 *         // Why generic array creation is illegal - won't compile!
 *         List<String>[] stringLists = new List<String>[1]; // (1)
 *         List<Integer> intList = List.of(42);              // (2)
 *         Object[] objects = stringLists;                   // (3)
 *         objects[0] = intList;                             // (4)
 *         String s = stringLists[0].get(0);                 // (5)
 *
 *      假设line(1)合法，line(2)会创建并初始化一个List<Integer>型的数组；
 *      line(3)会由于数组的协变特性，会将List<String>数组存到 Object型变量中；
 *      line(4)又会将List<Integer>型对象存到objects的唯一元素里面，这一行会成功是由于泛型的erase机制，
 *      因为在运行时，List<Integer>的实例实际上仍然会被替换成List原始类型，而List<String>[]
 *      则变为List[],于是就不会有{@link ArrayStoreException}出现。
 *      于是，我们成功将一个 List<Integer>型的实例存到了List<String>型数组stringLists之中，
 *      然后在line(5)调用get()方法，编译器希望将get到的元素转化为字符串，但却遇到一个Integer实例，
 *      最终扔出{@link ClassCastException}. 为了避免这种情况，就会令其在编译时及时报错。
 *
 * 诸如E、List<E>和List<String>这些类型学名"不可具体化类型(non-reifiable types)"，
 * 它们运行时所包含的信息比编译时要少。由于擦除(erasure)机制，唯一可以具体化的参数化类型(parameterized type)
 * 是带有无界通配符的泛型，比如List<?>,Map<?,?>这些，虽然合法但几乎没什么用。
 *
 * by the way , 这样是可以的
 * List<String[]> arrayInList = new ArrayList<>();
 * 即泛型集合虽然不能跟数组混用，但数组类型却可以作为泛型参数用在泛型里面
 *
 * 没办法创建泛型数组有时候也会带来小小的不方便。
 *      首先，例如一般情况下，泛型不能够返回其使用的元素类型的数组(解决方案在item33)；
 *      其次，在使用varargs做参数的方法时也很可能会出现warnings(见item53)，因为编译时实际上会将
 *    varargs转化为数组，因此如果数组中元素是“不可具体化类型的”(non-reifiable)，那么就会被报一下warning.
 *    这个问题可以通过@SafeVarargs注解以解决(见item32)。
 *
 * 当进行数组转换，并收到generic array creation error或者unchecked cast warning时，
 * 考虑用List<E>代替E[]. 虽然可读性会受到一些影响，但至少可以保证类型安全并且能够跟其他类型相互操作。
 * 例如这个{@link Chooser}逐渐改进到{@link Chooser2}的例子，最终版本虽然可能更冗长、更慢，
 * 但至少运行时不会扔ClassCastException.
 *
 * 总之，数组和泛型规则有所不同：数组是协变的和具体化的；泛型是不变的且使用擦除机制。因此数组保证运行时类型安全，
 * 但编译时就难说；泛型则相反。所以一般不要把两者混在一起。如果不小心混在一起了然后想改，
 * 首先考虑将数组改为ArrayList等各种List进行替换。
 *
 *
 * @author LightDance
 * @date 2018/9/19
 */
public class PreferList {

    /**
     * 这样无法通过编译，会提示类型不匹配
     */
    private void incompatibleTypesInit(){
//        List<Object> objectList = new ArrayList<Long>();
//        objectList.add("fit failed");
    }

    private void uselessReifiable(){
        //通过，虽然没啥用
        List<?>[] wildcardList = new List<?>[10];
        //报错
//        List<String>[] strList = new List<String>[10];

    }

}
