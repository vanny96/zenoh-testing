package queriable

import getZenohConfigFile
import io.zenoh.Session
import io.zenoh.keyexpr.KeyExpr
import io.zenoh.keyexpr.intoKeyExpr
import io.zenoh.query.Reply
import kotlinx.coroutines.runBlocking
import java.util.UUID

fun main() {
    Session.open(getZenohConfigFile())
        .mapCatching { session -> createTestCounterGetter(session) }
        .onFailure { println("Failed: ${it.message}") }
}

private fun createTestCounterGetter(session: Session) {
    val uuid = UUID.randomUUID()

    val key = "test/counter/$uuid".intoKeyExpr().getOrThrow()
    sendPeriodicMessage(session, key)
}


private fun sendPeriodicMessage(session: Session, key: KeyExpr) {
    var counter = 1
    while (true) {
        println("Sending message nr: $counter")
        val receiver = session.get(key).withValue(counter.toString()).res().getOrThrow()

        runBlocking {
            for (reply in receiver!!) {
                when (reply) {
                    is Reply.Success -> {
                        println("Reply had value: ${reply.sample.value}")
                    }

                    is Reply.Error -> {
                        println("Error had value: ${reply.error}")
                    }
                }

            }
        }

        counter++
        Thread.sleep(2000)
    }
}