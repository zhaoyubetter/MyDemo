// 1.需要h5，提供原生调用的API；可参考：https://www.cnblogs.com/dailc/p/5931324.html#hybrid_3_2


(function (win) {
    var hasOwnProperty = Object.prototype.hasOwnProperty;
    var JSBridge = win.JSBridge || (win.JSBridge = {}); // var JSBridge = win.JSBridge || (win.JSBridge = {});
    var JSBRIDGE_PROTOCOL = 'me';   // JSBridge
    var JSBRIDGE_HOST = 'jsbridge';   // host
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
})(window);