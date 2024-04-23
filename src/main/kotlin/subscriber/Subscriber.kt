package subscriber

import getZenohConfigFile
import io.zenoh.Session
import io.zenoh.keyexpr.intoKeyExpr
import kotlinx.coroutines.runBlocking

fun main() {
    Session.open(getZenohConfigFile())
        .mapCatching { session -> createTestSubscriber(session) }
        .onFailure { println("Failed: ${it.message}") }
}

private fun createTestSubscriber(session: Session) {
    val key = "test/counter/*".intoKeyExpr().getOrThrow()
    val subscriber = session.declareSubscriber(key).res().getOrThrow()

    runBlocking {
        for (message in subscriber.receiver!!) {
            println("Received ${message.value} of kind ${message.kind} from ${message.keyExpr}")
        }
    }
}