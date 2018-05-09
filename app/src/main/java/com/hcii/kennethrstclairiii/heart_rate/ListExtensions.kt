package com.hcii.kennethrstclairiii.heart_rate

/**
 * Created by kennethrstclairiii on 10/20/17.
 */

/**
 * List extensions, to make processing way easier.
 * In this writing, only work on Lists of Doubles, not Lists of Ints or Sets of Doubles
 */
fun List<Double>.demean(): List<Double> {return this.map{it - (this.sum() / this.count())}}

fun List<Double>.absMax(): Double {
    var m = this[0]
    for (e in this){
        if (Math.abs(e) > m){
            m = Math.abs(e)
        }
    }
    return m
}

fun List<Double>.normalize(): List<Double> {
    return this.map{it / this.absMax()}
}

// forward-difference. result set is one element shorter
fun List<Double>.differentiate(): List<Double>{
    var dst: MutableList<Double> = arrayListOf()

    this.mapIndexed loop@{ index, it ->
        if (index+1 > this.size -1) return@loop //checks if we have enough room, else breaks the for loop
        dst.add( this[index+1] - this[index] )
    }
    return dst
}

// forward medianFilter
fun List<Double>.medianFilter(n: Int): List<Double> {
    var dst: MutableList<Double> = arrayListOf()

    this.mapIndexed loop@{index, it ->
        val top = index+n-1
        if(top > this.size-1) return@loop //checks if we have enough room, else breaks the for loop
        var s = this.slice(index..top)
        dst.add(s.median())

    }
    return dst
}


// forward meanFilter
fun List<Double>.meanFilter(n: Int): List<Double> {
    var dst: MutableList<Double> = arrayListOf()

    this.mapIndexed loop@{index, it ->
        val top = index+n-1
        if(top > this.size-1) return@loop //checks if we have enough room, else breaks the for loop
        var s = this.slice(index..top)
        dst.add(s.average())

    }
    return dst
}

fun List<Double>.median(): Double {
    val even = this.size % 2 == 0
    val s = this.sorted()
    if (even){
        return (s[size/2] + s[(size/2) -1]) / 2
    }else{
        val midI = Math.floor(s.size/2.0).toInt()
        return this[midI]
    }
}

fun List<Double>.innerThreshhold(d: Double): List<Double> {
    return this.map{ if (Math.abs(it) < d) 0.0 else it }
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)