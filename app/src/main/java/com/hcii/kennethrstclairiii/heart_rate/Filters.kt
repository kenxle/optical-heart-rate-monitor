package com.hcii.kennethrstclairiii.heart_rate

import android.util.Log

/**
 * Created by kennethrstclairiii on 10/20/17.
 */

interface Filter{
    fun process(): List<Double>
}

class RedFilter(val w: DataWindow) : Filter {

    val TAG = "RedFilter"

    override fun process(): List<Double> {
        if(w.isReady()){
            return  w.rawData
                    .demean()
                    .normalize()
//                    .innerThreshhold(0.4)
//                    .medianFilter(3)

                    .meanFilter(2)
                    .meanFilter(2)
                    .meanFilter(4)
                    .meanFilter(8)
                    .meanFilter(2)
//                    .medianFilter(medianFilterValue)
//                    .innerThreshhold(0.3)
                    .differentiate()

        }
        else{
            Log.d(TAG, "DataWindow not ready. Size: ${w.rawData.size}")
            return listOf(0.0)
        }
    }
}