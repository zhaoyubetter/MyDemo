package com.github.android.sample.opengl;


import android.util.Log;

import static android.opengl.GLES32.*;
import static android.opengl.GLES32.glShaderSource;
import static android.opengl.GLES32.glCreateShader;
import static android.opengl.GLES32.GL_FRAGMENT_SHADER;
import static android.opengl.GLES32.GL_VERTEX_SHADER;

/**
 * @author zhaoyu1  2020-07-21
 **/
public class ShaderHelper {

    private static final String TAG = "openGL";

    /**
     * 编译着色器代码
     * 顶点着色器
     *
     * @param shaderCode
     * @return
     */
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    /**
     * 片段着色器
     *
     * @param shaderCode
     * @return
     */
    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        // glCreateShader 创建着色器对象，返回值为 OpenGL 对象的引用，0为创建失败
        // OpenGL 不会抛出异常
        final int shaderObjectID = glCreateShader(type);
        if (shaderObjectID == 0) {
            Log.w("openGL", "Could not create new shader.");
        }

        // 2.编译着色器源代码
        // glShaderSource 上传源代码
        glShaderSource(shaderObjectID, shaderCode);
        // 编译着色器
        glCompileShader(shaderObjectID);
        // 3.获取编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectID, GL_COMPILE_STATUS, compileStatus, 0);
        Log.v("openGL", String.format("compileing source: %s, log:%s", shaderCode, glGetShaderInfoLog(shaderObjectID)));
        // 4.查看编译状态
        if (compileStatus[0] == 0) {
            // compile failed. del the shader object
            glDeleteShader(shaderObjectID);
            Log.w(TAG, "Compilation of shader failed.");
        }

        return shaderObjectID;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 新建程序对象
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.w(TAG, "Could not create new program");
        }
        return programObjectId;
    }
}
