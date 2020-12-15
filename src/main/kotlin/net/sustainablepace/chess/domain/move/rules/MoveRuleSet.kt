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
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == C1 &&
                            position.pieceOn(A1) is WhiteRook &&
                            position.whiteCastlingOptions.queenSide &&
                            position.pieceOn(B1) is NoPiece &&
                            position.pieceOn(C1) is NoPiece &&
                            position.pieceOn(D1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == C8 &&
                            position.pieceOn(A8) is BlackRook &&
                            position.blackCastlingOptions.queenSide &&
                            position.pieceOn(B8) is NoPiece &&
                            position.pieceOn(C8) is NoPiece &&
                            position.pieceOn(D8) is NoPiece
                        else -> false
                    }
                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == E1 &&
                            arrivalSquare == G1 &&
                            position.pieceOn(H1) is WhiteRook &&
                            position.whiteCastlingOptions.kingSide &&
                            position.pieceOn(F1) is NoPiece &&
                            position.pieceOn(G1) is NoPiece
                        is BlackKing -> departureSquare == E8 &&
                            arrivalSquare == G8 &&
                            position.pieceOn(H8) is BlackRook &&
                            position.blackCastlingOptions.queenSide &&
                            position.pieceOn(F8) is NoPiece &&
                            position.pieceOn(G8) is NoPiece
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
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (val movingPiece = position.pieceOn(departureSquare)) {
                        is NoPiece -> false
                        is Piece -> when (movingPiece.side) {
                            White -> departureSquare.rank == '2' &&
                                position.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                            Black -> departureSquare.rank == '7' &&
                                position.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                        }
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
                moveCondition = { departureSquare, _, position ->
                    val neighbourSquare = departureSquare.rightNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        position.enPassantSquare is Square &&
                        position.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Pawn && movingPiece.side != neighbourPiece.side
                    } else false
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { departureSquare, _, position ->
                    val neighbourSquare = departureSquare.leftNeighbour()
                    val movingPiece = position.pieceOn(departureSquare)
                    if (
                        position.enPassantSquare is Square &&
                        position.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = position.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && movingPiece is Pawn && movingPiece.side != neighbourPiece.side
                    } else false
                }
            )
        )
    }
}