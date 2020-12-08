package net.sustainablepace.chess.application

import net.sustainablepace.chess.application.port.`in`.Command

interface ApplicationService<in T : Command, out U : Any?> {
    fun process(intent: T): U
}