// 定义浮点数的默认精度：可选 lowp、mediump、highp 分别对应 低、中、高
precision mediump float;

// 生成片段着色器的颜色
// 一个 uniform 会让每个顶点都使用同一个值
// u_Color 为4颜色分量
uniform vec4 u_Color;

void main()
{
    // 着色器给 gl_FragColor 赋值，OpenGL 使用此颜色作为当前片段的最终颜色
    gl_FragColor = u_Color;
}