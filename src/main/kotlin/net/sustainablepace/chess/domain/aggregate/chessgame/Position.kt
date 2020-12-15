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

    override fun moveOptionsIgnoringCheck(square: Square): Set<Move> =
        board[square].let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(square, pieceToBeMoved, this)
                }.toSet()
            } else emptySet()
        }

    override fun moveOptions(side: Side): Set<Move> =
        board
            .filter { it.value.side == side }.keys
            .flatMap { square -> moveOptionsIgnoringCheck(square) }
            .filter { move ->
                !movePiece(move).isInCheck(side) // TODO: Castling not allowed in check
            }
            .toSet()

    fun isCheckMate(side: Side): Boolean = moveOptions(side).isEmpty() && isInCheck(side)
    fun isStaleMate(side: Side): Boolean = moveOptions(side).isEmpty() && !isInCheck(side)

    override fun isInCheck(side: Side): Boolean =
        board.findKing(side)?.let { threatenedKingSquare ->
            board.findSquares(!side).find { square ->
                moveOptionsIgnoringCheck(square)
                    .map { it.arrivalSquare }
                    .contains(threatenedKingSquare)
            } is Square
        } ?: false

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
                if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(E1, c1)) {
                    updatedBoard[d1] = WhiteRook
                    updatedBoard.remove(a1)
                }
                else if (updatedBoard[move.arrivalSquare] is WhiteKing && move == Move(E1, G1)) {
                    updatedBoard[F1] = WhiteRook
                    updatedBoard.remove(H1)
                }
                else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(E8, c8)) {
                    updatedBoard[d8] = BlackRook
                    updatedBoard.remove(a8)
                }
                else if (updatedBoard[move.arrivalSquare] is BlackKing && move == Move(E8, G8)) {
                    updatedBoard[F8] = BlackRook
                    updatedBoard.remove(H8)
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
            E1 to WhiteKing,
            F1 to WhiteBishop,
            G1 to WhiteKnight,
            H1 to WhiteRook,
            a2 to WhitePawn,
            b2 to WhitePawn,
            c2 to WhitePawn,
            d2 to WhitePawn,
            E2 to WhitePawn,
            F2 to WhitePawn,
            G2 to WhitePawn,
            H2 to WhitePawn,
            a8 to BlackRook,
            b8 to BlackKnight,
            c8 to BlackBishop,
            d8 to BlackQueen,
            E8 to BlackKing,
            F8 to BlackBishop,
            G8 to BlackKnight,
            H8 to BlackRook,
            a7 to BlackPawn,
            b7 to BlackPawn,
            c7 to BlackPawn,
            d7 to BlackPawn,
            E7 to BlackPawn,
            F7 to BlackPawn,
            G7 to BlackPawn,
            H7 to BlackPawn
        )
    }
}
