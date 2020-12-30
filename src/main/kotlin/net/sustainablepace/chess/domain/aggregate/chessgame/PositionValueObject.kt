package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.event.PieceMovedOnBoard
import net.sustainablepace.chess.domain.event.PieceMovedOnBoardOrNot
import net.sustainablepace.chess.domain.event.PieceNotMovedOnBoard
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRule.CaptureType.DISALLOWED
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet

typealias EnPassantSquare = Square?

interface MoveOptionsCalculator {
    fun moveOptions(): Set<ValidMove>
}

interface Position: MoveOptionsCalculator {
    fun movePiece(move: ValidMove): PieceMovedOnBoardOrNot

    fun isInCheck(): Boolean
    fun isInCheck(side: Side): Boolean
    fun isSquareThreatenedBy(threatenedSquare: Square, side: Side): Boolean
    fun pieceOn(square: Square?): PieceOrNoPiece
    fun moveOptionsForSquare(square: Square): Set<ValidMove>
    fun isCheckMate(): Boolean
    fun isStaleMate(): Boolean
    fun isDeadPosition(): Boolean

    val board: Board
    val enPassantSquare: EnPassantSquare
    val whiteCastlingOptions: CastlingOptions
    val blackCastlingOptions: CastlingOptions
    val turn: Side
}

fun position(
    board: Board = PositionValueObject.defaultBoard,
    enPassantSquare: EnPassantSquare = null,
    whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    blackCastlingOptions: CastlingOptions = CastlingOptions(Black),
    turn: Side = White
) = PositionValueObject(board, enPassantSquare, whiteCastlingOptions, blackCastlingOptions, turn)

data class PositionValueObject(
    override val board: Board = defaultBoard,
    override val enPassantSquare: EnPassantSquare = null,
    override val whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    override val blackCastlingOptions: CastlingOptions = CastlingOptions(Black),
    override val turn: Side = White
) : Position {

    override fun pieceOn(square: Square?): PieceOrNoPiece = board[square] ?: NoPiece

    override fun moveOptionsForSquare(square: Square): Set<ValidMove> =
        pieceOn(square).let { pieceToBeMoved ->
            when (pieceToBeMoved) {
                is NoPiece -> emptySet()
                is Piece -> when {
                    else -> MoveRuleSet.getRulesForPiece(pieceToBeMoved).flatMap { rule ->
                        rule.findMoves(square, this)
                    }.toSet()
                }
            }
        }

    override fun moveOptions(): Set<ValidMove> =
        board
            .findSquares(turn)
            .flatMap { square -> moveOptionsForSquare(square) }
            .filterNot { move -> movePiece(move).isInCheck(turn) }
            .toSet()

    override fun isCheckMate(): Boolean = moveOptions().isEmpty() && isInCheck()
    override fun isStaleMate(): Boolean = moveOptions().isEmpty() && !isInCheck()
    override fun isDeadPosition(): Boolean = board.isDeadPosition()

    override fun isInCheck(): Boolean = isInCheck(turn)

    override fun isInCheck(side: Side): Boolean =
        board.findKing(side)?.let { threatenedKingSquare ->
            isSquareThreatenedBy(threatenedKingSquare, !side)
        } ?: false

    override fun isSquareThreatenedBy(threatenedSquare: Square, side: Side): Boolean {
        board.findPieces(side).forEach { (square, pieceToBeMoved) ->
            val rules = MoveRuleSet.getRulesForPiece(pieceToBeMoved)
            for (rule in rules) {
                if (rule.captureType != DISALLOWED) {
                    if (rule.isThreatened(threatenedSquare, this, square)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun movePiece(move: ValidMove): PieceMovedOnBoardOrNot =
        when (val movingPiece = pieceOn(move.departureSquare)) {
            is NoPiece -> PieceNotMovedOnBoard(
                position = this,
                pieceCapturedOrPawnMoved = false,
                move = move
            )
            is Piece -> PieceMovedOnBoard(
                move = move,
                position = position(
                    board = board.movePiece(move, movingPiece, enPassantSquare),
                    enPassantSquare = if (
                        movingPiece is Pawn &&
                        move.departureSquare.rank diff move.arrivalSquare.rank == 2
                    ) move.arrivalSquare else null,
                    whiteCastlingOptions = whiteCastlingOptions.updateAfterPieceMoved(
                        move.departureSquare,
                        movingPiece
                    ),
                    blackCastlingOptions = blackCastlingOptions.updateAfterPieceMoved(
                        move.departureSquare,
                        movingPiece
                    ),
                    turn = !turn
                ),
                pieceCapturedOrPawnMoved = pieceOn(move.arrivalSquare) is Piece || movingPiece is Pawn
            )
        }

    companion object {
        val defaultBoard = mapOf(
            a1 to WhiteRook,
            b1 to WhiteKnight,
            c1 to WhiteBishop,
            d1 to WhiteQueen,
            e1 to WhiteKing,
            f1 to WhiteBishop,
            g1 to WhiteKnight,
            h1 to WhiteRook,
            a2 to WhitePawn,
            b2 to WhitePawn,
            c2 to WhitePawn,
            d2 to WhitePawn,
            e2 to WhitePawn,
            f2 to WhitePawn,
            g2 to WhitePawn,
            h2 to WhitePawn,
            a8 to BlackRook,
            b8 to BlackKnight,
            c8 to BlackBishop,
            d8 to BlackQueen,
            e8 to BlackKing,
            f8 to BlackBishop,
            g8 to BlackKnight,
            h8 to BlackRook,
            a7 to BlackPawn,
            b7 to BlackPawn,
            c7 to BlackPawn,
            d7 to BlackPawn,
            e7 to BlackPawn,
            f7 to BlackPawn,
            g7 to BlackPawn,
            h7 to BlackPawn
        )
    }
}
