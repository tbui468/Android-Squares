package com.example.androidsquares

val puzzle00 = arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.BlueB, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB)

val puzzle01 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.BlueB, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.BlueB, FractalType.Empty, FractalType.Empty)

val puzzle02 = arrayOf(
        FractalType.BlueB, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Blue,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB)

val puzzle03 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB)

val puzzle04 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty)

val puzzle05 = arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB)


val puzzle06 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.Empty, FractalType.RedB, FractalType.RedB, FractalType.RedB)

val puzzle07 = arrayOf(
        FractalType.BlueB, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.RedB, FractalType.RedB, FractalType.RedB, FractalType.Empty)

val puzzle08 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Red,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)




val cubeData0: Array<Array<FractalType>> = Array(16){Array(24){FractalType.Normal}}.also {
    for(i in 0 until 16) {
        it[i] = emptyArray()
    }
    it[8] = puzzle00
    it[9] = puzzle01
    it[10] = puzzle02
    it[6] = puzzle03
    it[2] = puzzle04
    it[3] = puzzle05
    it[14] = puzzle06
    it[15] = puzzle07
}


val puzzle10 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Blue, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.BlueB)

val puzzle11 = arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal)

val puzzle12 = arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.BlueB,
        FractalType.Normal, FractalType.Blue, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)

val puzzle13 = arrayOf(
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.RedB,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Red, FractalType.Empty)

val puzzle14 = arrayOf(
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)

val puzzle15 = arrayOf(
        FractalType.Empty, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.RedB,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty)

val cubeData1: Array<Array<FractalType>> = Array(16){Array(24){FractalType.Normal}}.also {
    for(i in 0 until 16) {
        it[i] = emptyArray()
    }
    it[8] = puzzle10
    it[9] = puzzle01
    it[10] = puzzle02
    it[6] = puzzle03
    it[2] = puzzle04
    it[3] = puzzle05
    it[14] = puzzle06
    it[15] = puzzle07
}


val puzzle20 = arrayOf(
        FractalType.Empty, FractalType.Blue, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.GreenB, FractalType.BlueB, FractalType.Empty)

val puzzle21 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Green, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Blue, FractalType.BlueB)

val puzzle22 = arrayOf(
        FractalType.Empty, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Green, FractalType.GreenB,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty)

val puzzle23 = arrayOf(
        FractalType.Green, FractalType.Blue, FractalType.RedB, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.GreenB, FractalType.BlueB,
        FractalType.Normal, FractalType.Green, FractalType.Normal, FractalType.Red,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Green)

val puzzle24 = arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.GreenB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Green,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty)

val puzzle25 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.RedB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Blue, FractalType.Green, FractalType.Red, FractalType.Normal,
        FractalType.Normal, FractalType.Normal, FractalType.GreenB, FractalType.Empty)


val cubeData2: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[0] = puzzle20
    it[1] = puzzle21
    it[2] = puzzle22
    it[3] = puzzle23
    it[4] = puzzle24
    it[5] = puzzle25
}

val puzzle30 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.RedB,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.Normal,
        FractalType.Normal, FractalType.Normal, FractalType.Green, FractalType.Empty,
        FractalType.GreenB, FractalType.Normal, FractalType.Empty, FractalType.Empty)

val puzzle31 = arrayOf(
        FractalType.Empty, FractalType.GreenB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.BlueB, FractalType.Green, FractalType.Empty, FractalType.Empty)

val cubeData3: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[0] = puzzle30
    it[1] = puzzle31
    it[2] = puzzle30
    it[3] = puzzle30
    it[4] = puzzle30
    it[5] = puzzle30
}


val cubeData4: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData5: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData6: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData7: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}


//three colors - 4 transformations
//four colors - 4 transformations
//four colors - 4 transformations

//val puzzleData = arrayOf(cubeData0, cubeData1, cubeData2, cubeData3, cubeData4, cubeData5, cubeData6, cubeData7)
val puzzleData = arrayOf(cubeData0, cubeData1)
val cubeLocations = arrayOf(floatArrayOf(-8f, 21f, 0f),
        floatArrayOf(-8f, 7f, 0f))/*
        floatArrayOf(-8f, -7f, 0f),
        floatArrayOf(-8f, -21f, 0f),
        floatArrayOf(8f, 21f, 0f),
        floatArrayOf(8f, 7f, 0f),
        floatArrayOf(8f, -7f, 0f),
        floatArrayOf(8f, -21f, 0f))*/
