package com.hcii.kennethrstclairiii.heart_rate

/**
 * Created by kennethrstclairiii on 10/20/17.
 */

class RateCalculator(
        val timeWindowMin: Long = 10_000, //10s
        val timeWindowMax: Long = 60_000  //60s
){
    val pulseTimes: MutableList<Long> = arrayListOf<Long>()

    fun add(d: Long){
        pulseTimes.add(d)
    }

    fun timeRange(): Long{
        return pulseTimes.last() - pulseTimes.first()
    }

    fun trimTo(millis: Long){
        while(timeRange() > millis){
            pulseTimes.removeAt(0)
        }
    }

    fun getRate(): String{
        if(timeRange() < timeWindowMin)
            return "Please wait"
        trimTo(timeWindowMax)
        return "${(pulseTimes.count().toDouble() / timeRange() *1000*60).format(1)}"
    }

    fun reset() {
        pulseTimes.clear()
    }
}