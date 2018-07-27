// INewBookAddListener.aidl
package com.github.android.sample;

import com.github.android.sample.Book;

// Declare any non-default types here with import statements


interface INewBookAddListener {
    void onNewBookAdd(in Book book);
}
