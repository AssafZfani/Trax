package zfani.assaf.trax.data.repository.product

import zfani.assaf.trax.data.events.product.ProductEvent
import zfani.assaf.trax.data.local.db.dao.ProductDao
import zfani.assaf.trax.data.local.entity.product.Product
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts() = productDao.getAllProducts()

    override suspend fun insertProduct(product: Product): ProductEvent {
        productDao.insertProduct(product)
        return ProductEvent.ProductInserted
    }

    override suspend fun updateProduct(product: Product): ProductEvent {
        productDao.updateProduct(product)
        return ProductEvent.ProductUpdated
    }

    override suspend fun deleteProduct(product: Product): ProductEvent {
        productDao.deleteProduct(product)
        return ProductEvent.ProductDeleted
    }

    override suspend fun deleteCheckedProducts() {
        productDao.deleteCheckedProducts()
    }
}
