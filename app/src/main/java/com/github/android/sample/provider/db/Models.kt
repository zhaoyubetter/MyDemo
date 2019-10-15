package com.github.android.sample.provider.db

import com.github.android.sample.provider.db.annotations.Table

/**
 * Created by better On 2019-09-05.
 */
@Table("tb_user")
data class User(
        var name: String = "",
        var age: Int = 0
         , var summary:String = ""  // 版本2 新增 summary : String
         , var sex:Boolean = false  // 版本3 新增 sex Boolean 可以直接映射成 INT 类型，反之亦然
         , var company:String = ""  // 版本4 新增 company String
)

@Table("tb_user2")
class User2(
        var name: String = "",
        var age2: Int = 0,
        var text: String = ""
)

// 是用kotlin需要空的构造喊谁呢
@Table("tb_cat")
class Cat(var number: Int = 0) {
    var name: String? = null
    var color: String? = null
}

// 联合主键测试
@Table(value = "auth_entity", primaryKeys = ["appId, scope"])
class AuthEntity {
    var id: Int = 0
    var appId = ""
    var scope = ""
    var permission = ""
    var title = ""
    var description = ""
    var state = 0
}
