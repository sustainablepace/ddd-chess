package net.sustainablepace.chess.domain.move.rules

import net.sustainablepace.chess.domain.aggregate.chessgame.*

class MoveRuleSet(val moveRules: Set<MoveRule>) {
    constructor(vararg moveOptions: Set<MoveRule>) : this(moveOptions.flatMap { it }.toSet())
    constructor(vararg moveOptions: MoveRuleSet) : this(moveOptions.flatMap { it.moveRules }.toSet())

    operator fun unaryMinus(): MoveRuleSet = MoveRuleSet(moveRules.map { -it }.toSet())

    companion object {
        fun getRulesForPiece(piece: Piece): Set<MoveRule> =
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
            }.moveRules

        private val rookMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = MoveRule.CaptureType.ALLOWED,
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val knightMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.lShaped(),
                captureType = MoveRule.CaptureType.ALLOWED,
                rotations = true
            ),
            MoveRule(
                direction = -Direction.lShaped(),
                captureType = MoveRule.CaptureType.ALLOWED,
                rotations = true
            )
        )

        private val bishopMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MoveRule.CaptureType.ALLOWED,
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
                captureType = MoveRule.CaptureType.ALLOWED,
                rotations = true
            ),
            MoveRule(
                direction = Direction.straightLine(),
                captureType = MoveRule.CaptureType.ALLOWED,
                rotations = true
            ),
            // TODO Unify both castling rules (?)
            MoveRule(
                direction = -Direction.castlingMove(), // queenside
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { departureSquare, arrivalSquare, position ->
                    with(position) {
                        when (pieceOn(departureSquare)) {
                            is WhiteKing -> departureSquare == e1 &&
                                arrivalSquare == c1 &&
                                pieceOn(a1) is WhiteRook &&
                                whiteCastlingOptions.queenSide &&
                                pieceOn(b1) is NoPiece &&
                                pieceOn(c1) is NoPiece &&
                                pieceOn(d1) is NoPiece &&
                                !isSquareThreatenedBy(e1, Black) &&
                                !isSquareThreatenedBy(b1, Black) &&
                                !isSquareThreatenedBy(c1, Black) &&
                                !isSquareThreatenedBy(d1, Black)
                            is BlackKing ->
                                blackCastlingOptions.queenSide &&
                                    departureSquare == e8 && !isSquareThreatenedBy(e8, White) &&
                                    pieceOn(d8) is NoPiece && !isSquareThreatenedBy(d8, White) &&
                                    arrivalSquare == c8 &&
                                    pieceOn(c8) is NoPiece && !isSquareThreatenedBy(c8, White) &&
                                    pieceOn(b8) is NoPiece && !isSquareThreatenedBy(b8, White) &&
                                    pieceOn(a8) is BlackRook
                            else -> false
                        }
                    }

                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (position.pieceOn(departureSquare)) {
                        is WhiteKing -> departureSquare == e1 &&
                            arrivalSquare == g1 &&
                            position.pieceOn(h1) is WhiteRook &&
                            position.whiteCastlingOptions.kingSide &&
                            position.pieceOn(f1) is NoPiece &&
                            position.pieceOn(g1) is NoPiece &&
                            !position.isSquareThreatenedBy(e1, Black) &&
                            !position.isSquareThreatenedBy(f1, Black) &&
                            !position.isSquareThreatenedBy(g1, Black)
                        is BlackKing -> departureSquare == e8 &&
                            arrivalSquare == g8 &&
                            position.pieceOn(h8) is BlackRook &&
                            position.blackCastlingOptions.kingSide &&
                            position.pieceOn(f8) is NoPiece &&
                            position.pieceOn(g8) is NoPiece &&
                            !position.isSquareThreatenedBy(e8, White) &&
                            !position.isSquareThreatenedBy(f8, White) &&
                            !position.isSquareThreatenedBy(g8, White)
                        else -> false
                    }
                }
            )
        )

        private val pawnMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.straightLine(),
                captureType = MoveRule.CaptureType.DISALLOWED
            ),
            MoveRule(
                direction = Direction.initialPawnMove(),
                captureType = MoveRule.CaptureType.DISALLOWED,
                moveCondition = { departureSquare, arrivalSquare, position ->
                    when (val movingPiece = position.pieceOn(departureSquare)) {
                        is NoPiece -> false
                        is Piece -> when (movingPiece.side) {
                            White -> departureSquare.rank == 2 &&
                                position.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                            Black -> departureSquare.rank == 7 &&
                                position.pieceOn(departureSquare.lowerNeighbour()!!) is NoPiece &&
                                position.pieceOn(arrivalSquare) is NoPiece
                        }
                    }
                }
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY
            ),
            MoveRule(
                direction = Direction.diagonal(),
                captureType = MoveRule.CaptureType.MANDATORY,
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
                captureType = MoveRule.CaptureType.MANDATORY,
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