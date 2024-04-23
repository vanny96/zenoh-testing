package subscriber

import getZenohConfigFile
import io.zenoh.Session
import io.zenoh.keyexpr.KeyExpr
import io.zenoh.keyexpr.intoKeyExpr
import io.zenoh.publication.Publisher
import java.util.UUID

fun main() {
    Session.open(getZenohConfigFile())
        .mapCatching { session -> createTestCounterPublisher(session) }
        .onFailure { println("Failed: ${it.message}") }
}

private fun createTestCounterPublisher(session: Session) {
    val uuid = UUID.randomUUID()
    val key = "test/counter/$uuid".intoKeyExpr().getOrThrow()

    val publisher = session.declarePublisher(key).res().getOrThrow()
    sendPeriodicMessage(publisher)
}

private fun sendPeriodicMessage(publisher: Publisher) {
    var counter = 1
    while (true) {
        println("Sending message nr: $counter")
        if (counter % 3 == 0) {
            publisher.delete().res()
        } else {
            publisher.put("Message nr: $counter").res()
        }
        counter++
        Thread.sleep(2000)
    }
}