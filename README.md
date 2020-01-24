# java8InAction
java8新特性实践

#### 1.应对变化的农场主要求  

##### 1.1 筛选出绿苹果  


##### 1.2 筛选多种颜色：浅绿色、暗红色、黄色等苹果  
农场主需求变化，他还想要筛选红苹果。
你该怎么做呢？简单的解决办法就是复制这个方法，把名字改成 filterRedApples ，然后更改
if 条件来匹配红苹果。然而，要是农民想要筛选多种颜色：浅绿色、暗红色、黄色等，这种方法
就应付不了了。一个良好的原则是在编写类似的代码之后，尝试将其抽象化。

##### 1.3 筛选出颜色、重量苹果  
农民又跑回来和你说：“要是能区分轻的苹果和重的苹果就太好了。重的苹果一般是重量大于150克”；
这种参数的变化就代表了需求的不断变化，怎么让方法内部满足调用者的各种要求，而且不需要更改很多东西；我们按照之前的就是使用策略模式。  
我们定义一个接口：AppleFilter;一种是定义多种方法，但是定义的多种方法局限性比较大，满足不了负责业务需求，所以最好的方法是使用匿名内部类

你需要一种比添加很多参数更好的方法来应对变化的需求。让
我们后退一步来看看更高层次的抽象。一种可能的解决方案是对你的选择标准建模：你考虑的
是苹果，需要根据 Apple 的某些属性（比如它是绿色的吗？重量超过150克吗？）来返回一个
boolean 值。我们把它称为谓词（即一个返回 boolean 值的函数）。让我们定义一个接口来对选
择标准建模：

```
public interface AppleFilter{
        boolean filter(Apple apple);
 }
```  

你可以把这些标准看作 filter 方法的不同行为。你刚做的这些和“策略设计模式”
① 相关，
它让你定义一族算法，把它们封装起来（称为“策略”），然后在运行时选择一个算法。在这里，
算法族就是 AppleFilter ，不同的策略就是 GreenAnd160WeightFilter 和 YellowLess150WeightFilter。
但是，该怎么利用 AppleFilter 的不同实现呢？你需要 findApples 方法接受
AppleFilter 对象，对 Apple 做条件测试。这就是行为参数化：让方法接受多种行为（或战
略）作为参数，并在内部使用，来完成不同的行为。

``` 
public static List<Apple> findApples(List<Apple> appleList,AppleFilter appleFilter){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:appleList){
            if(appleFilter.filter(apple)){
                result.add(apple);
            }
        }
        return result;
    }
```   

你已经做成了一件很酷的事:findApples方法的行为取决于你通过AppleFilter对象传递的代码。换句话说，你把findApples方法的行为参数化了！  

请注意，在上一个例子中，唯一重要的代码是 filter 方法的实现，如下所示；

```   
 public static class GreenAnd160WeightFilter implements AppleFilter {
        @Override
        public boolean filter(Apple apple) {
            return (apple.getColor().equals("green") && apple.getWeight() >= 160);
        }
    }

    public static class YellowLess150WeightFilter implements AppleFilter {
        @Override
        public boolean filter(Apple apple) {
            return (apple.getColor().equals("yellow") && apple.getWeight() < 150);
        }
    }
```   

1. 1.正是它定义了 findApples 方法的新行为。但令人遗憾的是，由于该 filterApples 方法只能接受对象，
所以你必须把代码包裹在 AppleFilter 对象里。你的做法就类似于在内联“传递代码”，因
为你是通过一个实现了 filter 方法的对象来传递布尔表达式的。你将在2.3节（第3章中有更详细的
内容）中看到，通过使用Lambda，你可以直接把表达式 "red".equals(apple.getColor())
&&apple.getWeight() > 150 传递给 filterApples 方法，而无需定义多个 ApplePredicate
类，从而去掉不必要的代码。
2. 2.正如我们先前解释的那样，行为参数化的好处在于你可以把迭代要筛选的集合的逻辑与对集合中每个元素应用的行为区分开来。这样你可以重复使用同一个方法，给它不同的行为来达到不同的目的

#### 2.lambda表达式初探  

##### 2.1 不使用lambda的缺点
1.不管是使用匿名内部类或者是产生子类(使用定义一个类去实现一个接口)代码都是比较累赘的  
2.代码容易混淆  
3.使用lambda表达式比使用匿名内部类或者产生子类更节约内存空间(可通过监控java8和java7在运行idea时候 内存分配情况)  

##### 2.2 使用lambda
函数式接口:只含有一个抽象方法的接口，为了限制接口中方法的个数，我们使用注解限制。

