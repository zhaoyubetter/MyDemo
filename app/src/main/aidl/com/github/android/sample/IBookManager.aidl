package com.github.android.sample;

// 放入package包中

// 必要的导入
import com.github.android.sample.Book;

interface IBookManager {
    List<Book> getBookList();   // 从远程获取
    void addBook(in Book book);
}