package com.hcii.kennethrstclairiii.heart_rate


class DataWindow(
        val minWindowLength: Int = 10
){
    var rawData: MutableList<Double> = arrayListOf()

    private val TAG = "SC:WindowedList"

//
    fun add(d: Double): DataWindow {
        val b = rawData.add(d)
        //        Log.d(TAG, b + " | adding double to rawData:" + d);
        //        Log.d(TAG, String.valueOf(rawData));
        return this
    }

    fun drop(n: Int = 1): DataWindow {

        for (i in 0..n-1) rawData.removeAt(i)
        return this
    }

    fun isReady(): Boolean{
        return rawData.count() > minWindowLength
    }
    fun isFull(): Boolean{
        return rawData.count() > minWindowLength
    }

}