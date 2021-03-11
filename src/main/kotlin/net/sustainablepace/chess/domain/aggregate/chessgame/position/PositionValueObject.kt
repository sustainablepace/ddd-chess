package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.aggregate.chessgame.position.board.*
import net.sustainablepace.chess.domain.event.PieceMovedOnBoard
import net.sustainablepace.chess.domain.event.PieceMovedOnBoardOrNot
import net.sustainablepace.chess.domain.event.PieceNotMovedOnBoard
import net.sustainablepace.chess.domain.move.ValidMove
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet

typealias EnPassantSquare = Square?

interface MoveOptionsCalculator {
    val moveOptions: Set<ValidMove>
}

data class PositionValueObject(
    override val board: Board = defaultBoard,
    override val enPassantSquare: EnPassantSquare = null,
    override val whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    override val blackCastlingOptions: CastlingOptions = CastlingOptions(Black),
    override val turn: Side = White
) : Position, Board by board {

    override val moveOptions: Set<ValidMove> by lazy {
        when (turn) {
            White -> board.whiteSquares
            Black -> board.blackSquares
        }.flatMap { square -> moveOptionsForSquare(square) }
            .filterNot { move -> movePiece(move).isInCheck(turn) }
            .toSet()
    }

    private fun moveOptionsForSquare(square: Square): Set<ValidMove> =
        pieceOn(square).let { pieceToBeMoved ->
            when (pieceToBeMoved) {
                is NoPiece -> emptySet()
                is Piece -> MoveRuleSet.findMoves(pieceToBeMoved, square, this)
            }
        }

    override val isCheckMate: Boolean by lazy {
        moveOptions.isEmpty() && isInCheck
    }
    override val isStaleMate: Boolean by lazy {
        moveOptions.isEmpty() && !isInCheck
    }

    override val isInCheck: Boolean by lazy {
        isInCheck(turn)
    }

    override fun isInCheck(side: Side): Boolean =
        when (side) {
            White -> board.whiteKing
            Black -> board.blackKing
        }?.let { threatenedKingSquare ->
            MoveRuleSet.isSquareThreatenedBy(
                threatenedSquare = threatenedKingSquare,
                side = !side,
                position = this
            )
        } ?: false

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
        val defaultBoard = board(
            mapOf(
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
        )
    }
}


