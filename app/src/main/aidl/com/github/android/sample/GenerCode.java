package com.github.android.sample;

import android.os.RemoteException;

/**
 * 生成的代码
 * AIDL支持的数据类型：
 * <p>
 * 参考：https://www.cnblogs.com/zhujiabin/p/6080827.html
 * <p>
 * 基本操作是：
 * 1. 在服务端实现 AIDL 中定义的方法接口的具体逻辑，然后在客户端调用这些方法接口，从而达到跨进程通信的目的。
 * 2. AIDL 中方法是运行在 Binder 线程中的，需要注意线程安全问题；
 */
public class GenerCode {

    public interface IBookManager extends android.os.IInterface {

        /**
         * 运行在服务端，因为在Service实现了此 Stub类
         * 静态内部Stub类；实现 IBookManager 接口，运行在服务端进程
         */
        public static abstract class Stub extends android.os.Binder implements com.github.android.sample.IBookManager {

            /**
             * Binder 的唯一标示 com.github.android.sample.IBookManager
             */
            private static final java.lang.String DESCRIPTOR = "com.github.android.sample.IBookManager";

            public Stub() {
                this.attachInterface(this, DESCRIPTOR);
            }

            /**
             * 将服务端的binder对象，转成客户端需要的 aidl 接口类型对象；
             * <p>
             * ==》android 开发艺术探索 中的一句话，如下：我实在是看不懂是在说什么？？？
             * 如果客户端与服务器在同一进程，返回的是服务端的 Stub 对象本身，
             * 否则返回是系统封装后的Stub.Proxy代理对象；
             * <p>
             * ===》https://www.cnblogs.com/zhujiabin/p/6080827.html
             * 这个文章就讲的明白多了
             *
             * @param obj
             * @return
             */
            public static com.github.android.sample.IBookManager asInterface(android.os.IBinder obj) {
                if ((obj == null)) {
                    return null;
                }
                //有可用的对象了，如果有就将其返回
                android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
                if (((iin != null) && (iin instanceof com.github.android.sample.IBookManager))) {
                    return ((com.github.android.sample.IBookManager) iin);
                }
                //如果本地没有的话就新建一个返回
                return new com.github.android.sample.GenerCode.IBookManager.Stub.Proxy(obj);
            }

            /**
             * 返回当前binder对象
             *
             * @return
             */
            @Override
            public android.os.IBinder asBinder() {
                return this;
            }

            /**
             * 运行在【服务端中的binder线程中】
             * 注意这里是线程池，调用时，需用同步方式
             * 客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法处理
             *
             * @param code  客户端请求的目标方法
             * @param data  目标方法参数
             * @param reply 方法执行完，向reply写入返回值
             * @param flags
             * @return false 表示客户端的请求会失败
             * @throws android.os.RemoteException
             */
            @Override
            public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
                switch (code) {
                    case INTERFACE_TRANSACTION: {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    case TRANSACTION_getBookList: {
                        data.enforceInterface(DESCRIPTOR);
                        java.util.List<com.github.android.sample.Book> _result = this.getBookList();
                        reply.writeNoException();
                        reply.writeTypedList(_result);
                        return true;
                    }
                    case TRANSACTION_addBook: {
                        data.enforceInterface(DESCRIPTOR);
                        com.github.android.sample.Book _arg0;
                        if ((0 != data.readInt())) {
                            _arg0 = com.github.android.sample.Book.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        this.addBook(_arg0);
                        reply.writeNoException();
                        return true;
                    }
                }
                return super.onTransact(code, data, reply, flags);
            }

            /**
             * Stub内部代理类 Proxy，运行在客户端
             * Proxy 类确实是我们的目标，客户端最终通过这个类与服务端进行通信
             */
            private static class Proxy implements com.github.android.sample.IBookManager {
                private android.os.IBinder mRemote;

                Proxy(android.os.IBinder remote) {
                    mRemote = remote;
                }

                @Override
                public android.os.IBinder asBinder() {
                    return mRemote;
                }

                public java.lang.String getInterfaceDescriptor() {
                    return DESCRIPTOR;
                }

                /**
                 * 此方法运行在客户端，运行过程如下：
                 * 1.创建该方法所需的输入型 Parcel 对象 _data; 输出型参数 _replay
                 * 2.调用 transact 方法，发起RPC请求，同时当前线程挂起；
                 * 3.触发服务端 onTransact 方法，等待RPC调用返回，当前线程执行；
                 * 4.从_reply取出RPC过程的返回结果，返回_reply中数据；
                 *
                 * @return
                 * @throws android.os.RemoteException
                 */
                @Override
                public java.util.List<com.github.android.sample.Book> getBookList() throws android.os.RemoteException {
                    /*
                       很容易可以分析出来，_data用来存储流向服务端的数据流，
                       _reply用来存储服务端流回客户端的数据流
                     */

                    android.os.Parcel _data = android.os.Parcel.obtain();       // 输入型参数
                    android.os.Parcel _reply = android.os.Parcel.obtain();      // 输出型参数
                    java.util.List<com.github.android.sample.Book> _result;
                    try {
                        _data.writeInterfaceToken(DESCRIPTOR);
                        // 触发服务端的 onTransact方法，等待返回结果

                        /*
                        核心方法：
                        参数1：方法ID，这个是客户端与服务端约定好的给方法的编码，彼此一一对应；
                        参数4：用是设置进行 IPC 的模式，为 0 表示数据可以双向流通，即 _reply 流可以正常的携带数据回来，如果为 1 的话那么数据将只能单向流通，从服务端回来的 _reply 流将不携带任何数据
                         */
                        mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);  // 挂起
                        _reply.readException();
                        //从_reply中取出服务端执行方法的结果
                        _result = _reply.createTypedArrayList(com.github.android.sample.Book.CREATOR);
                    } finally {
                        _reply.recycle();
                        _data.recycle();
                    }

                    // 返回结果
                    return _result;
                }

                @Override
                public void addBook(com.github.android.sample.Book book) throws android.os.RemoteException {
                    android.os.Parcel _data = android.os.Parcel.obtain();
                    android.os.Parcel _reply = android.os.Parcel.obtain();
                    try {
                        _data.writeInterfaceToken(DESCRIPTOR);
                        if ((book != null)) {
                            _data.writeInt(1);
                            book.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        // 触发服务端的 onTransact方法，等待返回结果
                        mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                        _reply.readException();
                    } finally {
                        _reply.recycle();
                        _data.recycle();
                    }
                }

                @Override
                public void registerBookAddListener(INewBookAddListener listener) throws RemoteException {

                }

                @Override
                public void unregisterBookAddListener(INewBookAddListener listener) throws RemoteException {

                }
            }


            // 标识AIDL中的2个方法
            static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
            static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        }


        /**
         * AIDL中对应接口声明
         *
         * @return
         */
        public java.util.List<com.github.android.sample.Book> getBookList() throws android.os.RemoteException;

        public void addBook(com.github.android.sample.Book book) throws android.os.RemoteException;
    }

}
