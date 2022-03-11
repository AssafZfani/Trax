package zfani.assaf.trax.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import zfani.assaf.trax.data.local.db.dao.ProductDao
import zfani.assaf.trax.data.repository.product.ProductRepository
import zfani.assaf.trax.data.repository.product.ProductRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideProductRepository(productDao: ProductDao): ProductRepository =
        ProductRepositoryImpl(productDao)
}