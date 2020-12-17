package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.event.PositionChanged
import net.sustainablepace.chess.domain.event.PositionChangedOrNot
import net.sustainablepace.chess.domain.event.PositionEvent
import net.sustainablepace.chess.domain.event.PositionNotChanged
import net.sustainablepace.chess.domain.move.Move
import net.sustainablepace.chess.domain.move.rules.MoveRuleSet

typealias EnPassantSquare = Square?

data class Position(
    override val board: Board,
    override val enPassantSquare: EnPassantSquare = null,
    override val whiteCastlingOptions: CastlingOptions = CastlingOptions(White),
    override val blackCastlingOptions: CastlingOptions = CastlingOptions(Black)
) : PositionEvent {

    constructor() : this(defaultBoard)

    override fun pieceOn(square: Square): PieceOrNoPiece = board[square] ?: NoPiece

    override fun moveOptionsIgnoringCheck(square: Square, ignoreKing: Boolean): Set<Move> =
        board[square].let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece && (!ignoreKing || ignoreKing && pieceToBeMoved !is King)) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(square, pieceToBeMoved, this)
                }.toSet()
            } else emptySet()
        }

    override fun moveOptions(side: Side): Set<Move> =
        board
            .filter { it.value.side == side }.keys
            .flatMap { square -> moveOptionsIgnoringCheck(square) } // TODO: Castling not allowed in check
            .filter { move ->
                !movePiece(move).isInCheck(side)
            }
            .toSet()

    fun isCheckMate(side: Side): Boolean = moveOptions(side).isEmpty() && isInCheck(side)
    fun isStaleMate(side: Side): Boolean = moveOptions(side).isEmpty() && !isInCheck(side)

    override fun isInCheck(side: Side): Boolean =
        board.findKing(side)?.let { threatenedKingSquare ->
            isSquareThreatenedBy(threatenedKingSquare, !side)
        } ?: false

    override fun isSquareThreatenedBy(threatenedSquare: Square, side: Side): Boolean =
        board.findSquares(side).find { square ->
            moveOptionsIgnoringCheck(square, true)
                .map { it.arrivalSquare }
                .contains(threatenedSquare)
        } is Square

    override fun movePiece(move: Move): PositionChangedOrNot =
        when (val movingPiece = pieceOn(move.departureSquare)) {
            is NoPiece -> PositionNotChanged(
                position = this,
                pieceCapturedOrPawnMoved = false
            )
            is Piece -> mutableMapOf<Square, Piece>().let { updatedBoard ->
                updatedBoard.putAll(board)

                updatedBoard[move.arrivalSquare] = movingPiece
                updatedBoard.remove(move.departureSquare)

                // castling
                if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, c1)) {
                    updatedBoard[d1] = WhiteRook
                    updatedBoard.remove(a1)
                }
                else if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(e1, g1)) {
                    updatedBoard[f1] = WhiteRook
                    updatedBoard.remove(h1)
                }
                else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, c8)) {
                    updatedBoard[d8] = BlackRook
                    updatedBoard.remove(a8)
                }
                else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(e8, g8)) {
                    updatedBoard[f8] = BlackRook
                    updatedBoard.remove(h8)
                }

                // Promotion
                if (updatedBoard[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank == '8') {
                    updatedBoard[move.arrivalSquare] = WhiteQueen
                }
                else if (updatedBoard[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank == '1') {
                    updatedBoard[move.arrivalSquare] = BlackQueen
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

                PositionChanged(
                    position = Position(
                        board = updatedBoard,
                        enPassantSquare = with(move) {
                            if (
                                movingPiece is Pawn &&
                                departureSquare.rank diff arrivalSquare.rank == 2
                            ) arrivalSquare else null
                        },
                        whiteCastlingOptions = whiteCastlingOptions.updateAfterPieceMoved(
                            move.departureSquare,
                            movingPiece
                        ),
                        blackCastlingOptions = blackCastlingOptions.updateAfterPieceMoved(
                            move.departureSquare,
                            movingPiece
                        )
                    ),
                    pieceCapturedOrPawnMoved = board[move.arrivalSquare] is Piece || movingPiece is Pawn
                )
            }
        }

    companion object {
        private val defaultBoard = mapOf(
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
