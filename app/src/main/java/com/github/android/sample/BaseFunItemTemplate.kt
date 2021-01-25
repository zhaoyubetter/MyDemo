package com.github.android.sample

import android.app.Activity
import android.content.Context
import com.better.base.model.SampleItem
import com.github.android.sample.activity_base.singleTask.SingleTaskMainTestActivity
import com.github.android.sample.anim.*
import com.github.android.sample.camera.CameraMainActivity
import com.github.android.sample.camera.CameraOld2Activity
import com.github.android.sample.camera.CameraOld3Activity
import com.github.android.sample.camera.CameraOldActivity
import com.github.android.sample.canvas_paint.*
import com.github.android.sample.sensor.SensorMainActivity
import com.github.android.sample.jetpack.architecture.*
import com.github.android.sample.jetpack.databinding.DataBindTest1Activity
import com.github.android.sample.jetpack.databinding.DataBindTest2Activity
import com.github.android.sample.jetpack.databinding.DatabindTest3Activity
import com.github.android.sample.md.DrawerLayoutActivity
import com.github.android.sample.opengl.OpenglMainActivity
import com.github.android.sample.provider.db.DBActivity1
import com.github.android.sample.svg.SVG1Activity
import com.github.android.sample.tools.ToolAttributesTest1Activity
import view.*

/**
 * Created by zhaoyu on 2018/3/11.
 */
class BaseFunItemTemplate private constructor(ctx: Context) {

