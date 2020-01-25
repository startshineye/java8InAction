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

### 6.Java8实战视频-07Stream入门及Stream在JVM中的线程表现  
需求:
1.获取卡里绿小于400并且卡里绿从小到大的菜肴名称  

传统方法:  

```
public static List<String> getDishNamesByCollections(List<Dish> menu){
        List<String> result = new ArrayList<>();

        List<Dish> lowerMenu = new ArrayList<>();
        //1.获取卡里绿小于400的菜肴
        for (Dish dish:menu){
            if(dish.getCalories()<400){
                lowerMenu.add(dish);
            }
        }

        //2.排序
        Collections.sort(lowerMenu,(d1,d2)->Integer.compare(d1.getCalories(),d2.getCalories()));

        //3.获取名称
        for(Dish dish:lowerMenu){
            result.add(dish.getName());
        }
        return result;
    }
```

stream流方法:

```
public static List<String> getDishNamesByStream(List<Dish> menu){
        List<String>  result = new ArrayList<>();
       return menu.stream().filter((d)->d.getCalories()<400).sorted(Comparator.comparing((d)->d.getCalories())).map(Dish::getName).collect(toList());
}
```



因为 filter 、 sorted 、 map 和 collect 等操作是与具体线程模型无关的高层次构件，所以它们的内部实现可以是单线程的，也可能透明地充分利用你的多核架构！在实践中，这意味着你
用不着为了让某些数据处理任务并行而去操心线程和锁了，Stream API都替你做好了！    

default Stream<E> stream() {
        return StreamSupport.stream(this.spliterator(), false);
}

spliterator就是根据cpu的核数来进行并行处理   

我们怎样查看是根据并行处理，我们先看下getDishNamesByCollections，我们在getDishNamesByCollections添加线程睡眠100秒    

```
public static List<String> getDishNamesByCollections(List<Dish> menu){
        List<String> result = new ArrayList<>();

        List<Dish> lowerMenu = new ArrayList<>();
        //1.获取卡里绿小于400的菜肴
        for (Dish dish:menu){
            if(dish.getCalories()<400){
                lowerMenu.add(dish);
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //2.排序
        Collections.sort(lowerMenu,(d1,d2)->Integer.compare(d1.getCalories(),d2.getCalories()));

        //3.获取名称
        for(Dish dish:lowerMenu){
            result.add(dish.getName());
        }
        return result;
    }
```

传统方法:然后我们借助于jdk工具查看具体线程  
![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/1.png)  
在控制台命令打开java的console  
![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/2.png) 
查看对应堆栈和线程
![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/3.png) 
![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/4.png)   

stream流方法:借助于jdk工具查看具体线程,我们使用并行的流  

```
 public static List<String> getDishNamesByStream(List<Dish> menu){
        List<String>  result = new ArrayList<>();
       return menu.parallelStream().filter(
               (d)->{
                   try {
                       Thread.sleep(100000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   return d.getCalories()<400;
               }).sorted(Comparator.comparing((d)->d.getCalories())).map(Dish::getName).collect(toList());
}
```  

![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/5.png)   

### 8.Java8实战视频-08Stream知识点总结Stream源码阅读
#### 8.1 流到底是什么呢？
简短的定义就是“从支持数据处理操作的源生成的元素序列”。让我们一步步剖析这个定义。  
1. 1.元素序列（Dish）  
就像集合一样，流也提供了一个接口，可以访问特定元素类型的一组有序
值。因为集合是数据结构，所以它的主要目的是以特定的时间/空间复杂度存储和访问元
素（如 ArrayList 与 LinkedList ）。但流的目的在于表达计算，比如你前面见到的
filter 、 sorted 和 map 。集合讲的是数据，流讲的是计算。我们会在后面几节中详细解
释这个思想。
2. 2.源(menu)      
流会使用一个提供数据的源，如集合、数组或输入/输出资源。 请注意，从有序集
合生成流时会保留原有的顺序。由列表生成的流，其元素顺序与列表一致。
3. 3.数据处理操作(filter 、 map 、 reduce 、 find 、 match 、 sort 等)    
流的数据处理功能支持类似于数据库的操作，以及函数式编程语言中的常用操作，如 filter 、 map 、 reduce 、 find 、 match 、 sort 等。流操作可以顺序执
行，也可并行执行。  

#### 8.2 流两个重要的特点  
此外，流操作有两个重要的特点。
1. 1.流水线(pipelining)    
很多流操作本身会返回一个流，这样多个操作就可以链接起来，形成一个大的流水线。这让我们下一章中的一些优化成为可能，如延迟和短路。流水线的操作可以
看作对数据源进行数据库式查询。
2. 2.内部迭代(Internal iteration)  
与使用迭代器显式迭代的集合不同，流的迭代操作是在jdk内部进行的。  

 #### 8.3 stream操作简介  
   
 ![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/6.png)   
 
 1. 1.filter ——接受Lambda，从流中排除某些元素。在本例中，通过传递lambda d ->d.getCalories() > 300 ，选择出热量超过300卡路里的菜肴。  
 2. 2.map ——接受一个Lambda，将元素转换成其他形式或提取信息。在本例中，通过传递方法引用 Dish::getName ，相当于Lambda d -> d.getName() ，提取了每道菜的菜名。  
 3. 3.limit ——截断流，使其元素不超过给定数量。  
 4. 4.collect ——将流转换为其他形式。在本例中，流被转换为一个列表。它看起来有点儿像变魔术，我们在第6章中会详细解释 collect 的工作原理。现在，你可以把 collect 看作能够接受各种方案作为参数，并将流中的元素累积成为一个汇总结果的操作。这里的toList() 就是将流转换为列表的方案。  
 
 #### 6.4 流与集合
