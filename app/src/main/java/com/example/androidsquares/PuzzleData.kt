package com.example.androidsquares

import java.util.Stack

data class UndoData(val transformation: Transformation, val index: IntArray, val size: Int)
data class FractalData(val index: IntArray, val size: Int)

data class PuzzleData(var elements: Array<FractalType>, val undoStack: Stack<UndoData>, val maxTransformations: Int, var isLocked: Boolean, var isCleared: Boolean)
data class SetData(val puzzleData: Array<PuzzleData?>, var isLocked: Boolean, var isCleared: Boolean, val pos: FloatArray)
data class AppData(val setData: Array<SetData>)

val puzzle00 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle01 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val puzzle02 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)

val puzzle03 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)


val puzzle04 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)

val puzzle05 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Red, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)

val puzzle06 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.BlueB, FractalType.Empty,
        FractalType.RedB, FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)


val puzzle07 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Red, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, true, false)

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


val p20 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Normal,
        FractalType.Normal, FractalType.Normal, FractalType.Red, FractalType.Normal,
        FractalType.Normal, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val p21 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p22 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.Red,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.Normal, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p23 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Red, FractalType.RedB,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p24 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Green, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.BlueB,
        FractalType.RedB, FractalType.Blue, FractalType.Normal, FractalType.GreenB,
        FractalType.Empty, FractalType.RedB, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p25 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Green,
        FractalType.BlueB, FractalType.Normal, FractalType.Green, FractalType.Blue,
        FractalType.Empty, FractalType.BlueB, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p26 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Red, FractalType.Green, FractalType.GreenB,
        FractalType.Empty, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p27 = PuzzleData(arrayOf(
        FractalType.BlueB, FractalType.Empty, FractalType.GreenB, FractalType.RedB,
        FractalType.Normal, FractalType.Blue, FractalType.Blue, FractalType.Green,
        FractalType.Green, FractalType.Red, FractalType.Red, FractalType.Green,
        FractalType.BlueB, FractalType.GreenB, FractalType.Empty, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val p30 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.BlueB,
        FractalType.Blue, FractalType.Blue, FractalType.Green, FractalType.Blue,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Green,
        FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.Green,
        FractalType.Blue, FractalType.Green, FractalType.Green, FractalType.Green,
        FractalType.GreenB, FractalType.BlueB, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p31 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.GreenB, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Green, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.GreenB,
        FractalType.Red, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p32 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.RedB,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Red, FractalType.Red, FractalType.Red, FractalType.Red,
        FractalType.Green, FractalType.Green, FractalType.Green, FractalType.Green,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.GreenB, FractalType.Empty, FractalType.Empty, FractalType.GreenB),
        Stack<UndoData>(), 3, false, false)


val p33 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.RedB,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Green, FractalType.Red, FractalType.Red, FractalType.Green,
        FractalType.Red, FractalType.Green, FractalType.Green, FractalType.Red,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.GreenB, FractalType.Empty, FractalType.Empty, FractalType.GreenB),
        Stack<UndoData>(), 3, false, false)

val p34 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.GreenB, FractalType.Empty,
        FractalType.Normal, FractalType.Blue, FractalType.Blue, FractalType.Normal,
        FractalType.Normal, FractalType.Blue, FractalType.Blue, FractalType.Normal,
        FractalType.Green, FractalType.Green, FractalType.Green, FractalType.Green,
        FractalType.Normal, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.BlueB, FractalType.GreenB, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p35 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.BlueB,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Green,
        FractalType.Blue, FractalType.Green, FractalType.Blue, FractalType.Green,
        FractalType.Blue, FractalType.Blue, FractalType.Green, FractalType.Green,
        FractalType.Blue, FractalType.Green, FractalType.Normal, FractalType.Normal,
        FractalType.GreenB, FractalType.BlueB, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val p36 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.BlueB,
        FractalType.Blue, FractalType.Green, FractalType.Normal, FractalType.Normal,
        FractalType.Normal, FractalType.Blue, FractalType.Green, FractalType.Normal,
        FractalType.Normal, FractalType.Blue, FractalType.Green, FractalType.Normal,
        FractalType.Blue, FractalType.Green, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.BlueB),
        Stack<UndoData>(), 3, false, false)

val p37 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Red, FractalType.Red, FractalType.Blue, FractalType.Blue,
        FractalType.Normal, FractalType.Red, FractalType.Blue, FractalType.Normal,
        FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Blue,
        FractalType.Normal, FractalType.Red, FractalType.Blue, FractalType.Normal,
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.BlueB),
        Stack<UndoData>(), 3, false, false)