    // 伴生对象
    companion object {
        private var instance: BaseFunItemTemplate? = null
        private val items = mutableListOf<SampleItem<Activity>>()
        private val groupsItems = mutableMapOf<Int, List<SampleItem<Activity>>>()

        fun getInstance(context: Context): BaseFunItemTemplate {
            if (instance == null)
                instance = BaseFunItemTemplate(context)
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
                title = "自定义控件集成系列"
                desc = "由浅入深介绍"
                item {
                    pid = 2
                    title = "Paint & Canvas基础1"
                    desc = "基本图形绘制、文字绘制"
                    clazz = PaintCanvas_Base1_Activity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础2"
                    desc = "Canvas drawTextOnPath"
                    clazz = PaintCanvas_Base2_Activity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础3"
                    desc = "区域 Region"
                    clazz = PaintCanvas_Region_Activity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础4"
                    desc = "Canvas变换、rotate、clip、图层等"
                    clazz = CanvasActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础5"
                    desc = "Canvas.drawText()方法"
                    clazz = CanvasDrawTextActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础6"
                    desc = "Path 与 贝塞尔曲线"
                    clazz = CanvasPathActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础7"
                    desc = "Paint 画笔相关函数"
                    clazz = Paint_Method_Activity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础8"
                    desc = "ColorMatrix与滤镜"
                    clazz = PaintColorMatrixActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础9"
                    desc = "ColorFilter"
                    clazz = PaintColorFilterActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础10"
                    desc = "Xfermode"
                    clazz = PaintXfermodeActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础11"
                    desc = "Canvas图层详解Layer"
                    clazz = CanvasLayerActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础12"
                    desc = "ShadowLayer 阴影与 MaskFilter 发光效果"
                    clazz = PaintShadowLayerActivity::class.java
                }

                item {
                    pid = 2
                    title = "Paint & Canvas基础13"
                    desc = "Paint' Shader着色器"
                    clazz = PaintShaderActivity::class.java
                }

                item {
                    pid = 2
                    id = 21
                    title = "Paint & Canvas 示例"

                    item {
                        pid = 21
                        title = "QQ红点消息拖动"
                        desc = "仿QQ红点消息拖动"
                        clazz = RedPointerDragActivity::class.java
                    }
                } // end 21
            }

            // 第三部分 materials design
            item {
                pid = 0
                id = 3
                title = "Materials Design 设计"
                desc = "Materials Design 设计"
                item {
                    pid = 3
                    title = "DrawerLayout 抽屉示例"
                    desc = "DrawerLayout 抽屉示例"
                    clazz = DrawerLayoutActivity::class.java
                }
            }

            // 第4部分 svg
            item {
                pid = 0
                id = 4
                title = "SVG学习与应用"
                desc = "SVG学习与应用"
                item {
                    pid = 4
                    title = "SVG基本使用"
                    desc = "SVG基本使用"
                    clazz = SVG1Activity::class.java
                }
            }

            // activity 知识复习
            item {
                pid = 0
                id = 5
                title = "Activity 相关"
                desc = "Activity 相关"
                item {
                    pid = 5
                    title = "singleTask 任务栈"
                    desc = "singleTask 任务栈"
                    clazz = SingleTaskMainTestActivity::class.java
                }
            }

            // 内容提供者
            item {
                pid = 0
                id = 6
                title = "内容提供者"
                desc = "内容提供者"
                item {
                    pid = 6
                    title = "provider 数据库"
                    desc = "provider 数据库"
                    clazz = DBActivity1::class.java
                }
            }

            // jetpack
            item {
                pid = 0
                id = 7
                title = "jetpack 使用"
                desc = "jetpack 的使用"
                item {
                    id = 71
                    pid = 7
                    title = "architecture"
                    desc = "architecture 架构"

                    item {
                        pid = 71
                        title = "ViewModel"
                        desc = "ViewModel 简单使用"
                        clazz = ViewModelTest1Activity::class.java
                    }

                    item {
                        pid = 71
                        title = "LiveData"
                        desc = "LiveData 简单使用"
                        clazz = LiveDataTest1Activity::class.java
                    }

                    item {
                        pid = 71
                        title = "Lifecycle"
                        desc = "Lifecycle 简单使用"
                        clazz = LifeCycleTest1Activity::class.java
                    }

                    item {
                        pid = 71
                        title = "LiveData实现数据通信"
                        desc = "LiveData实现数据通信"
                        clazz = SeekBarTestActivity::class.java
                    }

                    item {
                        pid = 71
                        title = "SavedState 示例"
                        desc = "SavedState 示例"
                        clazz = SavedStateActivity::class.java
                    }
                }

                item {
                    id = 72
                    pid = 7
                    title = "DataBinding"
                    desc = "DataBinding 使用"

                    item {
                        pid = 72
                        title = "DataBinding 简单使用"
                        desc = "DataBinding 简单使用"
                        clazz = DataBindTest1Activity::class.java
                    }
                    item {
                        pid = 72
                        title = "DataBinding ObserableField"
                        desc = "DataBinding 简单使用"
                        clazz = DataBindTest2Activity::class.java
                    }

                    item {
                        pid = 72
                        title = "DataBinding BindingAdapter"
                        desc = "DataBinding BindingAdapter"
                        clazz = DatabindTest3Activity::class.java
                    }
                }

                item {
                    id = 73
                    pid = 7
                    title = "工具相关"
                    desc = "Google提供的一些工具"

                    item {
                        pid = 73
                        title = "Tools Attributes"
                        desc = "Tools Attributes"
                        clazz = ToolAttributesTest1Activity::class.java
                    }
                }
            }

            // view base
            item {
                pid = 0
                id = 8
                title = "View 基础知识"
                desc = "View 基础知识"
                item {
                    pid = 8
                    title = "View 的坐标"
                    desc = "Android 坐标与视图坐标"
                    clazz = BaseViewCoordinateActivity::class.java
                }
                item {
                    pid = 8
                    title = "View 的ScrollTo/By与ScrollX/Y"
                    desc = "View 的ScrollTo/By与ScrollX/Y"
                    clazz = BaseViewScrollActivity::class.java
                }
                item {
                    pid = 8
                    title = "setX setTranslationX diff"
                    desc = "setX setTranslationX diff"
                    clazz = BaseViewXAndTranslationXActivity::class.java
                }
                item {
                    pid = 8
                    title = "随着手指滑动"
                    desc = "随着手指滑动"
                    clazz = BaseViewTouchAnimActivity::class.java
                }

                item {
                    pid = 8
                    title = "事件分发"
                    desc = "事件分发(View & ViewGroup)"
                    clazz = BaseDispatchTouchEventActivity::class.java
                }
            }

            item {
                pid = 0
                id = 9
                title = "Camera"
                desc = "Camera2"

                item {
                    pid = 9
                    title = "老的Api基本使用"
                    clazz = CameraOldActivity::class.java
                }

                item {
                    pid = 9
                    title = "老的Api自定义大小"
                    clazz = CameraOld2Activity::class.java
                }

                item {
                    pid = 9
                    title = "Google 开源的相机与MaskView预览"
                    desc = "第一次拍也无抖动，棒"
                    clazz = CameraOld3Activity::class.java
                }

                item {
                    pid = 9
                    title = "Camera2"
                    clazz = CameraMainActivity::class.java
                }
            }

            item {
                pid = 0
                id = 10
                title = "传感器"
                desc = "传感器"

                item {
                    pid = 10
                    title = "传感器"
                    clazz = SensorMainActivity::class.java
                }
            }

            item {
                pid = 0
                id = 11
                title = "openGL"
                desc = "OpenGL ES"

                item {
                    pid = 11
                    title = "openGL主页"
                    clazz = OpenglMainActivity::class.java
                }
            }
        }
    }
}