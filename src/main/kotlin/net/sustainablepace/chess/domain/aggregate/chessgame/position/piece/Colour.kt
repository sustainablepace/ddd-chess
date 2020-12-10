package net.sustainablepace.chess.domain.aggregate.chessgame.position.piece

interface Colour
interface White : Colour
interface Black : Colour

sealed class Side
object WhitePieces: Side(), White
object BlackPieces: Side(), Black
