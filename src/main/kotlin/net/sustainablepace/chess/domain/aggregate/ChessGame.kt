package net.sustainablepace.chess.domain.aggregate

import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.aggregate.chessgame.*
import net.sustainablepace.chess.domain.move.ValidMove

class ChessGame private constructor(
    override val id: ChessGameId,
    override val position: Position,
    override val turn: Side,
    override val white: Player,
    override val black: Player,
    override val status: Status,
    override val numberOfNextMove: Int = 1,
    override val fiftyMoveRule: Int = 0
) : ChessGameEvent {

    override fun getActivePlayer(): Player = if (turn == White) white else black

    override fun pieceOn(arrivalSquare: Square): PieceOrNoPiece = position.pieceOn(arrivalSquare)

    override fun moveOptions(): Set<ValidMove> = position.moveOptions(turn)

    override fun movePiece(move: ValidMove): PieceMovedOrNot =
        when (moveOptions().contains(move)) {
            false -> PieceNotMoved("Move $move not possible in game $id.", this)
            true -> position.movePiece(move).let { event ->
                when (event) {
                    is PositionNotUpdated -> PieceNotMoved("Move $move not executed in game $id.", this)
                    is PositionUpdated -> PieceMoved(
                        move = move,
                        chessGame = ChessGame(
                            id = id,
                            position = event.position,
                            turn = !turn,
                            numberOfNextMove = numberOfNextMove + 1,
                            white = white,
                            black = black,
                            status = when {
                                event.position.isCheckMate(!turn) -> Checkmate
                                event.position.isStaleMate(!turn) -> Stalemate
                                (if (event.pieceCapturedOrPawnMoved) 0 else (fiftyMoveRule + 1)) >= 50 -> FiftyMoveRule
                                else -> status
                            },
                            fiftyMoveRule = if (event.pieceCapturedOrPawnMoved) 0 else (fiftyMoveRule + 1)
                        )
                    )
                }
            }
        }

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
                    status = InProgress,
                    fiftyMoveRule = 0
                )
            )

    }
}