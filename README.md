# 记账应用

### 系统架构图



本系统包含了三个主要组成部分：View、ViewModel和Model。
View：包括三个主要的视图组件，分别是MainActivity、AccountEditActivity和ChartActivity。这些视图组件负责用户界面的展示和交互。
ViewModel：包括一个AccountViewModel，用于管理与用户界面相关的数据和业务逻辑。ViewModel负责处理与用户界面的交互，并通过LiveData与Model进行通信。
Model：包括多个模型组件，包括Account、AccountDao、AccountRepository、AccountRoomDatabase和Converters。这些模型组件负责数据的存储、访问和处理。Account是数据实体类，AccountDao是用于定义数据库操作的接口，AccountRepository是用于管理数据操作的中间层，AccountRoomDatabase是Room数据库实例，Converters是用于日期类型转换的类。
在系统架构中，View通过ViewModel与Model进行交互，ViewModel负责协调View和Model之间的数据流动和业务逻辑处理，而Model负责数据的存储和处理。



### 各模块开发人员

B21031914刘小渝：负责账单记录的增删改操作，主要负责AccountEditActivity的页面

B21032009邓裕澄：负责数据库AccountDao，AccountRepository，AccountRoomDatabase和模型类AcoountViewModel

B21032007陈天阳：负责MainActivity的所有显示和页面布局



### 各模块之间的关系

1. **MainActivity**：这是应用的主界面，它显示了用户的所有账户信息，包括总金额、收入和支出等。用户可以通过这个界面添加新的账户信息，查看和修改已有的账户信息，以及查看账户的图表。
2. **AccountEditActivity**：这个Activity是从MainActivity启动的，用于添加新的账户信息或者编辑已有的账户信息。当用户在MainActivity点击添加按钮或者点击一个已有的账户时，就会启动这个Activity。用户在这个界面输入或修改信息后，点击保存按钮，信息就会被保存到数据库中，并返回MainActivity。



### 优化点

1. **ChartActivity**：这个Activity也是从MainActivity启动的，用于显示账户信息的图表。当用户在MainActivity点击图表按钮时，就会启动这个Activity。这个界面会显示一个根据账户信息生成的图表，用户可以通过这个图表直观地了解到自己的收入和支出情况。

2. 给下拉列表设置样式，而不是使用默认样式
3. 可根据用户需求选择标准和科学两种模式
