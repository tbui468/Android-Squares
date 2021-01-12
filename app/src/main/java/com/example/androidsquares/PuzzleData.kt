package com.example.androidsquares


val puzzle00 = arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.BlueB)

val puzzle01 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty)

val puzzle02 = arrayOf(
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)

val puzzle03 = arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty)

val puzzle04 = arrayOf(
        FractalType.Blue, FractalType.Normal, FractalType.Red, FractalType.Normal,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.RedB,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)

val puzzle05 = arrayOf(
        FractalType.Red, FractalType.Blue, FractalType.Red, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Red, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty)


val cubeData0: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[Surface.Front.value] = puzzle00
    it[Surface.Back.value] = puzzle01
    it[Surface.Left.value] = puzzle02
    it[Surface.Right.value] = puzzle03
    it[Surface.Top.value] = puzzle04
    it[Surface.Bottom.value] = puzzle05
}

val puzzle10 = arrayOf(
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB)

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

val cubeData1: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}.also {
    it[Surface.Front.value] = puzzle10
    it[Surface.Back.value] = puzzle11
    it[Surface.Left.value] = puzzle12
    it[Surface.Right.value] = puzzle13
    it[Surface.Top.value] = puzzle14
    it[Surface.Bottom.value] = puzzle15
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
    it[Surface.Front.value] = puzzle20
    it[Surface.Back.value] = puzzle21
    it[Surface.Left.value] = puzzle22
    it[Surface.Right.value] = puzzle23
    it[Surface.Top.value] = puzzle24
    it[Surface.Bottom.value] = puzzle25
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
    it[Surface.Front.value] = puzzle30
    it[Surface.Back.value] = puzzle31
    it[Surface.Left.value] = puzzle30
    it[Surface.Right.value] = puzzle30
    it[Surface.Top.value] = puzzle30
    it[Surface.Bottom.value] = puzzle30
}


val cubeData4: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData5: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData6: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}
val cubeData7: Array<Array<FractalType>> = Array(6){Array(16){FractalType.Normal}}


//three colors - 4 transformations
//four colors - 4 transformations
//four colors - 4 transformations

val puzzleData = arrayOf(cubeData0, cubeData1, cubeData2, cubeData3, cubeData4, cubeData5, cubeData6, cubeData7)