1. 1.粗略地说，集合与流之间的差异就在于什么时候进行计算。集合是一个内存中的数据结构，它包含数据结构中目前所有的值——集合中的每个元素都得先算出来才能添加到集合中。（你可
以往集合里加东西或者删东西，但是不管什么时候，集合中的每个元素都是放在内存里的，元素都得先算出来才能成为集合的一部分。）这个思想就是用户仅仅从流中提取需要的值，而这些值——在用
户看不见的地方——只会按需生成。这是一种生产者－消费者的关系。从另一个角度来说，流就像是一个延迟创建的集合：只有在消费者要求的时候才会计算值（用管理学的话说这就是需求驱动，甚至是实时制造)。   
与此相反，集合则是急切创建的（供应商驱动：先把仓库装满，再开始卖，就像那些昙花一现的圣诞新玩意儿一样）。以质数为例，要是想创建一个包含所有质数的集合，那这个程序算起
来就没完没了了，因为总有新的质数要算，然后把它加到集合里面。当然这个集合是永远也创建不完的，消费者这辈子都见不着了。

2. 2.相比之下，流则是在概念上固定的数据结构（你不能添加或删除元素），其元素则是按需计算的。  

3. 3.图4-3用DVD对比在线流媒体的例子展示了流和集合之间的差异。  
另一个例子是用浏览器进行互联网搜索。假设你搜索的短语在Google或是网店里面有很多匹配项。你用不着等到所有结果和照片的集合下载完，而是得到一个流，里面有最好的10个或20
个匹配项，还有一个按钮来查看下面10个或20个。当你作为消费者点击“下面10个”的时候，供应商就按需计算这些结果，然后再送回你的浏览器上显示。   

 ![](https://raw.githubusercontent.com/startshineye/img/master/2020/01/7.png)  
 
##### 1.只能遍历一次  

```
Stream<Dish> stream = menu.stream();
stream.forEach(System.out::println);
stream.forEach(System.out::println); 
```

打印出:
Dish(name=pork, vegetarian=false, calories=800, type=MEAT)
Dish(name=beef, vegetarian=false, calories=700, type=MEAT)
Dish(name=chicken, vegetarian=false, calories=400, type=MEAT)
Dish(name=french fries, vegetarian=true, calories=530, type=OTHER)
Dish(name=rice, vegetarian=true, calories=350, type=OTHER)
Dish(name=season fruit, vegetarian=true, calories=120, type=OTHER)
Dish(name=pizza, vegetarian=true, calories=550, type=OTHER)
Dish(name=prawns, vegetarian=false, calories=300, type=FISH)
Dish(name=salmon, vegetarian=false, calories=450, type=FISH)
Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
at java.util.stream.AbstractPipeline.sourceStageSpliterator(AbstractPipeline.java:279)
at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:580)
at com.yxm.stream.lesson6.SimpleStream.main(SimpleStream.java:34) 


##### 2.外部迭代与内部迭代

### 9.创建流  
1. 1.Collection.stream()  
2. 2.Stream.of(T... var0);  
3. 3.Arrays.stream(T[] var0)  
4. 4.Files.lines(path)  
5. 5.

### 11.Stream之filter，distinct，skip，limit，map，flatmap详细介绍.
#### 11.1 筛选和切片 
选择流中的元素：用谓词筛选，筛选出各不相同的元素，忽略流中的头几个元素，或将流截短至指定长度  

```
public class StreamFilter {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 6, 7, 7, 1);

        //1.filter过滤
        List<Integer> filterCollect = list.stream().filter(i -> i % 2 == 0).collect(toList());
        System.out.println("filterCollect:"+filterCollect);  //filterCollect:[2, 4, 6, 6]

        //2.去重
        List<Integer> distinctCollect = list.stream().distinct().collect(toList());
        System.out.println("distinctCollect:"+distinctCollect);  //distinctCollect:[1, 2, 3, 4, 5, 6, 7]

        //3.跳过前面几个元素
        List<Integer> skipCollect = list.stream().skip(5).collect(toList());
        System.out.println("skipCollect:"+skipCollect); //skipCollect:[6, 6, 7, 7, 1]

        //4.取前面几个元素
        List<Integer> limitCollect = list.stream().limit(5).collect(toList());
        System.out.println("limitCollect:"+limitCollect);  //limitCollect:[1, 2, 3, 4, 5]
    }
}
```

#### 11.2 映射
一个非常常见的数据处理套路就是从某些对象中选择信息。比如在SQL里，你可以从表中选择一列。Stream API也通过 map 和 flatMap 方法提供了类似的工具。  

```  
public class StreamMap {
    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 6, 7, 7, 1);

        //1.获取偶数
        List<Integer> collect = list.stream().map((i) -> i * 2).collect(toList());

        //2.返回Dish的菜名
        List<Dish> dishList = getDishList();
        dishList.stream().map(d->d.getName()).forEach(System.out::println);
        List<String> mapCollect1 = dishList.stream().map(d -> d.getName()).collect(toList());
        System.out.println(mapCollect1); //[pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon]

        //3.去除单词中相同的符号,并以字符串输出  
        String[] words = {"Hello","World"};

        //{H,e,l,l,o,W,o,r,l,d};
        Stream<String[]> stream = Arrays.stream(words).map(w -> w.split(""));
        //H,e,l,l,o,W,o,r,l,d
        Stream<String> stringStream = stream.flatMap(Arrays::stream);
        stringStream.distinct().forEach(System.out::println);

    }

    private static List<Dish>  getDishList(){
        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));
        return menu;
    }
}
```

#### 11.3 查找和匹配
  



   
  



  
  












