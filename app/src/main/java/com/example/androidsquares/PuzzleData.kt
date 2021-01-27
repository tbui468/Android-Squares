package com.example.androidsquares

import java.util.Stack

data class UndoData(val transformation: Transformation, val index: IntArray, val size: Int)
data class FractalData(val index: IntArray, val size: Int)

data class PuzzleData(var elements: Array<F>, val undoStack: Stack<UndoData>, val maxTransformations: Int, var isLocked: Boolean, var isCleared: Boolean)
data class SetData(val puzzleData: Array<PuzzleData?>, var isLocked: Boolean, var isCleared: Boolean, val pos: FloatArray)
data class AppData(val setData: Array<SetData>)

val p00 = PuzzleData(arrayOf(
        F.E, F.E, F.E, F.BB, F.E,
        F.B, F.N, F.N, F.N, F.E,
        F.E, F.E, F.E, F.BB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p01 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.BB, F.N, F.E, F.E, F.E,
        F.N, F.N, F.E, F.E, F.E,
        F.B, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p02 = PuzzleData(arrayOf(
        F.E, F.B, F.E, F.E, F.E,
        F.BB, F.N, F.N, F.BB, F.E,
        F.E, F.E, F.N, F.E, F.E,
        F.E, F.E, F.B, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p03 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.B, F.N, F.B, F.E, F.E,
        F.E, F.N, F.E, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)


val p04 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.B, F.N, F.N, F.R, F.E,
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p05 = PuzzleData(arrayOf(
        F.E, F.E, F.RB, F.E, F.E,
        F.BB, F.R, F.R, F.E, F.E,
        F.E, F.BB, F.N, F.B, F.E,
        F.E, F.E, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p06 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.N, F.R, F.BB, F.E,
        F.RB, F.B, F.E, F.E, F.E,
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)


val p07 = PuzzleData(arrayOf(
        F.BB, F.R, F.BB, F.E, F.E,
        F.N, F.N, F.B, F.E, F.E,
        F.RB, F.N, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

//two transformation puzzles
val p08 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.B, F.N, F.N, F.E, F.E,
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p09 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.N, F.B, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.B, F.N, F.E, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p010 = PuzzleData(arrayOf(
        F.B, F.N, F.E, F.E, F.E,
        F.E, F.N, F.BB, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p011 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.N, F.B, F.B, F.E, F.E,
        F.BB, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
//connecting two lines of the same color
val p012 = PuzzleData(arrayOf(
        F.BB, F.E, F.E, F.RB, F.E,
        F.N, F.B, F.R, F.N, F.E,
        F.BB, F.E, F.E, F.RB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p013 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.RB, F.N, F.B, F.R, F.BB,
        F.E, F.E, F.E, F.BB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p014 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.B, F.N, F.R, F.E, F.E,
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p015 = PuzzleData(arrayOf(
        F.E, F.N, F.R, F.E, F.E,
        F.BB, F.N, F.B, F.RB, F.E,
        F.E, F.BB, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)

//teach reflection
val p10 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.N, F.N, F.RB, F.E,
        F.R, F.N, F.N, F.E, F.E,
        F.E, F.E, F.R, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p11 = PuzzleData(arrayOf(
        F.R, F.RB, F.E, F.E, F.E,
        F.N, F.N, F.RB, F.E, F.E,
        F.N, F.N, F.BB, F.E, F.E,
        F.B, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p12 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.R, F.N, F.N, F.BB, F.E,
        F.B, F.N, F.B, F.RB, F.E,
        F.E, F.E, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p13 = PuzzleData(arrayOf(
        F.B, F.BB, F.E, F.E, F.E,
        F.N, F.N, F.E, F.E, F.E,
        F.R, F.N, F.R, F.BB, F.E,
        F.E, F.B, F.B, F.RB, F.E,
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

//teach rotation
val p14 = PuzzleData(arrayOf(
        F.RB, F.N, F.RB, F.E, F.E,
        F.E, F.N, F.E, F.E, F.E,
        F.E, F.N, F.B, F.E, F.E,
        F.BB, F.R, F.B, F.BB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)


val p15 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.B, F.N, F.B, F.BB, F.E,
        F.E, F.N, F.N, F.N, F.E,
        F.E, F.E, F.E, F.BB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p16 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.BB, F.N, F.RB, F.E, F.E,
        F.E, F.B, F.N, F.E, F.E,
        F.E, F.N, F.R, F.E, F.E,
        F.E, F.R, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)

val p17 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.R, F.B, F.N, F.R, F.E,
        F.N, F.N, F.B, F.E, F.E,
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, true, false)
//new reflection only 2 transformation puzzles
val p18 = PuzzleData(arrayOf(
        F.E, F.E, F.N, F.E, F.E,
        F.B, F.N, F.B, F.E, F.E,
        F.E, F.N, F.N, F.BB, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p19 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.B, F.N, F.N, F.E, F.E,
        F.B, F.N, F.N, F.E, F.E,
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p110 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.RB, F.N, F.R, F.E, F.E,
        F.N, F.N, F.B, F.E, F.E,
        F.N, F.N, F.BB, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p111 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.B, F.N, F.R, F.E,
        F.N, F.B, F.R, F.E, F.E,
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p112 = PuzzleData(arrayOf(
        F.E, F.B, F.E, F.E, F.E,
        F.E, F.N, F.N, F.N, F.E,
        F.BB, F.B, F.N, F.E, F.E,
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p113 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.B, F.B, F.N, F.E, F.E,
        F.N, F.N, F.BB, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p114 = PuzzleData(arrayOf(
        F.BB, F.B, F.B, F.N, F.BB,
        F.E, F.B, F.N, F.N, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p115 = PuzzleData(arrayOf(
        F.E, F.GB, F.E, F.E, F.E,
        F.G, F.N, F.G, F.E, F.E,
        F.N, F.N, F.N, F.E, F.E,
        F.E, F.GB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
/*
val puzzle112 = PuzzleData(arrayOf(
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)*/


val p20 = PuzzleData(arrayOf(
        F.BB, F.E, F.E, F.E, F.E,
        F.N, F.BB, F.E, F.E, F.E,
        F.N, F.N, F.B, F.N, F.E,
        F.N, F.N, F.R, F.N, F.E,
        F.N, F.RB, F.E, F.E, F.E,
        F.RB, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)


val p21 = PuzzleData(arrayOf(
        F.E, F.N, F.N, F.E, F.E,
        F.BB, F.N, F.N, F.BB, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.N, F.B, F.E, F.E,
        F.E, F.B, F.N, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p22 = PuzzleData(arrayOf(
        F.BB, F.N, F.N, F.BB, F.E,
        F.E, F.R, F.N, F.R, F.E,
        F.B, F.N, F.B, F.E, F.E,
        F.RB, F.N, F.N, F.RB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p23 = PuzzleData(arrayOf(
        F.E, F.E, F.RB, F.E, F.E,
        F.E, F.B, F.N, F.E, F.E,
        F.BB, F.B, F.R, F.E, F.E,
        F.E, F.B, F.R, F.RB, F.E,
        F.E, F.N, F.R, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p24 = PuzzleData(arrayOf(
        F.E, F.G, F.BB, F.E, F.E,
        F.E, F.N, F.R, F.BB, F.E,
        F.RB, F.B, F.N, F.GB, F.E,
        F.E, F.RB, F.GB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p25 = PuzzleData(arrayOf(
        F.E, F.E, F.GB, F.E, F.E,
        F.E, F.E, F.N, F.G, F.E,
        F.BB, F.N, F.G, F.B, F.E,
        F.E, F.BB, F.GB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p26 = PuzzleData(arrayOf(
        F.RB, F.E, F.E, F.E, F.E,
        F.G, F.R, F.E, F.E, F.E,
        F.N, F.R, F.G, F.GB, F.E,
        F.E, F.R, F.R, F.E, F.E,
        F.E, F.RB, F.GB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p27 = PuzzleData(arrayOf(
        F.BB, F.E, F.GB, F.RB, F.E,
        F.N, F.B, F.B, F.G, F.E,
        F.G, F.R, F.R, F.G, F.E,
        F.BB, F.GB, F.E, F.RB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)


val p30 = PuzzleData(arrayOf(
        F.E, F.E, F.GB, F.BB, F.E,
        F.B, F.B, F.G, F.B, F.E,
        F.B, F.N, F.N, F.G, F.E,
        F.B, F.N, F.N, F.G, F.E,
        F.B, F.G, F.G, F.G, F.E,
        F.GB, F.BB, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p31 = PuzzleData(arrayOf(
        F.RB, F.GB, F.E, F.E, F.E,
        F.G, F.G, F.E, F.E, F.E,
        F.R, F.G, F.G, F.E, F.E,
        F.R, F.N, F.N, F.GB, F.E,
        F.R, F.R, F.E, F.E, F.E,
        F.RB, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p32 = PuzzleData(arrayOf(
        F.RB, F.E, F.E, F.RB, F.E,
        F.N, F.N, F.N, F.N, F.E,
        F.R, F.R, F.R, F.R, F.E,
        F.G, F.G, F.G, F.G, F.E,
        F.N, F.N, F.N, F.N, F.E,
        F.GB, F.E, F.E, F.GB, F.E),
        Stack<UndoData>(), 3, false, false)


val p33 = PuzzleData(arrayOf(
        F.RB, F.E, F.E, F.RB, F.E,
        F.N, F.N, F.N, F.N, F.E,
        F.G, F.R, F.R, F.G, F.E,
        F.R, F.G, F.G, F.R, F.E,
        F.N, F.N, F.N, F.N, F.E,
        F.GB, F.E, F.E, F.GB, F.E),
        Stack<UndoData>(), 3, false, false)

val p34 = PuzzleData(arrayOf(
        F.E, F.BB, F.GB, F.E, F.E,
        F.N, F.B, F.B, F.N, F.E,
        F.N, F.B, F.B, F.N, F.E,
        F.G, F.G, F.G, F.G, F.E,
        F.N, F.N, F.N, F.N, F.E,
        F.E, F.BB, F.GB, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p35 = PuzzleData(arrayOf(
        F.E, F.E, F.GB, F.BB, F.E,
        F.N, F.N, F.B, F.G, F.E,
        F.B, F.G, F.B, F.G, F.E,
        F.B, F.B, F.G, F.G, F.E,
        F.B, F.G, F.N, F.N, F.E,
        F.GB, F.BB, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)


val p36 = PuzzleData(arrayOf(
        F.E, F.E, F.GB, F.BB, F.E,
        F.B, F.G, F.N, F.N, F.E,
        F.N, F.B, F.G, F.N, F.E,
        F.N, F.B, F.G, F.N, F.E,
        F.B, F.G, F.N, F.N, F.E,
        F.E, F.E, F.GB, F.BB, F.E),
        Stack<UndoData>(), 3, false, false)

val p37 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.R, F.R, F.B, F.B, F.E,
        F.N, F.R, F.B, F.N, F.E,
        F.R, F.N, F.N, F.B, F.E,
        F.N, F.R, F.B, F.N, F.E,
        F.RB, F.E, F.E, F.BB, F.E),
        Stack<UndoData>(), 3, false, false)


//temp placeholder puzzle
val p40 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.N, F.B, F.E, F.E,
        F.E, F.B, F.B, F.RB, F.E,
        F.BB, F.R, F.R, F.E, F.E,
        F.E, F.R, F.N, F.E, F.E,
        F.E, F.E, F.BB, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p41 = PuzzleData(arrayOf(
        F.RB, F.E, F.E, F.BB, F.E,
        F.N, F.R, F.N, F.N, F.E,
        F.N, F.B, F.R, F.B, F.E,
        F.RB, F.E, F.E, F.BB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p42 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.BB, F.E,
        F.B, F.B, F.B, F.N, F.E,
        F.N, F.R, F.R, F.R, F.E,
        F.RB, F.E, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p43 = PuzzleData(arrayOf(
        F.E, F.E, F.BB, F.E, F.E,
        F.RB, F.R, F.B, F.E, F.E,
        F.E, F.B, F.N, F.R, F.E,
        F.B, F.N, F.R, F.E, F.E,
        F.E, F.R, F.B, F.BB, F.E,
        F.E, F.RB, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p44 = PuzzleData(arrayOf(
        F.E, F.GB, F.E, F.E, F.E,
        F.RB, F.N, F.G, F.E, F.E,
        F.R, F.G, F.R, F.GB, F.E,
        F.G, F.R, F.RB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p45 = PuzzleData(arrayOf( //easy - should switch to earlier set (only one color)
        F.E, F.GB, F.E, F.E, F.E,
        F.G, F.G, F.E, F.E, F.E,
        F.G, F.N, F.N, F.GB, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p46 = PuzzleData(arrayOf(
        F.GB, F.RB, F.E, F.E, F.E,
        F.G, F.G, F.E, F.E, F.E,
        F.G, F.R, F.G, F.RB, F.E,
        F.R, F.R, F.G, F.GB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p47 = PuzzleData(arrayOf(
        F.RB, F.E, F.E, F.E, F.E,
        F.N, F.B, F.BB, F.E, F.E,
        F.R, F.N, F.R, F.E, F.E,
        F.RB, F.N, F.B, F.E, F.E,
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p50 = PuzzleData(arrayOf( //easy peasy - should go in an earlier set
        F.E, F.N, F.B, F.RB, F.E,
        F.RB, F.B, F.B, F.E, F.E,
        F.E, F.N, F.G, F.GB, F.E,
        F.GB, F.G, F.G, F.E, F.E,
        F.E, F.N, F.R, F.BB, F.E,
        F.BB, F.R, F.R, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p51 = PuzzleData(arrayOf(
        F.RB, F.R, F.R, F.E, F.E,
        F.E, F.R, F.R, F.E, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.E, F.N, F.E, F.E,
        F.E, F.R, F.R, F.E, F.E,
        F.E, F.R, F.N, F.RB, F.E),
        Stack<UndoData>(), 3, false, false)
val p52 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.B, F.R, F.E, F.E,
        F.E, F.R, F.B, F.E, F.E,
        F.RB, F.N, F.N, F.BB, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.R, F.B, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p53 = PuzzleData(arrayOf(
        F.E, F.E, F.B, F.BB, F.E,
        F.E, F.E, F.N, F.N, F.E,
        F.E, F.B, F.B, F.E, F.E,
        F.E, F.B, F.B, F.E, F.E,
        F.B, F.N, F.E, F.E, F.E,
        F.BB, F.B, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p54 = PuzzleData(arrayOf(
        F.E, F.RB, F.BB, F.E, F.E,
        F.E, F.G, F.G, F.E, F.E,
        F.RB, F.B, F.R, F.BB, F.E,
        F.GB, F.B, F.R, F.GB, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
//temp placeholder puzzle
val p55 = PuzzleData(arrayOf(
        F.R, F.B, F.B, F.RB, F.E,
        F.R, F.R, F.R, F.BB, F.E,
        F.B, F.R, F.E, F.E, F.E,
        F.R, F.R, F.R, F.BB, F.E,
        F.R, F.B, F.B, F.RB, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
//temp placeholder puzzle
val p56 = PuzzleData(arrayOf( //give players 3 or 4 transformations max, but miminum is 2
        F.GB, F.E, F.E, F.E, F.E,
        F.N, F.G, F.N, F.E, F.E,
        F.N, F.G, F.G, F.GB, F.E,
        F.N, F.N, F.G, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p57 = PuzzleData(arrayOf( ///give players up to 3 transformations
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.B, F.N, F.E, F.E,
        F.B, F.N, F.B, F.E, F.E,
        F.B, F.N, F.E, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)

//let's do more 2 or 3 transformation puzzles
val p60 = PuzzleData(arrayOf(
        F.E, F.E, F.RB, F.E, F.E,
        F.BB, F.R, F.B, F.E, F.E,
        F.E, F.R, F.B, F.E, F.E,
        F.E, F.R, F.B, F.RB, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p61 = PuzzleData(arrayOf(
        F.E, F.GB, F.BB, F.E, F.E,
        F.GB, F.B, F.B, F.E, F.E,
        F.BB, F.B, F.G, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)

val p62 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.G, F.R, F.GB, F.E,
        F.G, F.R, F.R, F.E, F.E,
        F.RB, F.E, F.GB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p63 = PuzzleData(arrayOf(
        F.GB, F.BB, F.E, F.E, F.E,
        F.B, F.G, F.N, F.BB, F.E,
        F.B, F.G, F.N, F.E, F.E,
        F.GB, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p64 = PuzzleData(arrayOf(
        F.RB, F.E, F.GB, F.E, F.E,
        F.R, F.R, F.N, F.E, F.E,
        F.N, F.G, F.G, F.E, F.E,
        F.RB, F.E, F.GB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p65 = PuzzleData(arrayOf(
        F.E, F.BB, F.E, F.E, F.E,
        F.N, F.N, F.E, F.E, F.E,
        F.B, F.B, F.B, F.E, F.E,
        F.E, F.N, F.N, F.E, F.E,
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p66 = PuzzleData(arrayOf(
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.R, F.E, F.E, F.E,
        F.R, F.B, F.N, F.E, F.E,
        F.RB, F.N, F.R, F.BB, F.E,
        F.E, F.E, F.BB, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 2, false, false)
val p67 = PuzzleData(arrayOf( //can be easily solved in 4 transformations
        F.RB, F.E, F.GB, F.E, F.E,
        F.R, F.R, F.N, F.E, F.E,
        F.N, F.G, F.G, F.E, F.E,
        F.N, F.G, F.G, F.E, F.E,
        F.R, F.R, F.N, F.E, F.E,
        F.RB, F.E, F.GB, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p70 = PuzzleData(arrayOf( //can be solved in 3.  give players 4
        F.E, F.E, F.E, F.BB, F.E,
        F.E, F.E, F.G, F.N, F.BB,
        F.E, F.N, F.N, F.B, F.E,
        F.GB, F.N, F.N, F.E, F.E,
        F.E, F.GB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

val p71 = PuzzleData(arrayOf( //can be solved in 2.  Give players 4
        F.E, F.E, F.E, F.GB, F.E,
        F.E, F.E, F.E, F.G, F.G,
        F.E, F.E, F.GB, F.N, F.N,
        F.GB, F.G, F.N, F.E, F.E,
        F.E, F.G, F.N, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p72 = PuzzleData(arrayOf( //solved in 3
        F.E, F.N, F.R, F.E, F.E,
        F.E, F.N, F.R, F.RB, F.E,
        F.R, F.N, F.E, F.E, F.E,
        F.R, F.N, F.E, F.E, F.E,
        F.E, F.N, F.R, F.RB, F.E,
        F.E, F.N, F.R, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p73 = PuzzleData(arrayOf( //can be solved in 2.  Give players 4
        F.E, F.BB, F.E, F.E, F.E,
        F.E, F.B, F.N, F.N, F.GB,
        F.E, F.G, F.N, F.G, F.E,
        F.BB, F.B, F.B, F.G, F.E,
        F.E, F.E, F.E, F.GB, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p74 = PuzzleData(arrayOf( //easy 2 moves.  Put in tutorial
        F.BB, F.E, F.E, F.E, F.E,
        F.N, F.B, F.E, F.E, F.E,
        F.BB, F.RB, F.E, F.E, F.E,
        F.R, F.N, F.E, F.E, F.E,
        F.E, F.RB, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p75 = PuzzleData(arrayOf( //easy 3 moves. put in tutorial
        F.E, F.B, F.E, F.E, F.E,
        F.BB, F.N, F.BB, F.E, F.E,
        F.E, F.E, F.N, F.B, F.E,
        F.BB, F.N, F.BB, F.E, F.E,
        F.E, F.B, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p76 = PuzzleData(arrayOf( //solve in 3
        F.G, F.G, F.GB, F.E, F.E,
        F.N, F.N, F.B, F.BB, F.E,
        F.GB, F.G, F.N, F.N, F.E,
        F.E, F.BB, F.B, F.B, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)
val p77 = PuzzleData(arrayOf( //2 moves (can't really be solved in a reasonale number of move higher than that)
        F.E, F.E, F.GB, F.E, F.RB,
        F.E, F.R, F.R, F.R, F.R,
        F.GB, F.R, F.N, F.N, F.N,
        F.E, F.R, F.N, F.G, F.G,
        F.RB, F.R, F.N, F.G, F.N,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)

//temp placeholder puzzle
val p = PuzzleData(arrayOf(
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E,
        F.E, F.E, F.E, F.E, F.E),
        Stack<UndoData>(), 3, false, false)


//0, 1, 5, 8, 9, 10, 11, 14

//val set0 = SetData(arrayOf(puzzle00, puzzle01, puzzle02, puzzle03, puzzle04, puzzle05, puzzle06, puzzle07,
 //       p08, p09, p010, p011, p012, p013, p014, p015), false, false, floatArrayOf(-8f, 21f, 0f))
val set0 = SetData(arrayOf(
        p00, p01, null, null,
        null, p02, null, null,
        p04, p03, p05, p06,
        null, null, p07, null
), false, false, floatArrayOf(-8f, 21f, 0f))
val set1 = SetData(arrayOf(p10, p11, p12, p13, p14, p15, p16, p17,
        p18, p19, p110, p111, p112, p113, p114, p115), false, false, floatArrayOf(8f, 21f, 0f))
val set2 = SetData(arrayOf(p20, p21, p22, p23, p24, p25, p26, p27,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(-8f, 7f, 0f))
val set3 = SetData(arrayOf(p30, p31, p32, p33, p34, p35, p36, p37,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, 7f, 0f))
val set4 = SetData(arrayOf(p40, p41, p42, p43, p44, p45, p46, p47,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(-8f, -7f, 0f))
val set5 = SetData(arrayOf(p50, p51, p52, p53, p54, p55, p56, p57,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, -7f, 0f))
val set6 = SetData(arrayOf(p60, p61, p62, p63, p64, p65, p66, p67,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(-8f, -21f, 0f))
val set7 = SetData(arrayOf(p70, p71, p72, p73, p74, p75, p76, p77,
        null, null, null, null, null, null, null, null), true, false, floatArrayOf(8f, -21f, 0f))

//val defaultAppData = AppData(arrayOf(set0, set1, set2, set3, set4, set5, set6, set7))
val defaultAppData = AppData(arrayOf(set0, set1, set2, set3, set4, set5, set6, set7))
var appData = defaultAppData.copy()
