package com.hcii.kennethrstclairiii.heart_rate


import org.opencv.android.JavaCameraView
import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet

//class Tutorial3View(context: Context, cameraId: Int) : JavaCameraView(context, cameraId) {
class Tutorial3View(context: Context, attrs: AttributeSet) : JavaCameraView(context, attrs) {


    fun setFlashOn() {
        val params = mCamera.getParameters()
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
        //        params.set("orientation", "portrait");
        //        params.setRotation(90);
        mCamera.setParameters(params)
        mCamera.setDisplayOrientation(90)


    }

    fun setFlashOff() {
        val params = mCamera.getParameters()
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF)
        //        params.set("orientation", "portrait");
        //        params.setRotation(90);
        mCamera.setParameters(params)
        mCamera.setDisplayOrientation(90)
    }
}
