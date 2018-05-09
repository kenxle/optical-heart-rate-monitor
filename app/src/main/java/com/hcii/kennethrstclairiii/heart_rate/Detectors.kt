package com.hcii.kennethrstclairiii.heart_rate

import org.opencv.core.Core
import org.opencv.core.Mat

/**
 * Created by kennethrstclairiii on 10/20/17.
 */


class FingerDetector(){

    val TAG = "FingerDetector"

    // pass in the detection region for pulse
    // will check for majority red and low green and low blue
    fun check (m: Mat): Boolean {

        val red = (Core.mean(m).`val`[0]).toInt()
        val green = (Core.mean(m).`val`[1]).toInt()
        val blue = (Core.mean(m).`val`[2]).toInt()
        // if mat has lots of red, little green or blue
//        Log.d(TAG, "Red ${red} Green: ${green} Blue: ${blue} " )

        if(red > 220 && green < 10 && blue < 25){
            return true
        }

        return false
    }

}

class PulseDetector(){
    var wait = 0
    val TAG = "Detector"

    // pass in the detection region for pulse
    // will check for majority red and low green and low blue
    fun check (window: List<Double>): Boolean {

        if(wait > 0) {
            wait--
            return false
        }
        for(i in window.indices){
            if(i <= window.size-2 &&
                    window[i] > 0 && window[i+1] <= 0){ //downward 0 crossing
                wait = i+1
                return true
            }
        }
        return false
    }

}