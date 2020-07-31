package com.github.android.sample.opengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES32
import android.opengl.GLES32.*
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.android.sample.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author zhaoyu1  2020-07-21
 **/
class FirstOpenglFragment : Fragment() {

    var glSurfaceView: GLSurfaceView? = null
    var isSetRender = false
    val BYTES_PER_FLOAT = 4

    val tableVertices = arrayOf(
            0f, 0f,
            0f, 14f,
            9f, 14f,
            9f, 0f
    )

    val tableVerticesWithTriangles = floatArrayOf(
//            // Triangle 1
//            0f, 0f,
//            9f, 14f,
//            0f, 14f,
//            // Triangle 2
//            0f, 0f,
//            9f, 0f,
//            9f, 14f,
//            // Line 1
//            0f, 7f,
//            9f, 7f,
//            // Mallets
//            4.5f, 2f,
//            4.5f, 12f
            // Triangle 1
            0f, 0f,


            // Triangle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            // Line 1
            -0.5f, 0f,
            0.5f, 0f,

            // Mallets
            0f, -0.25f,
            0f, 0.25f,
            0f, 0f
    )

    // 数据直接给 native 区域, allocateDirect 分配本地内存（不会被垃圾回收管理）
    // 桌子的顶点位置
    var vertexData: FloatBuffer = ByteBuffer.allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder()).asFloatBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 从 java 内存，赋值到 native 内存，用于后面方便 openGL 获取数据
        vertexData.put(tableVerticesWithTriangles)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_opengl_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glSurfaceView = GLSurfaceView(view.context)
        glSurfaceView?.setEGLContextClientVersion(2)    // 需要加上这句，不然画不出来
        glSurfaceView?.setRenderer(FirstRenderer(view.context))        // surface 创建 or 绘制变化时 render都将被调用
        isSetRender = true
        (view as ViewGroup).addView(glSurfaceView)
    }

    inner class FirstRenderer(ctx: Context) : GLSurfaceView.Renderer {

        // 一个顶点有2个分量
        val POSITION_COMPONENT_COUNT = 2

        val vertexShaderSource: String = readTextFileFromResource(ctx, R.raw.gl_simple_vertex_shader)
        val fragShaderSource: String = readTextFileFromResource(ctx, R.raw.gl_simple_fragment_shader)
        // 链接程序id
        var program: Int = 0

        val U_COLOR = "u_Color"
        var uColorLocation: Int = 0
        val A_POSITON = "a_Position"
        var aPositionLocation = 0

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            // 1.清空屏幕时
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            // 2.编译着色器
            val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
            val fragmentShader = ShaderHelper.compileFragmentShader(fragShaderSource)
            program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
            // 验证程序
            ShaderHelper.validateProgram(program)
            // 使用程序
            glUseProgram(program)

            // 获取 uniform 位置
            uColorLocation = glGetUniformLocation(program, U_COLOR)
            // 获取属性位置
            aPositionLocation = glGetAttribLocation(program, A_POSITON)

            // 告诉 openGL 到哪里找到属性 a_Position 对应的数据
            vertexData.position(0)
            glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT,
                    GL_FLOAT, false, 0, vertexData)

            // 使能属性
            glEnableVertexAttribArray(aPositionLocation)
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES32.glViewport(0, 0, width, height)  // 设置渲染大小
        }

        override fun onDrawFrame(gl: GL10) {
            // 1.清屏
            glClear(GLES32.GL_COLOR_BUFFER_BIT)
            // 更新着色器代码中的 u_Color 值
            glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)
            // 绘制2个三角形 （draw the table）
            glDrawArrays(GL_TRIANGLES, 0, 6)

            // 绘制分割线
            glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
            glDrawArrays(GLES20.GL_LINES, 6, 2)

            // 绘制点1
            glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
            glDrawArrays(GLES20.GL_POINTS, 8, 1)

            // 绘制点2
            glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
            glDrawArrays(GLES20.GL_POINTS, 9, 1)

            // 绘制点3
            glDrawArrays(GLES20.GL_POINTS, 10, 1)

            // 绘制长方形
        }
    }

    override fun onPause() {
        super.onPause()
        if (isSetRender) {
            glSurfaceView?.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isSetRender) {
            glSurfaceView?.onResume()
        }
    }
}