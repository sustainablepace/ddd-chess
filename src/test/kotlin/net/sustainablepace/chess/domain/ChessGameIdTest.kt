package net.sustainablepace.chess.domain

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ChessGameIdTest {
    @Test
    fun `test serialization`() {
        val game = ChessGame()

        val json = ObjectMapper()
            .registerModule(KotlinModule())
            .valueToTree<JsonNode>(game)

        assertThat(json.get("id")).isInstanceOf(TextNode::class.java)
    }
}