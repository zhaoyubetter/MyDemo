package com.github.android.sample;

/**
 * 生成的代码
 */
public class GenerCode {

    public interface IBookManager extends android.os.IInterface {

        /**
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
             * 将服务端的binder对象，转成客户端需要的aidl接口类型对象；
             * 如果客户端与服务器统一进程，返回Stub对象，
             * 否则返回Stub.Proxy代理对象；
             *
             * @param obj
             * @return
             */
            public static com.github.android.sample.IBookManager asInterface(android.os.IBinder obj) {
                if ((obj == null)) {
                    return null;
                }
                android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
                if (((iin != null) && (iin instanceof com.github.android.sample.IBookManager))) {
                    return ((com.github.android.sample.IBookManager) iin);
                }

                // return new com.github.android.sample.IBookManager.Stub.Proxy(obj);
                return null;

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
             * 运行在【服务端中的binder线程中】// 注意这里是线程池，调用时，需用同步方式
             * 进程不同时，走这里；
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
                    case  TRANSACTION_getBookList: {
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
             * Stub内部代理类 Proxy， 运行在客户端进程
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
                 * @return
                 * @throws android.os.RemoteException
                 */
                @Override
                public java.util.List<com.github.android.sample.Book> getBookList() throws android.os.RemoteException {
                    android.os.Parcel _data = android.os.Parcel.obtain();       // 输入型参数
                    android.os.Parcel _reply = android.os.Parcel.obtain();      // 输出型参数
                    java.util.List<com.github.android.sample.Book> _result;
                    try {
                        _data.writeInterfaceToken(DESCRIPTOR);
                        // 触发服务端的 onTransact方法，等待返回结果
                        mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                        _reply.readException();
                        _result = _reply.createTypedArrayList(com.github.android.sample.Book.CREATOR);
                    } finally {
                        _reply.recycle();
                        _data.recycle();
                    }
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
