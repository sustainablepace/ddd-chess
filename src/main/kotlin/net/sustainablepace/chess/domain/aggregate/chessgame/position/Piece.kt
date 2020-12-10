package net.sustainablepace.chess.domain.aggregate.chessgame.position

import net.sustainablepace.chess.domain.*
import net.sustainablepace.chess.domain.PieceMoveRules.getRulesForPiece
import net.sustainablepace.chess.domain.aggregate.chessgame.Square
import net.sustainablepace.chess.domain.aggregate.chessgame.add
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Black
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.Colour
import net.sustainablepace.chess.domain.aggregate.chessgame.position.piece.White

interface PieceType
interface Pawn : PieceType
interface Knight : PieceType
interface Rook : PieceType
interface Bishop : PieceType
interface Queen : PieceType
interface King : PieceType

fun findMoves(chessGame: ChessGame = ChessGame(), square: Square): Set<ValidMove> {
    val moves = mutableSetOf<ValidMove>()
    chessGame.position.get(square)?.let { pieceToBeMoved ->
        getRulesForPiece(pieceToBeMoved).moveRules.forEach { rule ->
            when (rule.rotations) {
                true -> 4
                false -> 1
            }.let { numRotations ->
                (1..numRotations).map { currentRotation ->
                    moves.addAll(
                        findMovesInDirection(
                            chessGame,
                            square,
                            pieceToBeMoved,
                            rule.direction.rotate(currentRotation % numRotations),
                            rule.canCapture,
                            rule.multiples
                        )
                    )
                }
            }
        }
    }
    return moves
}

fun findMovesInDirection(
    chessGame: ChessGame,
    departureSquare: Square,
    pieceToBeMoved: Piece,
    direction: Direction,
    canCapture: Boolean,
    multiples: Boolean
): Set<ValidMove> {
    val moves = mutableSetOf<ValidMove>()
    when (multiples) {
        true -> (1..7)
        false -> (1..1)
    }.forEach { scale ->
        val arrivalSquare = departureSquare.add(direction * scale) ?: return moves
        val blockingPiece = chessGame.position.get(arrivalSquare)
        if (blockingPiece == null) {
            moves.add(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
        } else if (blockingPiece.colour != pieceToBeMoved.colour && canCapture) {
            moves.add(ValidMove("$departureSquare-$arrivalSquare") as ValidMove)
            return moves
        } else {
            return moves
        }
    }
    return moves
}


sealed class Piece(open val colour: Colour) {
    override fun equals(other: Any?): Boolean =
        other is Piece &&
            colour == other.colour &&
            when (this) {
                is WhitePawn -> other is WhitePawn
                is WhiteKnight -> other is WhiteKnight
                is WhiteRook -> other is WhiteRook
                is WhiteBishop -> other is WhiteBishop
                is WhiteQueen -> other is WhiteQueen
                is WhiteKing -> other is WhiteKing
                is BlackPawn -> other is BlackPawn
                is BlackKnight -> other is BlackKnight
                is BlackRook -> other is BlackRook
                is BlackBishop -> other is BlackBishop
                is BlackQueen -> other is BlackQueen
                is BlackKing -> other is BlackKing
            }

    override fun hashCode(): Int {
        return colour.hashCode() + when (this) {
            is WhitePawn -> 1
            is WhiteKnight -> 2
            is WhiteRook -> 3
            is WhiteBishop -> 4
            is WhiteQueen -> 5
            is WhiteKing -> 6
            is BlackPawn -> 7
            is BlackKnight -> 8
            is BlackRook -> 9
            is BlackBishop -> 10
            is BlackQueen -> 11
            is BlackKing -> 12
        }
    }
}

class WhitePawn : Piece(White), Pawn
class WhiteKnight : Piece(White), Knight
class WhiteRook : Piece(White), Rook
class WhiteBishop : Piece(White), Bishop
class WhiteQueen : Piece(White), Queen
class WhiteKing : Piece(White), King
class BlackPawn : Piece(Black), Pawn
class BlackKnight : Piece(Black), Knight
class BlackRook : Piece(Black), Rook
class BlackBishop : Piece(Black), Bishop
class BlackQueen : Piece(Black), Queen
class BlackKing : Piece(Black), King