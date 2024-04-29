package dev.itchybit.thunderbuddy.ui.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.itchybit.thunderbuddy.databinding.ItemFavouriteBinding
import dev.itchybit.thunderbuddy.io.db.entity.Favourite

class FavouriteAdapter(private val onDelete: (Favourite) -> Unit) :
    ListAdapter<Favourite, FavouriteAdapter.ViewHolder>(FavouriteDiffCallback) {

    inner class ViewHolder(private val binding: ItemFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favourite: Favourite) = with(binding) {
            title.text = favourite.name
            delete.setOnClickListener { onDelete(favourite) }
        }
    }

    object FavouriteDiffCallback : DiffUtil.ItemCallback<Favourite>() {
        override fun areItemsTheSame(oldItem: Favourite, newItem: Favourite): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Favourite, newItem: Favourite
        ): Boolean = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}