package com.example.androidsquares

data class PuzzleData(val elements: Array<FractalType>, var isLocked: Boolean, var isCleared: Boolean)
data class SetData(val puzzleData: Array<PuzzleData?>, var isLocked: Boolean, var isCleared: Boolean, val pos: FloatArray)
data class AppData(val setData: Array<SetData>, var defaultData: Boolean)

val puzzle00 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.BlueB, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB), false, false)


val puzzle01 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.BlueB, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.BlueB, FractalType.Empty, FractalType.Empty), true, false)

val puzzle02 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Normal, FractalType.Blue,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB), true, false)

val puzzle03 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB), true, false)

val puzzle04 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty), true, false)

val puzzle05 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.BlueB, FractalType.BlueB,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB), true, false)


val puzzle06 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.BlueB, FractalType.BlueB,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.Empty, FractalType.RedB, FractalType.RedB, FractalType.RedB), true, false)

val puzzle07 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.RedB, FractalType.RedB, FractalType.RedB, FractalType.Empty), true, false)


val set0 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
                            null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 21f, 0f))
val set1 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
                            null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 7f, 0f))

val appData = AppData(arrayOf(set0, set1), true)
