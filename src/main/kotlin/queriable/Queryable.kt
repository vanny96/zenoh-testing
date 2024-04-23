package queriable

import getZenohConfigFile
import io.zenoh.Session
import io.zenoh.keyexpr.intoKeyExpr
import kotlinx.coroutines.runBlocking

fun main() {
    Session.open(getZenohConfigFile())
        .mapCatching { session -> createTestQueryable(session) }
        .onFailure { println("Failed: ${it.message}") }
}

private fun createTestQueryable(session: Session) {
    val key = "test/counter/*".intoKeyExpr().getOrThrow()
    val queryable = session.declareQueryable(key).res().getOrThrow()

    runBlocking {
        for (query in queryable.receiver!!) {
            println("Received ${query.value} from ${query.keyExpr}, returning double")
            val value = query.value.toString().toInt() * 2
            query.reply(query.keyExpr).success("Doubled value is: $value").res()
        }
    }
}