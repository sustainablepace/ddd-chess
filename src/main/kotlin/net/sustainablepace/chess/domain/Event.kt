package net.sustainablepace.chess.domain

interface Event

data class PiecesSetUp(val chessGame: ChessGame): Event
data class PieceMoved(val move: Move, val chessGame: ChessGame): Event
data class MoveCalculated(val move: Move): Event