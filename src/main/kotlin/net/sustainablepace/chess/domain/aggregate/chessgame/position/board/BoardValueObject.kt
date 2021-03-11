package net.sustainablepace.chess.domain.aggregate.chessgame.position.board

import net.sustainablepace.chess.domain.aggregate.chessgame.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.White
import net.sustainablepace.chess.domain.aggregate.chessgame.position.Board
import net.sustainablepace.chess.domain.aggregate.chessgame.position.EnPassantSquare
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.PromotionMove
import net.sustainablepace.chess.domain.move.ValidMove

data class BoardValueObject(val squares: Map<Square, Piece>) : Board {

    override fun pieceOn(square: Square?): PieceOrNoPiece = squares[square] ?: NoPiece

    override val whiteKing: Square? by lazy { squares.filter { it.value is King && it.value.side == White }.keys.firstOrNull() }
    override val blackKing: Square? by lazy { squares.filter { it.value is King && it.value.side == Black }.keys.firstOrNull() }
    override val whiteSquares: Set<Square> by lazy { squares.filter { it.value.side == White }.keys }
    override val blackSquares: Set<Square> by lazy { squares.filter { it.value.side == Black }.keys }
    override val pieces: List<Pair<Square, Piece>> by lazy { squares.toList() }
    override val whitePieces: List<Pair<Square, Piece>> by lazy { squares.filter { it.value.side == White }.toList() }
    override val blackPieces: List<Pair<Square, Piece>> by lazy { squares.filter { it.value.side == Black }.toList() }
    override val isDeadPosition: Boolean by lazy {
        containsOnlyKings ||
            containsOnlyKingsAndOneBishop ||
            containsOnlyKingsAndOneKnight ||
            containsOnlyKingsAndTwoBishopsOnSameSquareColour
    }

    private val allPieces: Set<Piece> by lazy { squares.values.toSet() }
    private val containsOnlyKings: Boolean by lazy {
        squares.size == 2 && allPieces == setOf(
            WhiteKing,
            BlackKing
        )
    }
    private val containsOnlyKingsAndOneBishop: Boolean by lazy {
        squares.size == 3 && (allPieces == setOf(WhiteKing, BlackKing, WhiteBishop) || allPieces == setOf(
            WhiteKing,
            BlackKing,
            BlackBishop
        ))
    }

    private val containsOnlyKingsAndOneKnight by lazy {
        squares.size == 3 && (allPieces == setOf(WhiteKing, BlackKing, WhiteKnight) || allPieces == setOf(
            WhiteKing,
            BlackKing,
            BlackKnight
        ))
    }

    private val containsOnlyKingsAndTwoBishopsOnSameSquareColour by lazy {
        squares.size == 4 &&
            allPieces == setOf(WhiteKing, BlackKing, WhiteBishop, BlackBishop) &&
            squares
                .filter { it.value is Bishop }
                .map { it.key.colour() }.distinct().size == 1
    }

    override fun movePiece(move: ValidMove, movingPiece: Piece, enPassantSquare: EnPassantSquare): Board {
        val updatedBoard = squares.toMutableMap()

        updatedBoard[move.arrivalSquare] = when (move) {
            is Move -> movingPiece
            is PromotionMove -> move.piece
        }
        updatedBoard.remove(move.departureSquare)

        // castling
        if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, c1)) {
            updatedBoard[d1] = WhiteRook
            updatedBoard.remove(a1)
        } else if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, g1)) {
            updatedBoard[f1] = WhiteRook
            updatedBoard.remove(h1)
        } else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, c8)) {
            updatedBoard[d8] = BlackRook
            updatedBoard.remove(a8)
        } else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, g8)) {
            updatedBoard[f8] = BlackRook
            updatedBoard.remove(h8)
        }

        // En passant capturing
        if (enPassantSquare != null) {
            val lowerNeighbour = enPassantSquare.lowerNeighbour()
            val upperNeighbour = enPassantSquare.upperNeighbour()
            if (movingPiece.side == White && upperNeighbour != null && updatedBoard[upperNeighbour] is WhitePawn) {
                updatedBoard.remove(enPassantSquare)
            } else if (movingPiece.side == Black && lowerNeighbour != null && updatedBoard[lowerNeighbour] is BlackPawn) {
                updatedBoard.remove(enPassantSquare)
            }
        }
        return board(updatedBoard)
    }
}