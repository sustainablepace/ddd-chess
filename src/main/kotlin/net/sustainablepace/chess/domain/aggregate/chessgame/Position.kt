package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.aggregate.chessgame.position.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.Square
import net.sustainablepace.chess.domain.event.PieceMovedOnBoardOrNot
import net.sustainablepace.chess.domain.move.ValidMove

interface Position : MoveOptionsCalculator, Board {
    fun movePiece(move: ValidMove): PieceMovedOnBoardOrNot
    fun isInCheck(side: Side): Boolean

    val isInCheck: Boolean
    val isCheckMate: Boolean
    val isStaleMate: Boolean
    val board: Board
    val enPassantSquare: EnPassantSquare
    val whiteCastlingOptions: CastlingOptions
    val blackCastlingOptions: CastlingOptions
    val turn: Side
}

fun position(
    board: Map<Square, Piece>
) = position(board = board(board))

fun position(
    board: Map<Square, Piece>,
    turn: Side
) = position(board = board(board), turn = turn)

fun position(
    board: Board = PositionValueObject.defaultBoard,
    enPassantSquare: EnPassantSquare = null,
    whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    blackCastlingOptions: CastlingOptions = CastlingOptions(Black),
    turn: Side = White
) = PositionValueObject(board, enPassantSquare, whiteCastlingOptions, blackCastlingOptions, turn)