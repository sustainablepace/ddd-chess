package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.*

class MoveRuleSet(val moveRules: Set<MoveRule>) {
    constructor(vararg moveOptions: Set<MoveRule>) : this(moveOptions.flatMap { it }.toSet())
    constructor(vararg moveOptions: MoveRuleSet) : this(moveOptions.flatMap { it.moveRules }.toSet())

    operator fun unaryMinus(): MoveRuleSet = MoveRuleSet(moveRules.map { -it }.toSet())

    companion object {
        fun getRulesForPiece(piece: Piece): MoveRuleSet =
            when (piece) {
                is Rook -> rookMoveRules
                is Knight -> knightMoveRules
                is Bishop -> bishopMoveRules
                is Queen -> queenMoveRules
                is King -> kingMoveRules
                is Pawn -> pawnMoveRules
            }.let { moveRules ->
                when (piece.side) {
                    is White -> moveRules
                    is Black -> -moveRules
                }
            }

        private val rookMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val knightMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.lShaped(),
                rotations = true
            ),
            MoveRule(
                direction = -Direction.lShaped(),
                rotations = true
            )
        )

        private val bishopMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val queenMoveRules = MoveRuleSet(
            bishopMoveRules,
            rookMoveRules
        )

        private val kingMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                rotations = true
            ),
            MoveRule(
                direction = Direction.straightLine(),
                rotations = true
            ),
            MoveRule(
                direction = -Direction.castlingMove(), // queenside
                moveCondition = { chessGame, departureSquare, arrivalSquare ->
                    when (chessGame.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == C1 &&
                            chessGame.pieceOn(A1) is WhiteRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn(B1) is NoPiece &&
                            chessGame.pieceOn(C1) is NoPiece &&
                            chessGame.pieceOn(D1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == C8 &&
                            chessGame.pieceOn(A8) is BlackRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn(B8) is NoPiece &&
                            chessGame.pieceOn(C8) is NoPiece &&
                            chessGame.pieceOn(D8) is NoPiece
                        else -> false
                    }
                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                moveCondition = { chessGame, departureSquare, arrivalSquare ->
                    when (chessGame.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == G1 &&
                            chessGame.pieceOn(H1) is WhiteRook &&
                            chessGame.whiteCastlingOptions.kingSide &&
                            chessGame.pieceOn(F1) is NoPiece &&
                            chessGame.pieceOn(G1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == G8 &&
                            chessGame.pieceOn(H8) is BlackRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn(F8) is NoPiece &&
                            chessGame.pieceOn(G8) is NoPiece
                        else -> false
                    }
                }
            )
        )

        private val pawnMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = CaptureType.DISALLOWED
            ),
            MoveRule(
                direction = Direction.initialPawnMove(),
                moveCondition = { chessGame, departureSquare, arrivalSquare ->
                    when (chessGame.turn) {
                        White -> departureSquare.rank == '2' &&
                            chessGame.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                            chessGame.pieceOn(arrivalSquare) is NoPiece
                        Black -> departureSquare.rank == '7' &&
                            chessGame.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                            chessGame.pieceOn(arrivalSquare) is NoPiece
                    }
                }
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = CaptureType.MANDATORY
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { chessGame, departureSquare, _ ->
                    val neighbourSquare = departureSquare.rightNeighbour()
                    if (
                        chessGame.enPassantSquare is Square &&
                        chessGame.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && chessGame.turn != neighbourPiece.side
                    } else false
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { chessGame, departureSquare, _ ->
                    val neighbourSquare = departureSquare.leftNeighbour()
                    if (
                        chessGame.enPassantSquare is Square &&
                        chessGame.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && chessGame.turn != neighbourPiece.side
                    } else false
                }
            )
        )
    }
}