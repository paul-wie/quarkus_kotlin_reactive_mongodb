package org.myecommerce.mutiny

import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.ws.rs.ServiceUnavailableException
import java.time.Duration

const val connectionTimeout = 3000L
fun <T> Multi<T>.failOnTimeout(): Multi<T> {
    return this.ifNoItem().after(Duration.ofMillis(connectionTimeout)).failWith(ServiceUnavailableException())
}
fun <T> Uni<T>.failOnTimeout(): Uni<T> {
    return this.ifNoItem().after(Duration.ofMillis(connectionTimeout)).failWith(ServiceUnavailableException())
}
