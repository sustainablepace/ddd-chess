package net.sustainablepace.chess.domain.aggregate.chessgame

import net.sustainablepace.chess.domain.event.PositionEvent
import net.sustainablepace.chess.domain.event.PositionNotUpdated
import net.sustainablepace.chess.domain.event.PositionUpdated
import net.sustainablepace.chess.domain.event.PositionUpdatedOrNot
import net.sustainablepace.chess.domain.move.ValidMove
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

    override fun moveOptionsIgnoringCheck(square: Square): Set<ValidMove> =
        board[square].let { pieceToBeMoved ->
            if (pieceToBeMoved is Piece) {
                MoveRuleSet.getRulesForPiece(pieceToBeMoved).moveRules.flatMap { rule ->
                    rule.findMoves(square, pieceToBeMoved, this)
                }.toSet()
            } else emptySet()
        }

    override fun moveOptions(side: Side): Set<ValidMove> =
        board
            .filter { it.value.side == side }.keys
            .flatMap { square -> moveOptionsIgnoringCheck(square) }
            .filter { move ->
                !movePiece(move).isInCheck(side)
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

    override fun movePiece(move: ValidMove): PositionUpdatedOrNot =
        when (val movingPiece = pieceOn(move.departureSquare)) {
            is NoPiece -> PositionNotUpdated(
                position = this,
                pieceCapturedOrPawnMoved = false
            )
            is Piece -> mutableMapOf<Square, Piece>().let { updatedBoard ->
                updatedBoard.putAll(board)

                updatedBoard[move.arrivalSquare] = movingPiece
                updatedBoard.remove(move.departureSquare)

                // castling
                if (updatedBoard[move.arrivalSquare] is WhiteKing && move == ValidMove(E1, C1)) {
                    updatedBoard[D1] = WhiteRook
                    updatedBoard.remove(A1)
                }
                if (updatedBoard[move.arrivalSquare] is WhiteKing && move == ValidMove(E1, G1)) {
                    updatedBoard[F1] = WhiteRook
                    updatedBoard.remove(H1)
                }
                if (updatedBoard[move.arrivalSquare] is BlackKing && move == ValidMove(E8, C8)) {
                    updatedBoard[D8] = BlackRook
                    updatedBoard.remove(A8)
                }
                if (updatedBoard[move.arrivalSquare] is BlackKing && move == ValidMove(E8, G8)) {
                    updatedBoard[F8] = BlackRook
                    updatedBoard.remove(H8)
                }

                // Promotion
                if (updatedBoard[move.arrivalSquare] is WhitePawn && move.arrivalSquare.rank == '8') {
                    updatedBoard[move.arrivalSquare] = WhiteQueen
                }
                if (updatedBoard[move.arrivalSquare] is BlackPawn && move.arrivalSquare.rank == '1') {
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

                PositionUpdated(
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
            A1 to WhiteRook,
            B1 to WhiteKnight,
            C1 to WhiteBishop,
            D1 to WhiteQueen,
            E1 to WhiteKing,
            F1 to WhiteBishop,
            G1 to WhiteKnight,
            H1 to WhiteRook,
            A2 to WhitePawn,
            B2 to WhitePawn,
            C2 to WhitePawn,
            D2 to WhitePawn,
            E2 to WhitePawn,
            F2 to WhitePawn,
            G2 to WhitePawn,
            H2 to WhitePawn,
            A8 to BlackRook,
            B8 to BlackKnight,
            C8 to BlackBishop,
            D8 to BlackQueen,
            E8 to BlackKing,
            F8 to BlackBishop,
            G8 to BlackKnight,
            H8 to BlackRook,
            A7 to BlackPawn,
            B7 to BlackPawn,
            C7 to BlackPawn,
            D7 to BlackPawn,
            E7 to BlackPawn,
            F7 to BlackPawn,
            G7 to BlackPawn,
            H7 to BlackPawn
        )
    }
}
