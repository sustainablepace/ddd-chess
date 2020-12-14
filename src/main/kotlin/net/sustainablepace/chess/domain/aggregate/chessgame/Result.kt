package net.sustainablepace.chess.domain.aggregate.chessgame

sealed class Status
sealed class Result: Status()
sealed class Win(): Result()
sealed class Draw: Result()

object InProgress: Status()
object Checkmate: Win()
object Stalemate: Draw()
object FiftyMoveRule: Draw() // TODO
object DeadPosition: Draw() // TODO
object ThreefoldRepetition: Draw() // TODO