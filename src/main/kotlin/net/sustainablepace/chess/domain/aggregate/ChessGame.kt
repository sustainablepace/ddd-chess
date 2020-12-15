package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.event.*
import net.sustainablepace.chess.domain.move.Move

class ChessGame private constructor(
    override val id: ChessGameId,
    override val position: Position,
    override val turn: Side,
    override val white: Player,
    override val black: Player,
    override val numberOfNextMove: Int = 1,
    override val movesWithoutCaptureOrPawnMove: Int = 0
) : ChessGameEvent {


    override fun getActivePlayer(): Player = if (turn == White) white else black

    override fun pieceOn(arrivalSquare: Square): PieceOrNoPiece = position.pieceOn(arrivalSquare)

    override fun moveOptions(): Set<Move> = position.moveOptions(turn)

    override fun getStatus(): Status = when {
        position.isCheckMate(turn) -> Checkmate
        position.isStaleMate(turn) -> Stalemate
        movesWithoutCaptureOrPawnMove >= 50 -> FiftyMoveRule
        else -> InProgress
    }

    override fun movePiece(move: Move): PieceMovedOrNot =
        if (move in moveOptions()) {
            when (val event = position.movePiece(move)) {
                is PositionChanged -> PieceMoved(
                    move = move,
                    chessGame = ChessGame(
                        id = id,
                        position = event.position,
                        turn = !turn,
                        numberOfNextMove = numberOfNextMove + 1,
                        white = white,
                        black = black,
                        movesWithoutCaptureOrPawnMove = if (event.pieceCapturedOrPawnMoved) 0 else movesWithoutCaptureOrPawnMove + 1
                    )
                )
                is PositionNotChanged -> PieceNotMoved(
                    reason = "Move $move not executed in game $id.",
                    chessGame = this
                )
            }
        } else PieceNotMoved(
            reason = "Move $move not possible in game $id.",
            chessGame = this
        )


    companion object {
        operator fun invoke(): PiecesHaveBeenSetUp = ChessGame(Position())
        operator fun invoke(position: Position): PiecesHaveBeenSetUp = ChessGame(White, position)
        operator fun invoke(side: Side): PiecesHaveBeenSetUp = ChessGame(side, Position())
        operator fun invoke(side: Side, position: Position): PiecesHaveBeenSetUp =
            PiecesHaveBeenSetUp(
                ChessGame(
                    id = chessGameId(),
                    position = position,
                    turn = side,
                    white = HumanPlayer, //StupidComputerPlayer,
                    black = StupidComputerPlayer,
                    movesWithoutCaptureOrPawnMove = 0
                )
            )

    }
}