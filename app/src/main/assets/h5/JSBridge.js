// 1.需要h5，提供原生调用的API；可参考：https://www.cnblogs.com/dailc/p/5931324.html#hybrid_3_2


(function (win) {
    var hasOwnProperty = Object.prototype.hasOwnProperty;
    var JSBridge = win.JSBridge || (win.JSBridge = {}); // var JSBridge = win.JSBridge || (win.JSBridge = {});
    var JSBRIDGE_PROTOCOL = 'me';   // JSBridge
    var JSBRIDGE_HOST = 'jsbridge';   // host

    //本地注册的方法集合,原生只能调用本地注册的方法,否则会提示错误
    var messageHandlers = {};

    var Inner = {
        callbacks: {},
        call: function (obj, method, params, callback) {
            console.log(obj+" "+method+" "+params+" "+callback);
            var port = Util.getPort();
            console.log(port);
            this.callbacks[port] = callback;
            var uri=Util.getUri(obj,method,params,port);
            console.log(uri);
            window.prompt(uri, "");  // window.prompt(uri, “”)将uri传递到native层
        },

        // onFinish()方法接受native回传的port值和执行结果，
        // 根据port值从callbacks中得到原始的callback函数，执行callback函数，之后从callbacks中删除
        onFinish: function (port, jsonObj){
            var callback = this.callbacks[port];
            var backString = callback && callback(jsonObj);
            delete this.callbacks[port];
            return backString;
        },

        // better ==> 新方式
        callNew: function (params, callback) {   // params: {} json参数, callback：回调地址
            console.log(" "+params+" "+callback);
            var port = Util.getPort();
            console.log(port);
            this.callbacks[port] = callback;
            var uri=Util.getUriNew(params,port);
            console.log("after encode: " + uri);
            window.prompt(uri, "");  // window.prompt(uri, “”)将uri传递到native层
        },

        /// 原生调用H5////////
            /**
             * @description 注册本地JS方法通过JSBridge给原生调用
             * 我们规定,原生必须通过JSBridge来调用H5的方法
             * 注意,这里一般对本地函数有一些要求,要求第一个参数是data,第二个参数是callback
             * @param {String} handlerName 方法名
             * @param {Function} handler 对应的方法
             */
        registerHandler: function(handlerName, handler) {
           messageHandlers[handlerName] = handler;
        },

        /**
         * @description 原生调用H5页面注册的方法,或者调用回调方法
         * @param {String} messageJSON 对应的方法的详情,需要手动转为json
         */
        _handleMessageFromNative: function(messageJSON) {
            setTimeout(_doDispatchMessageFromNative);
            /**
             * @description 处理原生过来的方法
             */
            function _doDispatchMessageFromNative() {
                var message;
                try {
                    if(typeof messageJSON === 'string') {
                        message = JSON.parse(messageJSON);
                    } else {
                        message = messageJSON;
                    }
                } catch(e) {
                    console.error("原生调用H5方法出错,传入参数错误");
                    return;
                }

                //回调函数
                var responseCallback;
                if(message.responseId) {
                    //这里规定,原生执行方法完毕后准备通知h5执行回调时,回调函数id是responseId
                    responseCallback = responseCallbacks[message.responseId];
                    if(!responseCallback) {
                        return;
                    }
                    //执行本地的回调函数
                    responseCallback(message.responseData);
                    delete responseCallbacks[message.responseId];
                } else {
                    //否则,代表原生主动执行h5本地的函数
                    //从本地注册的函数中获取
                    var handler = messageHandlers[message.handlerName];
                    if(!handler) {
                        //本地没有注册这个函数
                    } else {
                        //执行本地函数,按照要求传入数据和回调
                        handler(message.data);
                    }
                }
            }
        }
    };
    var Util = {
        getPort: function () {
            return Math.floor(Math.random() * (1 << 30));
        },
        getUri:function(obj, method, params, port){
            params = this.getParam(params);
            var uri = JSBRIDGE_PROTOCOL + '://' + obj + ':' + port + '/' + method + '?' + params;
            return uri;
        },
        getParam:function(obj){
            if (obj && typeof obj === 'object') {
                return JSON.stringify(obj);
            } else {
                return obj || '';
            }
        },

        // better-->new
        getUriNew:function(params, port){
            params = this.getParam(params);
            console.log("before: encode: " + JSBRIDGE_PROTOCOL + '://' + JSBRIDGE_HOST + '?data=' + params + '&callback=' + port);
            var uri = JSBRIDGE_PROTOCOL + '://' + JSBRIDGE_HOST + '?data=' + encodeURIComponent(params) + '&callback=' + encodeURIComponent(port);
            return uri;
        },
    };
    for (var key in Inner) {
        if (!hasOwnProperty.call(JSBridge, key)) {
            JSBridge[key] = Inner[key];
        }
    }


    //注册一个测试函数( 原生调用 h5)
    JSBridge.registerHandler('testH5Func', function(data) {
        alert('测试函数接收到数据:' + JSON.stringify(data));
    });

})(window);