```   
public class FilterApple {

    //3.匿名内部类调用
    @FunctionalInterface
    public interface AppleFilter{
        boolean filter(Apple apple);
    }

    public static List<Apple> findApples(List<Apple> appleList, AppleFilter appleFilter){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:appleList){
            if(appleFilter.filter(apple)){
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args){
        /**
         * 苹果仓储
         */
        List<Apple> storeList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        List<Apple> innerApples = findApples(storeList, new AppleFilter() {
            @Override
            public boolean filter(Apple apple) {
                return "green".equals(apple.getColor());
            }
        });
        System.out.println("innerApples:"+innerApples);

        List<Apple> apples = findApples(storeList, (Apple apple) -> {
            return "green".equals(apple.getColor());
        });

        System.out.println("lambda:"+apples);
    }
}
```   

##### 2.3 之前很多接口都是函数式接口  

```   
@FunctionalInterface
public interface Runnable {
    void run();
}
```   

我们以:Runnable为例:

```   
 public static void main(String[] args) throws Exception{
        /**
         * 苹果仓储
         */
        List<Apple> storeList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("本线程名称:"+Thread.currentThread().getName());
            }
        }).start();

        new Thread(()-> System.out.println("本线程名称:"+Thread.currentThread().getName())).start();

        Thread.currentThread().join();
    }
```   


#### 3.lambda语法精讲  

##### 3.1 lambda前言  
1. 1.利用行为参数化来传递代码有助于应对不断变化的需求。它允许你定义一个代码块来表示一个行为，然后传递它  
2. 2.可以决定在某一事件发生时（例如单击一个按钮）或在算法中的某个特定时刻（例如筛选算法中类似于“重量超过150克的苹果”的谓词，或排序中的自定义比较操作）运行该代码块
一般来说，利用这个概念，你就可以编写更为灵活且可重复使用的代码了。
##### 3.2 Lambda表达式  
1. 1.Lambda表达式，它可以让你很简洁地表示一个行为或传递代码  
2. 2.可以把Lambda表达式看作匿名功能，它基本上就是没有声明名称的方法；但和匿名类一样，它也可以作为参数传递给一个方法。  

语法:  
(parameters) -> expression  
(parameters) -> {statements;}  
            参数列表                    ->箭头   函数体  
Comparator<Apple> comparator3 = (o1,o2) -> o1.getColor().compareTo(o2.getColor());

()->{}  
()->{return "Hello";}  
()->{return "Hello World";}  
(Integer s)-> return "Alex"+i   //invalid  
(String s)->{return "Hello Alex"}    

##### 3.3 在哪里以及如何使用 Lambda
你可以在函数式接口上使用Lambda表达式  

##### 3.4 用函数式接口可以干什么呢？  
Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例（具体说来，是函数式接口一个具体实现
的实例）。  

### 4.Lambda使用深入解析  

#### 4.1 三种等效的输出  

```
public class LambdaUsage {
    public static void main(String[] args){
        //lambada表达式就是:函数式接口的实例

        //1.定义一个lambda表达式:
        Runnable r1 = () -> System.out.println("Hello");

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello");
            }
        };
        process(r1);
        process(r2);
        process(()->System.out.println("Hello"));

    }

    public static void process(Runnable r){
          r.run();
    }
}
```  

1. 1.现在，Lambda表达式可以被赋给一个变量，或传递给"一个接受函数式接口"作为参数的方法  


#### 4.2 常见函数式接口  

##### 1.Predicate  
说明:java.util.function.Predicate<T> 接口定义了一个名叫 test 的抽象方法，它接受泛型T对象，并返回一个boolean。  
使用场景:在你需要表示一个涉及类型 T 的布尔表达式时，就可以使用这个接口。  

```  
public class LambdaUsage {

    private static List<Apple> findApples(List<Apple> list, Predicate<Apple> predicate){
        List<Apple> result = new ArrayList<>();
         for(Apple apple:list){
             if(predicate.test(apple)){
              result.add(apple);
             }
         }
        return result;
    }

    private static List<Apple> findApplesByLongPredicate(List<Apple> list, LongPredicate predicate){
        List<Apple> result = new ArrayList<>();
         for(Apple apple:list){
             if(predicate.test(apple.getWeight())){
              result.add(apple);
             }
        }
        return result;
    }

    private static List<Apple> findApplesByBiPredicate(List<Apple> list, BiPredicate<String,Long> biPredicate){
        List<Apple> result = new ArrayList<>();
        for(Apple apple:list){
            if(biPredicate.test(apple.getColor(),apple.getWeight())){
                result.add(apple);
            }
        }
        return result;
    }


    public static void main(String[] args){
        List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));
        List<Apple> greenList = findApples(appleList, (apple) -> apple.getColor().equals("green"));//boolean判断
        System.out.println("Predicate:"+greenList);

        List<Apple> applesByLongPredicate = findApplesByLongPredicate(appleList, (w) -> w > 160);
        System.out.println("LongPredicate:"+applesByLongPredicate);

        List<Apple> applesByBiPredicate = findApplesByBiPredicate(appleList, (s, w) -> s.equals("green") && w > 100);
        System.out.println("BiPredicate:"+applesByBiPredicate);
    }
}  
```  



