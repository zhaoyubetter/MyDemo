package com.github.android.sample.anim

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem

/**
 * Created by zhaoyu on 2018/3/11.
 */
class AnimFunTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: AnimFunTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): AnimFunTemplate {
            if (instance == null)
                instance = AnimFunTemplate(context)
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
            // sampleItem
            item {
                pid = 0
                id = 1
                title = "Android 动画体系"
                desc = "介绍tween动画、属性动画"

                // dsl 嵌套
                item {
                    pid = 1
                    title = "tween动画"
                    desc = "tween动画基本使用"
                    clazz = TweenAnim1Activity::class.java
                }

                item {
                    pid = 1
                    title = "tween动画Interpolator"
                    desc = "tween动画Interpolator详解"
                    clazz = TweenAnimInterpolatorActivity::class.java
                }

                item {
                    pid = 1
                    title = "Value Animation"
                    desc = "属性动画"
                    clazz = PropertyAnim1Activity::class.java
                }

                item {
                    pid = 1
                    title = "Evaluator 转换器"
                    desc = "自定义Evaluator"
                    clazz = CustomEvaluatorActivity::class.java
                }

                item {
                    pid = 1
                    title = "ObjectAnimator"
                    desc = "派生自ValueAnimator"
                    clazz = ObjectAnimatorActivity::class.java
                }

                item {
                    pid = 1
                    title = "PropertyValuesHolder使用"
                    desc = "ObjectAnimator使用PropertyValuesHolder构造动画"
                    clazz = PropertyValuesHolderActivity::class.java
                }

                item {
                    pid = 1
                    title = "Keyframe关键字"
                    desc = "Keyframe->PropertyValuesHolder->ObjectAnimator"
                    clazz = KeyframeActivity::class.java
                }

                item {
                    pid = 1
                    title = "AnimatorSet联合动画"
                    desc = "AnimatorSet"
                    clazz = AnimatorSetActivity::class.java
                }

                item {
                    pid = 1
                    title = "Animator from xml"
                    desc = "从xml中装载属性动画"
                    clazz = PropertyAnimFromXmlActivity::class.java
                }

                item {
                    pid = 1
                    title = "综合示例"
                    desc = "属性动画综合例子"
                    clazz = AnimatorSetSampleDemoActivity::class.java
                }

                item {
                    pid = 1
                    title = "LayoutAnimation"
                    desc = "ViewGroup的LayoutAnimation"
                    clazz = LayoutAnimationActivity::class.java
                }
            }
        }
    }
}