//temp placeholder puzzle
val p40 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Blue, FractalType.RedB,
        FractalType.BlueB, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p41 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.BlueB,
        FractalType.Normal, FractalType.Red, FractalType.Normal, FractalType.Normal,
        FractalType.Normal, FractalType.Blue, FractalType.Red, FractalType.Blue,
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p42 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.BlueB,
        FractalType.Blue, FractalType.Blue, FractalType.Blue, FractalType.Normal,
        FractalType.Normal, FractalType.Red, FractalType.Red, FractalType.Red,
        FractalType.RedB, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p43 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.RedB, FractalType.Red, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Red,
        FractalType.Blue, FractalType.Normal, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.BlueB,
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p44 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.GreenB, FractalType.Empty, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.Green, FractalType.Empty,
        FractalType.Red, FractalType.Green, FractalType.Red, FractalType.GreenB,
        FractalType.Green, FractalType.Red, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p45 = PuzzleData(arrayOf( //easy - should switch to earlier set (only one color)
        FractalType.Empty, FractalType.GreenB, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Green, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Normal, FractalType.Normal, FractalType.GreenB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p46 = PuzzleData(arrayOf(
        FractalType.GreenB, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Green, FractalType.Empty, FractalType.Empty,
        FractalType.Green, FractalType.Red, FractalType.Green, FractalType.RedB,
        FractalType.Red, FractalType.Red, FractalType.Green, FractalType.GreenB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p47 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Blue, FractalType.BlueB, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Red, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p50 = PuzzleData(arrayOf( //easy peasy - should go in an earlier set
        FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.RedB,
        FractalType.RedB, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Green, FractalType.GreenB,
        FractalType.GreenB, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.BlueB,
        FractalType.BlueB, FractalType.Red, FractalType.Red, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p51 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.RedB),
        Stack<UndoData>(), 3, false, false)
val p52 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Red, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p53 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Blue, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p54 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.RedB, FractalType.Blue, FractalType.Red, FractalType.BlueB,
        FractalType.GreenB, FractalType.Blue, FractalType.Red, FractalType.GreenB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
//temp placeholder puzzle
val p55 = PuzzleData(arrayOf(
        FractalType.Red, FractalType.Blue, FractalType.Blue, FractalType.RedB,
        FractalType.Red, FractalType.Red, FractalType.Red, FractalType.BlueB,
        FractalType.Blue, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Red, FractalType.Red, FractalType.BlueB,
        FractalType.Red, FractalType.Blue, FractalType.Blue, FractalType.RedB,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
//temp placeholder puzzle
val p56 = PuzzleData(arrayOf( //give players 3 or 4 transformations max, but miminum is 2
        FractalType.GreenB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Green, FractalType.Normal, FractalType.Empty,
        FractalType.Normal, FractalType.Green, FractalType.Green, FractalType.GreenB,
        FractalType.Normal, FractalType.Normal, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p57 = PuzzleData(arrayOf( ///give players up to 3 transformations
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.Blue, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)

//let's do more 2 or 3 transformation puzzles
val p60 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.RedB, FractalType.Empty,
        FractalType.BlueB, FractalType.Red, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Blue, FractalType.RedB,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p61 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.GreenB, FractalType.BlueB, FractalType.Empty,
        FractalType.GreenB, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)

val p62 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Green, FractalType.Red, FractalType.GreenB,
        FractalType.Green, FractalType.Red, FractalType.Red, FractalType.Empty,
        FractalType.RedB, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p63 = PuzzleData(arrayOf(
        FractalType.GreenB, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Green, FractalType.Normal, FractalType.BlueB,
        FractalType.Blue, FractalType.Green, FractalType.Normal, FractalType.Empty,
        FractalType.GreenB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p64 = PuzzleData(arrayOf(
        FractalType.RedB, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Red, FractalType.Red, FractalType.Normal, FractalType.Empty,
        FractalType.Normal, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.RedB, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p65 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Blue, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p66 = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Blue, FractalType.Normal, FractalType.Empty,
        FractalType.RedB, FractalType.Normal, FractalType.Red, FractalType.BlueB,
        FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 2, false, false)
val p67 = PuzzleData(arrayOf( //can be easily solved in 4 transformations
        FractalType.RedB, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Red, FractalType.Red, FractalType.Normal, FractalType.Empty,
        FractalType.Normal, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.Normal, FractalType.Green, FractalType.Green, FractalType.Empty,
        FractalType.Red, FractalType.Red, FractalType.Normal, FractalType.Empty,
        FractalType.RedB, FractalType.Empty, FractalType.GreenB, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p70 = PuzzleData(arrayOf( //can be solved in 3.  give players 4
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.BlueB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Green, FractalType.Normal, FractalType.BlueB,
        FractalType.Empty, FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.GreenB, FractalType.Normal, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.GreenB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

val p71 = PuzzleData(arrayOf( //can be solved in 2.  Give players 4
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Green, FractalType.Green,
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Normal, FractalType.Normal,
        FractalType.GreenB, FractalType.Green, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Green, FractalType.Normal, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p72 = PuzzleData(arrayOf( //solved in 3
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.RedB, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.RedB, FractalType.Empty,
        FractalType.Empty, FractalType.Normal, FractalType.Red, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p73 = PuzzleData(arrayOf( //can be solved in 2.  Give players 4
        FractalType.Empty, FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Normal, FractalType.Normal, FractalType.GreenB,
        FractalType.Empty, FractalType.Green, FractalType.Normal, FractalType.Green, FractalType.Empty,
        FractalType.BlueB, FractalType.Blue, FractalType.Blue, FractalType.Green, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p74 = PuzzleData(arrayOf( //easy 2 moves.  Put in tutorial
        FractalType.BlueB, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Red, FractalType.Normal, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.RedB, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p75 = PuzzleData(arrayOf( //easy 3 moves. put in tutorial
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Normal, FractalType.Blue, FractalType.Empty,
        FractalType.BlueB, FractalType.Normal, FractalType.BlueB, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Blue, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p76 = PuzzleData(arrayOf( //solve in 3
        FractalType.Green, FractalType.Green, FractalType.GreenB, FractalType.Empty, FractalType.Empty,
        FractalType.Normal, FractalType.Normal, FractalType.Blue, FractalType.BlueB, FractalType.Empty,
        FractalType.GreenB, FractalType.Green, FractalType.Normal, FractalType.Normal, FractalType.Empty,
        FractalType.Empty, FractalType.BlueB, FractalType.Blue, FractalType.Blue, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)
val p77 = PuzzleData(arrayOf( //2 moves (can't really be solved in a reasonale number of move higher than that)
        FractalType.Empty, FractalType.Empty, FractalType.GreenB, FractalType.Empty, FractalType.RedB,
        FractalType.Empty, FractalType.Red, FractalType.Red, FractalType.Red, FractalType.Red,
        FractalType.GreenB, FractalType.Red, FractalType.Normal, FractalType.Normal, FractalType.Normal,
        FractalType.Empty, FractalType.Red, FractalType.Normal, FractalType.Green, FractalType.Green,
        FractalType.RedB, FractalType.Red, FractalType.Normal, FractalType.Green, FractalType.Normal,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)

//temp placeholder puzzle
val p = PuzzleData(arrayOf(
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty,
        FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty, FractalType.Empty),
        Stack<UndoData>(), 3, false, false)


val set0 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 21f, 0f))
val set1 = SetData(arrayOf(puzzle10, puzzle11, puzzle12, puzzle13, puzzle14, puzzle15, puzzle16, puzzle17,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, 7f, 0f))
val set2 = SetData(arrayOf(p20, p21, p22, p23, p24, p25, p26, p27,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, -7f, 0f))
val set3 = SetData(arrayOf(p30, p31, p32, p33, p34, p35, p36, p37,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(-8f, -21f, 0f))
val set4 = SetData(arrayOf(p40, p41, p42, p43, p44, p45, p46, p47,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(8f, 21f, 0f))
val set5 = SetData(arrayOf(p50, p51, p52, p53, p54, p55, p56, p57,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(8f, 7f, 0f))
val set6 = SetData(arrayOf(p60, p61, p62, p63, p64, p65, p66, p67,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(8f, -7f, 0f))
val set7 = SetData(arrayOf(p70, p71, p72, p73, p74, p75, p76, p77,
        null, null, null, null, null, null, null, null), false, false, floatArrayOf(8f, -21f, 0f))

//val defaultAppData = AppData(arrayOf(set0, set1, set2, set3, set4, set5, set6, set7))
val defaultAppData = AppData(arrayOf(set0, set7))
var appData = defaultAppData.copy()
