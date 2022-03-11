package zfani.assaf.trax.ui.products

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import zfani.assaf.trax.R
import zfani.assaf.trax.data.local.entity.product.Product
import zfani.assaf.trax.databinding.FragmentProductListBinding
import zfani.assaf.trax.ui.products.adapter.ProductsAdapter
import zfani.assaf.trax.utils.hideLoading
import zfani.assaf.trax.utils.showLoading
import zfani.assaf.trax.utils.viewBinding

@AndroidEntryPoint
class ProductListFragment : Fragment(R.layout.fragment_product_list) {

    private val binding by viewBinding(FragmentProductListBinding::bind)
    private val viewModel: ProductListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        subscribeUI()
    }

    private fun initUI() = with(binding) {
        showLoading()
        addProductFab.setOnClickListener {
            moveToProductScreen()
        }
        deleteProductsTv.setOnClickListener {
            viewModel.deleteCheckedProducts()
        }
    }

    private fun subscribeUI() {
        viewModel.productsLiveData.observe(viewLifecycleOwner) { productList ->
            hideLoading()
            binding.productsRv.adapter = ProductsAdapter({
                moveToProductScreen(it)
            }, {
                viewModel.updateProduct(it)
            }).apply {
                submitList(productList)
            }
            setDeleteProductsState(productList)
        }
    }

    private fun moveToProductScreen(product: Product? = null) {
        findNavController().navigate(
            ProductListFragmentDirections.actionProductListFragmentToProductFragment(product)
        )
    }

    private fun setDeleteProductsState(productList: List<Product>) {
        binding.deleteProductsTv.isVisible = productList.any { it.isChecked }
    }
}