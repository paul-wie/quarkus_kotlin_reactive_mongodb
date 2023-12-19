package org.myecommerce.endpoints

import io.smallrye.mutiny.Uni
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.jboss.resteasy.reactive.RestPath
import org.myecommerce.model.Product

@Path("/products")
class Products {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun createProduct(product: Product): Uni<Product> {
        println(product)
        return Uni.createFrom().item(product)
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getProducts(): Uni<List<Product>>{
        return Uni.createFrom().item(mutableListOf(Product(
            id = "123",
            name = "T-Shirt",
            description = "Red T-Shirt, perfect for the summer",
            price = 12.99)))
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getProductById(id: String): Uni<Product>{
        return Uni.createFrom().item(Product(
            id = id,
            name = "T-Shirt",
            description = "Red T-Shirt, perfect for the summer",
            price = 12.99))
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getProduct(@RestPath id: String): Uni<Product> {

        println("Deleted product with id $id")

        return Uni.createFrom().item(
            Product(
                id = "123",
                name = "T-Shirt",
                description = "Red T-Shirt, perfect for the summer",
                price = 12.99
            )
        )
    }
}