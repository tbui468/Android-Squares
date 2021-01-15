package com.example.androidsquares

import java.util.Stack

data class UndoData(val transformation: Transformation, val index: IntArray, val size: Int)
data class FractalData(val index: IntArray, val size: Int)

data class PuzzleData(val elements: Array<FractalType>, val undoStack: Stack<UndoData>, val maxTransformations: Int, var isLocked: Boolean, var isCleared: Boolean)
data class SetData(val puzzleData: Array<PuzzleData?>, var isLocked: Boolean, var isCleared: Boolean, val pos: FloatArray)
data class AppData(val setData: Array<SetData>, var defaultData: Boolean)

val puzzle00 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle01 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle02 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle03 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val puzzle04 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Red,
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle05 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.BlueB, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Normal, FractalType.Blue,
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle06 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.BlueB,
        FractalType.RedB, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val puzzle07 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Red, FractalType.BlueB, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

//teach reflection
val puzzle10 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.RedB,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle11 = PuzzleData(arrayOf(
        FractalType.Red, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.RedB, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.BlueB, FractalType.Empty,
        FractalType.Blue, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle12 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle13 = PuzzleData(arrayOf(
        FractalType.Blue, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Red, FractalType.BlueB,
        FractalType.Empty, FractalType.Blue, FractalType.Blue, FractalType.RedB,
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

//teach rotation
val puzzle14 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Normal, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Red, FractalType.Blue, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val puzzle15 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.BlueB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle16 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle17 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Red, FractalType.Blue, FractalType.Normal, FractalType.Red,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

/*
//good puzzle, but it can be solved using both reflection and rotation - so it's best to put it in the next puzzle set
val puzzle?? = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Red,
        FractalType.BlueB, FractalType.Normal, FractalType.Red, FractalType.Blue,
        FractalType.Empty, FractalType.BlueB, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
*/
val set0 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 21f, 0f))
val set1 = SetData(arrayOf(puzzle10, puzzle11, puzzle12, puzzle13, puzzle14, puzzle15, puzzle16, puzzle17,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 7f, 0f))
val set2 = SetData(arrayOf(null, null, null, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(-8f, -7f, 0f))
val set3 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(-8f, -21f, 0f))
val set4 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, 21f, 0f))
val set5 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, 7f, 0f))
val set6 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, -7f, 0f))
val set7 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, -21f, 0f))

val appData = AppData(arrayOf(set0, set1, set2, set3, set4, set5, set6, set7), true)
