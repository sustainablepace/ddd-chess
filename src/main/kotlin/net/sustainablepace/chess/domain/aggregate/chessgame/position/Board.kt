package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.BoardValueObject
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.Piece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.PieceOrNoPiece
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.Square
import net.sustainablepace.chess.domain.move.ValidMove

interface Board {
    fun pieceOn(square: Square?): PieceOrNoPiece
    fun movePiece(move: ValidMove, movingPiece: Piece, enPassantSquare: EnPassantSquare): Board

    val whiteSquares: Set<Square>
    val blackSquares: Set<Square>
    val whiteKing: Square?
    val blackKing: Square?
    val pieces: List<Pair<Square, Piece>>
    val whitePieces: List<Pair<Square, Piece>>
    val blackPieces: List<Pair<Square, Piece>>
    val isDeadPosition: Boolean
}

fun board(squares: Map<Square, Piece>): Board = BoardValueObject(squares)


