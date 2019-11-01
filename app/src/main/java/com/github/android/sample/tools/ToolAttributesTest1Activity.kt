package com.github.android.sample.tools

import android.os.Bundle
import com.better.base.ToolbarActivity
import com.github.android.sample.R

/**
 * https://mp.weixin.qq.com/s/tlXNYixU2H_vNm9yYozRpw
 */
class ToolAttributesTest1Activity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tool_attributes_test1)
    }

    // Tool Attributes
    /*
    1. 配置只支持一个语言
        <string name="title_section1" tools:ignore="MissingTranslation">Vertical List</string>
    2. 设置样例数据
       tools: 可替换【任何】以 android: 为前缀的属性，可为其设置样例数据；
    3. tools:layout 只能用于 fragment 控件中，如果我们的 activity 布局文件中声明了 <fragment> 控件，
        我们就可以通过 tools:layout=”@layout/fragment_main” 来在当前 activity 布局中预览 fragment 中的布局效果。
    4. tools:showIn
        可以指定其他布局文件像 <include> 组件一样在当前布局文件中使用和预览 <include> 控件的实际效果。
        必须用在根布局；
    5. sample data
        如：tools:text="@tools:sample/date/hhmm" 系统自带的一些样例文本；

     */
}
