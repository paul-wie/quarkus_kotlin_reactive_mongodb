package org.myecommerce.endpoints

import com.mongodb.client.model.Filters.eq
import io.quarkus.mongodb.reactive.ReactiveMongoClient
import io.quarkus.mongodb.reactive.ReactiveMongoCollection
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.inject.Inject
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.bson.types.ObjectId
import org.jboss.resteasy.reactive.RestPath
import org.myecommerce.exception.CustomException
import org.myecommerce.exception.DeserializationException
import org.myecommerce.exception.NotFoundException
import org.myecommerce.model.Product
import org.myecommerce.mutiny.failOnTimeout

@Path("/products")
class Products {

    @Inject
    lateinit var mongoClient: ReactiveMongoClient


    fun getCollection(): ReactiveMongoCollection<Product>{
        return mongoClient.
            getDatabase("products_orders").
                getCollection("products", Product::class.java)
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createProduct(product: Product): Uni<Product> =
        getCollection().insertOne(product)
            // Handle DB unavailable
            .failOnTimeout()
            .onItem().transformToUni {
            result -> getCollection().find(eq("_id", result.insertedId)).collect().first()
        }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getProducts(): Multi<Product> {
        return getCollection().find().
        // Handle DB unavailable
        failOnTimeout()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Throws(CustomException::class)
    fun getProductById(id: String): Uni<Product> = runCatching {
        // Check serialization
        ObjectId(id)
    }.fold(
        onSuccess = {objectId ->
            getCollection().find(eq("_id", objectId)).
                // Handle DB unavailable
            failOnTimeout().
            collect().first().onItem().
            ifNull().failWith(NotFoundException("Not found product with productId=$id"))
        },
        onFailure = {
            Uni.createFrom().failure(DeserializationException("Unable to deserialize productId=$id"))
        }
    )

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    fun getProduct(@RestPath id: String): Uni<Void> =
        runCatching { ObjectId(id) }.fold(
            onSuccess = {objectId ->
                getCollection().deleteOne(eq("_id", ObjectId(id))).
                // Handle DB unavailable
                failOnTimeout()
                .onItem().transformToUni { _ ->
                    Uni.createFrom().voidItem()
                }
            },
            onFailure = { Uni.createFrom().failure(DeserializationException("Unable to deserialize productId=$id")) }
        )
}

