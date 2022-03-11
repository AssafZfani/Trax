package zfani.assaf.trax.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import zfani.assaf.trax.data.local.entity.product.Product
import zfani.assaf.trax.data.repository.product.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    val productsLiveData = productRepository.getAllProducts().asLiveData()

    fun updateProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        productRepository.updateProduct(product)
    }

    fun deleteCheckedProducts() = viewModelScope.launch(Dispatchers.IO) {
        productRepository.deleteCheckedProducts()
    }
}