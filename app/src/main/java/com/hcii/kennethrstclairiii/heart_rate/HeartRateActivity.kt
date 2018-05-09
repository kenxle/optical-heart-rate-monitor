package com.hcii.kennethrstclairiii.heart_rate

import android.Manifest
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.CameraBridgeViewBase
//import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener

import android.os.Bundle
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.TextView
import com.androidplot.util.Redrawer
import com.androidplot.xy.*
import org.opencv.core.*

import org.opencv.imgproc.Imgproc


class HeartRateActivity : Activity(), CvCameraViewListener{

    private val TAG = "OCVI:HeartRateActivity"
    val MY_PERMISSIONS_REQUEST_CAMERA = 1
    //plotting
    private val HISTORY_SIZE: Double = 100.0
    lateinit var aprHistoryPlot: XYPlot
    lateinit var rollHistorySeries: SimpleXYSeries
    lateinit var redrawer: Redrawer

    internal var redData = DataWindow(
            minWindowLength = 20
    )
    val redFilter = RedFilter(redData)
    val fingerDetector = FingerDetector()
    val pulseDetector = PulseDetector()
    val rateCalculator = RateCalculator()
    var skipEarlyFrames = 50

    val mask = Rect(300, 50, 108, 192)

    lateinit var fingered: TextView
    lateinit var pulseCountTV: TextView
    lateinit var heartRateTV: TextView
    var pulseCount = 0

    //camera
    lateinit private var mOpenCvCameraView: CameraBridgeViewBase

    private val mLoaderCallback = object : BaseLoaderCallback(this) {

        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    Log.i(TAG, "OpenCV loaded successfully")

                    /* Now enable camera view to start receiving frames */
//                    mOpenCvCameraView!!.setOnTouchListener(this@HeartRateActivity)
                    mOpenCvCameraView!!.enableView()
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.heartrate_activity)
//        fingered = findViewById(R.id.fingered)
//        pulseCountTV = findViewById(R.id.PulseCount)
        heartRateTV = findViewById(R.id.HeartRate)


        rollHistorySeries = SimpleXYSeries("Channel Avg")
        rollHistorySeries.useImplicitXVals()
        // setup the APR History plot:
        aprHistoryPlot = findViewById(R.id.aprHistoryPlot)
        aprHistoryPlot.setRangeBoundaries(-1.0, 1.0, BoundaryMode.FIXED)
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED)
        aprHistoryPlot.addSeries(rollHistorySeries,
                LineAndPointFormatter(
                        Color.rgb(200, 100, 100), null, null, null))
        aprHistoryPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL)

//            aprHistoryPlot.setDomainStepMode(StepMode.INCREMENT_BY_PIXELS);
        aprHistoryPlot.setDomainStepValue(HISTORY_SIZE/10)
        aprHistoryPlot.setLinesPerRangeLabel(3)
        aprHistoryPlot.setLinesPerDomainLabel(6)
        aprHistoryPlot.setDomainLabel("Legend")
        aprHistoryPlot.getDomainTitle().pack()
        aprHistoryPlot.setRangeLabel("Red Value")
        aprHistoryPlot.getRangeTitle().pack()

        redrawer = Redrawer(
                aprHistoryPlot,
                100f, false)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA),
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        mOpenCvCameraView = findViewById(R.id.HelloOpenCvView)
        mOpenCvCameraView.visibility = SurfaceView.VISIBLE
        mOpenCvCameraView.setCvCameraViewListener(this)
    }

    public override fun onPause() {

        redrawer.pause()
        super.onPause()
        if (mOpenCvCameraView != null)
            mOpenCvCameraView!!.disableView()
    }

    public override fun onResume() {
        super.onResume()
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback)
        redrawer.start()
    }

    public override fun onDestroy() {

        redrawer.finish()
        super.onDestroy()
        if (mOpenCvCameraView != null)
            mOpenCvCameraView!!.disableView()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        (mOpenCvCameraView as Tutorial3View).setFlashOn()
    }

    override fun onCameraViewStopped() {}

    /**
     * Processing for checks and filters
     */
    override fun onCameraFrame(inputFrame: Mat): Mat {

        val subImage = Mat(inputFrame, mask)

        if(fingerDetector.check(subImage)){
//            runOnUiThread {
//                fingered.setText("Finger Detected")
//            }
            if(skipEarlyFrames > 0){
                skipEarlyFrames--
                runOnUiThread {
                    heartRateTV.setText("Hold still...")
                }
            }else {
                redData.add(Core.mean(subImage).`val`[0])
                var t = redFilter.process()
                if (redData.isFull()) redData.drop()
                if(pulseDetector.check(t)){
                    pulseCount++
                    rateCalculator.add(System.currentTimeMillis())
                    var r = rateCalculator.getRate()
                    runOnUiThread {
                        heartRateTV.setText("Heart Rate: $r")
                    }

                }

                // get rid the oldest sample in history:
                if (rollHistorySeries.size() > HISTORY_SIZE) {
                    rollHistorySeries.removeFirst()
                }
                // add the next
                rollHistorySeries.addLast(null, t[0])
            }

        }else {
            runOnUiThread {
                heartRateTV.setText("Place your finger over the camera to get your heart rate")
                skipEarlyFrames = 50
                rateCalculator.reset();
            }
        }
        //draw the detection area
        Imgproc.rectangle(inputFrame, Point(
                mask.x.toDouble(), mask.y.toDouble()),
                Point((mask.x + mask.width).toDouble(), (mask.y + mask.height).toDouble()),
                Scalar(0.0, 255.0, 0.0)
        )

    return inputFrame

    }
}


/*
 *
 */



//
//
//internal class StepDetector {
//    var lastStep: Double = 0.toDouble()
//    var timeBuffer = 500
//
//    private val TAG = "SC:StepDetector"
//
//    fun detectStep(window: List<*>): Boolean {
//        for (i in 0 until window.size - 1) {
//            if (window[i] as Double > 0 && window[i + 1] as Double<0) {
//                Log.d(TAG, "system: " + System.currentTimeMillis())
//                Log.d(TAG, "last step: " + lastStep)
//                if (System.currentTimeMillis() - lastStep > timeBuffer) {
//                    lastStep = System.currentTimeMillis().toDouble()
//                    return true
//                }
//
//            }
//        }
//        return false
//    }
//}
