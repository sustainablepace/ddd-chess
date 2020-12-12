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
                captureType = CaptureType.ALLOWED,
                pieceCanTakeMultipleSteps = true,
                rotations = true
            )
        )

        private val knightMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.lShaped(),
                captureType = CaptureType.ALLOWED,
                rotations = true
            ),
            MoveRule(
                direction = -Direction.lShaped(),
                captureType = CaptureType.ALLOWED,
                rotations = true
            )
        )

        private val bishopMoveRules = MoveRuleSet(
            MoveRule(
                direction = Direction.diagonal(),
                captureType = CaptureType.ALLOWED,
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
                captureType = CaptureType.ALLOWED,
                rotations = true
            ),
            MoveRule(
                direction = Direction.straightLine(),
                captureType = CaptureType.ALLOWED,
                rotations = true
            ),
            MoveRule(
                direction = -Direction.castlingMove(), // queenside
                captureType = CaptureType.DISALLOWED,
                moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                    when (piece) {
                        is WhiteKing -> departureSquare == "e1" &&
                            arrivalSquare == "c1" &&
                            chessGame.pieceOn("a1") is WhiteRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn("b1") is NoPiece &&
                            chessGame.pieceOn("c1") is NoPiece &&
                            chessGame.pieceOn("d1") is NoPiece
                        is BlackKing -> departureSquare == "e8" &&
                            arrivalSquare == "c8" &&
                            chessGame.pieceOn("a8") is BlackRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn("b8") is NoPiece &&
                            chessGame.pieceOn("c8") is NoPiece &&
                            chessGame.pieceOn("d8") is NoPiece
                        else -> false
                    }
                }
            ),
            MoveRule(
                direction = Direction.castlingMove(), // kingside
                captureType = CaptureType.DISALLOWED,
                moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                    when (piece) {
                        is WhiteKing -> departureSquare == "e1" &&
                            arrivalSquare == "g1" &&
                            chessGame.pieceOn("h1") is WhiteRook &&
                            chessGame.whiteCastlingOptions.kingSide &&
                            chessGame.pieceOn("f1") is NoPiece &&
                            chessGame.pieceOn("g1") is NoPiece
                        is BlackKing -> departureSquare == "e8" &&
                            arrivalSquare == "g8" &&
                            chessGame.pieceOn("h8") is BlackRook &&
                            chessGame.whiteCastlingOptions.queenSide &&
                            chessGame.pieceOn("f8") is NoPiece &&
                            chessGame.pieceOn("g8") is NoPiece
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
                captureType = CaptureType.DISALLOWED,
                moveCondition = { chessGame, departureSquare, arrivalSquare, piece ->
                    when (piece.side) {
                        White -> departureSquare.rank() == '2' &&
                            chessGame.pieceOn(departureSquare.upperNeighbour()!!) is NoPiece &&
                            chessGame.pieceOn(arrivalSquare) is NoPiece
                        Black -> departureSquare.rank() == '7' &&
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
                moveCondition = { chessGame, departureSquare, _, piece ->
                    val neighbourSquare = departureSquare.rightNeighbour()
                    if (
                        chessGame.enPassantSquare is Square &&
                        chessGame.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && piece.side != neighbourPiece.side
                    } else false
                }
            ),
            MoveRule(
                direction = -Direction.diagonal(),
                captureType = CaptureType.MANDATORY,
                moveCondition = { chessGame, departureSquare, _, piece ->
                    val neighbourSquare = departureSquare.leftNeighbour()
                    if (
                        chessGame.enPassantSquare is Square &&
                        chessGame.enPassantSquare == neighbourSquare
                    ) {
                        val neighbourPiece = chessGame.pieceOn(neighbourSquare)
                        neighbourPiece is Pawn && piece.side != neighbourPiece.side
                    } else false
                }
            )
        )
    }
}