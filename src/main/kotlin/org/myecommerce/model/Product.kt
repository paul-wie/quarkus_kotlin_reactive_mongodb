package org.myecommerce.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.myecommerce.serializer.ObjectIdSerializer

@Serializable
class Product{

    constructor()

    constructor(id: ObjectId, name: String, description: String, price: String){
        this.id = id
        this.name = name
        this.description = description
        this.price = price
    }

    @Serializable(with = ObjectIdSerializer::class)
    var id: ObjectId? = null
    lateinit var name: String
    lateinit var description: String
    lateinit var price: String
}

