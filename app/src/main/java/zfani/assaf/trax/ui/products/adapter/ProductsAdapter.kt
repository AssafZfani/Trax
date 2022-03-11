package zfani.assaf.trax.ui.products.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import zfani.assaf.trax.data.local.entity.product.Product
import zfani.assaf.trax.databinding.ItemListProductBinding

class ProductsAdapter(
    var onItemSelectedListener: ((product: Product) -> Unit)? = null,
    var onItemCheckedListener: ((product: Product) -> Unit)? = null
) :
    ListAdapter<Product, ProductsAdapter.ProductViewHolder>(ProductDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LayoutInflater.from(parent.context)
            .let { ItemListProductBinding.inflate(it, parent, false) }
            .let { ProductViewHolder(it) }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) = with(binding) {
            productNameTv.text = item.name
            productCategoryTv.text = item.category
            Glide.with(root.context).load(item.image?.toByteArray()).into(productImageIv)
            root.setOnClickListener {
                onItemSelectedListener?.invoke(item)
            }
            deleteCb.isChecked = item.isChecked
            deleteCb.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                onItemCheckedListener?.invoke(item)
                notifyItemChanged(adapterPosition)
            }
        }
    }
}

class ProductDiffUtils : DiffUtil.ItemCallback<Product>() {

    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Product, newItem: Product) =
        oldItem.name == newItem.name
}