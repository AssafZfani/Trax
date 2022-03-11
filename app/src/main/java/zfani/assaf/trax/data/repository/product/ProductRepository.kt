package zfani.assaf.trax.data.repository.product

import kotlinx.coroutines.flow.Flow
import zfani.assaf.trax.data.events.product.ProductEvent
import zfani.assaf.trax.data.local.entity.product.Product

interface ProductRepository {

    fun getAllProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product): ProductEvent

    suspend fun updateProduct(product: Product): ProductEvent

    suspend fun deleteProduct(product: Product): ProductEvent

    suspend fun deleteCheckedProducts()
}