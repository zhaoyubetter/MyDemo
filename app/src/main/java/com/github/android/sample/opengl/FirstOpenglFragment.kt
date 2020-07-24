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
            0f, 0f,
            9f, 14f,
            0f, 14f,

            0f, 0f,
            9f, 0f,
            9f, 14f
    )

    // 数据直接给 native 区域, allocateDirect 分配本地内存（不会被垃圾回收管理）
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
        glSurfaceView?.setRenderer(FirstRenderer(view.context))        // surface 创建 or 绘制变化时 render都将被调用
        isSetRender = true
        (view as ViewGroup).addView(glSurfaceView)
    }

    class FirstRenderer(ctx: Context) : GLSurfaceView.Renderer {

        val vertexShaderSource: String = readTextFileFromResource(ctx, R.raw.gl_simple_vertex_shader)
        val fragShaderSource: String = readTextFileFromResource(ctx, R.raw.gl_simple_fragment_shader)

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            // 1.清空屏幕时，显示红色
            glClearColor(1.0f, 0.0f, 0.0f, 0.0f)
            // 2.编译着色器
            val vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource)
            val fragmentShader = ShaderHelper.compileFragmentShader(fragShaderSource)
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES32.glViewport(0, 0, width, height)  // 设置渲染大小
        }

        override fun onDrawFrame(gl: GL10) {
            // 1.清屏
            GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT)
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