##### 2.Consumer  
说明:java.util.function.Consumer<T> 定义了一个名叫 accept 的抽象方法，它接受泛型T的对象，没有返回（ void ）。   
使用场景:你如果需要访问类型 T 的对象，并对其执行某些操作，就可以使用这个接口。比如，你可以用它来创建一个 forEach 方法，接受一个 Integers 的列表，并对其中
每个元素执行操作。在下面的代码中，你就可以使用这个 forEach 方法，并配合Lambda来打印列表中的所有元素。  

```
public class LambdaUsage {
    private static List<Apple> simpleConsumer(List<Apple> list, Consumer<Apple> consumer){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:list){
            consumer.accept(apple);
            result.add(apple);
        }
       return result;
    }

    private static List<Apple> simpleBiConsumer(List<Apple> list, BiConsumer<Apple,Long> biConsumer){
        List<Apple> result = new ArrayList<>();
        for (Apple apple:list){
            biConsumer.accept(apple,apple.getWeight());
            result.add(apple);
        }
        return result;
    }

    public static void main(String[] args){
        List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        List<Apple> consumerList = simpleConsumer(appleList, (apple) -> System.out.println(apple));
        System.out.println("consumerList:"+consumerList);

        List<Apple> simpleBiConsumer = simpleBiConsumer(appleList, (a, s) -> System.out.println(s + a.getColor() + ":Weight=>" + a.getWeight()));
        System.out.println("simpleBiConsumer:"+simpleBiConsumer);
    }
}   
```  


##### 3.Function  
说明:java.util.function.Function<T, R> 接口定义了一个叫作 apply 的方法，它接受一个泛型 T 的对象，并返回一个泛型 R 的对象。  
使用场景:如果你需要定义一个Lambda，将输入对象的信息映射到输出，就可以使用这个接口（比如提取苹果的重量，或把字符串映射为它的长度）。  

```
public class LambdaUsage {
    private static Apple testFunction(Apple apple, Function<Apple,String> function){
        function.apply(apple);
        return apple;
    }

    public static void main(String[] args){
        List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));

        Apple apple = testFunction(new Apple("yellow", 200), (a) -> a.toString());
        System.out.println("functionApple:"+apple);
        }
 }
 ```  
 
 
 ##### 4.Supplier  
  Supplier<T> ，你可以用
 前面建议的新操作符将其改写为 () => T ，这进一步佐证了由于简单数据类型（primitive type）
 与对象类型（object type）的差异所导致的分歧  


 ```
 public class LambdaUsage {
 private static Apple createApple(Supplier<Apple> supplier) {
         return supplier.get();
     }
 
     public static void main(String[] args){
         List<Apple> appleList = Arrays.asList(new Apple("green", 150), new Apple("red", 160), new Apple("green", 170));
 
         Apple green = createApple(() -> new Apple("green", 100));
         System.out.println("green:"+green);
        }
 }
 ```

 ##### 5.Lambda方法推导  
方法引用主要有三类。
(1) 指向静态方法的方法引用（例如 Integer 的 parseInt 方法，写作 Integer::parseInt ）。
(2) 指向任意类型实例方法的方法引用(例如:String的length方法，写作String::length)。  
(3) 指向现有对象的实例方法的方法引用（假设你有一个局部变量expensiveTransaction用于存放Transaction类型的对象，它支持实例方法getValue那么你就可以写expensive-Transaction::getValue ）。

```
public class MethodReference {
    public static void main(String[] args){
        //1.书写lambda表达式，然后在其他方法中调用
        Consumer<String> consumer = (s)->System.out.println(s);
        useConsumer(consumer,"Hello Tom");

        //2.直接使用lambda表达式
        useConsumer((s)->System.out.println(s),"Hello Tom");

        //3.使用函数式签名
        useConsumer(System.out::println,"Hello Tom");

        //4.使用lambda表达式对集合进行排序
        List<Apple> appleList = Arrays.asList(new Apple("Green", 100), new Apple("red", 200), new Apple("blue", 300));
        System.out.println("appleList before sort:"+appleList);
        appleList.sort((a1,a2)->a1.getColor().compareTo(a2.getColor()));
        System.out.println("appleList after sort:"+appleList);

        //5.循环遍历打印出appleList
        appleList.stream().forEach(System.out::println);

        //类::静态方法
        Function<String, Integer> f = Integer::parseInt;
        Integer apply1 = f.apply("123");
        System.out.println(apply1);

        //类::实例方法
        BiFunction<String, Integer, Character> f2 = String::charAt;
        Character apply2 = f2.apply("Hello", 2);
        System.out.println("f2:"+apply2);

        //对象::实例
        String str = new String("Hello");
        Function<Integer, Character> f3 = str::charAt;
        Character apply3 = f3.apply(2);
        System.out.println("f3:"+apply3);

        BiFunction<String, Integer, Apple> appleBiFunction = Apple::new;
        Apple apple = appleBiFunction.apply("red", 100);
        System.out.println("appleBiFunction:"+apple);
    }
```



