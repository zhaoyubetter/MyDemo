package com.github.android.sample

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.anim.*
import com.github.android.sample.canvas_paint.PaintCanvas_Base1_Activity

/**
 * Created by zhaoyu on 2018/3/11.
 */
class FunItemTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: FunItemTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): FunItemTemplate {
            if (instance == null)
                instance = FunItemTemplate(context)
            return instance!!
        }

        private fun item(closure: SampleItem<Activity>.() -> Unit) {
            items.add(SampleItem<Activity>().apply(closure))
        }

        // 分组模板
        private fun group(closure: () -> Unit) {
            closure.invoke()
            groupsItems += items.groupBy { it.pid }     // 根据pid进行分组
        }
    }

    operator fun get(id: Int?) = groupsItems[id]
    operator fun contains(id: Int?) = groupsItems.any { it.key == id }

    init {
        group {
            // 第一部分 动画
            item {
                pid = 0
                id = 1
                title = "Android 动画体系"
                desc = "介绍tween动画、属性动画"

                item {
                    pid = 1
                    id = 11
                    title = "View 动画"
                    desc = "介绍tween动画、属性动画"

                    item {
                        pid = 11
                        title = "tween动画"
                        desc = "tween动画基本使用"
                        clazz = TweenAnim1Activity::class.java
                    }

                    item {
                        pid = 11
                        title = "tween动画Interpolator"
                        desc = "tween动画Interpolator详解"
                        clazz = TweenAnimInterpolatorActivity::class.java
                    }
                } // end 11

                item {
                    pid = 1
                    id = 12
                    title = "属性动画"

                    item {
                        pid = 12
                        title = "Value Animation"
                        desc = "属性动画"
                        clazz = PropertyAnim1Activity::class.java
                    }

                    item {
                        pid = 12
                        title = "Evaluator 转换器"
                        desc = "自定义Evaluator"
                        clazz = CustomEvaluatorActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "ObjectAnimator"
                        desc = "派生自ValueAnimator"
                        clazz = ObjectAnimatorActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "PropertyValuesHolder使用"
                        desc = "ObjectAnimator使用PropertyValuesHolder构造动画"
                        clazz = PropertyValuesHolderActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "Keyframe关键字"
                        desc = "Keyframe->PropertyValuesHolder->ObjectAnimator"
                        clazz = KeyframeActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "AnimatorSet联合动画"
                        desc = "AnimatorSet"
                        clazz = AnimatorSetActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "Animator from xml"
                        desc = "从xml中装载属性动画"
                        clazz = PropertyAnimFromXmlActivity::class.java
                    }

                    item {
                        pid = 12
                        title = "综合示例"
                        desc = "属性动画综合例子"
                        clazz = AnimatorSetSampleDemoActivity::class.java
                    }
                } // end 12

                item {
                    pid = 1
                    id = 13
                    title = "ViewGroup Item 动画"

                    item {
                        pid = 13
                        title = "LayoutAnimation"
                        desc = "ViewGroup的LayoutAnimation"
                        clazz = LayoutAnimationActivity::class.java
                    }

                    item {
                        pid = 13
                        title = "AnimateLayoutChanges"
                        desc = "ViewGroup添加Item 动画"
                        clazz = AnimateLayoutChangesActivity::class.java
                    }
                } // end 13
            }

            // 第二部分 paint_canvas部分
            item {
                pid = 0
                id = 2
                title = "自定义控件系列"
                desc = "由浅入深介绍"
                item {
                    pid = 2
                    title = "Paint & Canvas基础1"
                    desc = "Paint & Canvas基础1"
                    clazz = PaintCanvas_Base1_Activity::class.java
                }
            }
        }
    }
}