package com.github.android.sample;

// 放入package包中

// ====AIDL 规范：*/
// 1. AIDL中同一个包下的内容，如果用到，都需要显示导入；
// 2. 如果AIDL文件中用到了自定义的Parcelable类型，需要创建对应的类型的AIDL文件，如：Book；
// 3. AIDL中，除了基本数据类型，其他类型类型必须标上方向(in，out 或者 inout)，
//    a. in 表示输入型参数；
//    b. out 输出型参数；
//    c. inout 表示输入输出型；
// 4.AIDL 接口中，只能有方法，不能有静态常量；
// 5.与AIDL相关的类与文件，最好放入同一个包中，方便序列与反序列；

// 必要的导入，AIDL中同一个包下也需要导入
import com.github.android.sample.Book;

interface IBookManager {
    List<Book> getBookList();   // 从远程获取
    void addBook(in Book book);
}