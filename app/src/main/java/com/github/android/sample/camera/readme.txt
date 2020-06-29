1.google cameraview 问题：
 a. 不能设置  mCameraParameters.setRotation(calcCameraRotation(mDisplayOrientation));
    否则在某些手机上，保存图片时，位置反转了；
 b. cameraView 拿到的图片是最大尺寸，这个需要修改下；
 c. 拍照保存图片时，需要自己算角度，这样图片才是正确的方向啊