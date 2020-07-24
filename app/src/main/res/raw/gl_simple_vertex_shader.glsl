
// GLSL 是 openGL 的着手语言；语法类似于 C
// 定义过的顶点，顶点着色器会被调用一次，被调用的时候 a_position 属性接收当前顶点信息，类似为 vec4 类型；
// 1.vec4 在位置上下文中含：x，y，z， 三维位置，另还有一个 w 属性；
// 2.attribute 把顶点属性（如：颜色、位置等）放入着色器
// 3.openGL 会把 gl_Position 中存储的值作为当前顶点的最终位置，并将这些顶点组织成点、直线和三角形

attribute vec4 a_Position

void main()
{
    gl_Position = a_Position;
}