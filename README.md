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

正是它定义了 findApples 方法的新行为。但令人遗憾的是，由于该 filterApples 方法只能接受对象，
所以你必须把代码包裹在 AppleFilter 对象里。你的做法就类似于在内联“传递代码”，因
为你是通过一个实现了 filter 方法的对象来传递布尔表达式的。你将在2.3节（第3章中有更详细的
内容）中看到，通过使用Lambda，你可以直接把表达式 "red".equals(apple.getColor())
&&apple.getWeight() > 150 传递给 filterApples 方法，而无需定义多个 ApplePredicate
类，从而去掉不必要的代码。


