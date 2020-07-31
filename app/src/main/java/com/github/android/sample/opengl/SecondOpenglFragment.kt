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
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * @author zhaoyu1  2020-07-21
 **/
class SecondOpenglFragment : Fragment() {

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
            // Triangle Fan, add Color
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            // Mallets
            0f, -0.25f, 0f, 0f, 1f,
            0f, 0.25f, 1f, 0f, 0f
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

        lateinit var square: Square
        lateinit var triangle: Triangle

        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

            triangle = Triangle()
            square = Square()
        }

        override fun onDrawFrame(gl: GL10) {
            // 绘制图形前至少需要一个顶点着色器来绘制形状和一个片段着色器的颜色
            // 顶点着色器（Vertex Shader）顶点着色器是GPU上运行的小程序，由名字可以知道，通过它来处理顶点
            // 片段着色器（Fragment Shader ) 用于呈现与颜色或纹理的形状的面的OpenGL ES代码
            // 项目（Program） -包含要用于绘制一个或多个形状着色器的OpenGL ES的对象
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            GLES32.glViewport(0, 0, width, height)  // 设置渲染大小
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

    class Triangle {
        var vertexBuffer: FloatBuffer
        val COORDS_PER_VERTEX = 3
        var triangleCoords = floatArrayOf(// in counterclockwise order:
                0.0f, 0.5f, 0.0f,   // top
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f  // bottom right
        )
        var color = floatArrayOf(255f, 0f, 0f, 1.0f)

        init {
            // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
            val bb = ByteBuffer.allocateDirect(triangleCoords.size * 4)
            // 数组排列用nativeOrder
            bb.order(ByteOrder.nativeOrder())
            // 从ByteBuffer创建一个浮点缓冲区
            vertexBuffer = bb.asFloatBuffer()
            // 将坐标添加到FloatBuffer
            vertexBuffer.put(triangleCoords)
            // 设置缓冲区来读取第一个坐标
            vertexBuffer.position(0)
        }

        private val vertexShaderCode = "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = vPosition;" +
                "}"

        private val fragmentShaderCode = (
                "precision mediump float;" +
                        "uniform vec4 vColor;" +
                        "void main() {" +
                        "  gl_FragColor = vColor;" +
                        "}")
    }

    //
    inner class Square {

        private val vertexBuffer: FloatBuffer
        private val drawListBuffer: ShortBuffer

        private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

        // number of coordinates per vertex in this array
        internal val COORDS_PER_VERTEX = 3
        internal var squareCoords = floatArrayOf(-0.5f, 0.5f, 0.0f, // top left
                -0.5f, -0.5f, 0.0f, // bottom left
                0.5f, -0.5f, 0.0f, // bottom right
                0.5f, 0.5f, 0.0f) // top right

        init {
            // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
            val bb = ByteBuffer.allocateDirect(squareCoords.size * 4)
            bb.order(ByteOrder.nativeOrder())
            vertexBuffer = bb.asFloatBuffer()
            vertexBuffer.put(squareCoords)
            vertexBuffer.position(0)

            // 初始化ByteBuffer，长度为arr数组的长度*2，因为一个short占2个字节
            val dlb = ByteBuffer.allocateDirect(drawOrder.size * 2)
            dlb.order(ByteOrder.nativeOrder())
            drawListBuffer = dlb.asShortBuffer()
            drawListBuffer.put(drawOrder)
            drawListBuffer.position(0)
        }
    }
}