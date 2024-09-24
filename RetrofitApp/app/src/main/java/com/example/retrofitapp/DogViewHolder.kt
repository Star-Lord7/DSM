package com.example.retrofitapp

import android.view. View
import androidx.recyclerview.widget. RecyclerView
import com.example.retrofitapp.databinding. ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view: View?) : RecyclerView. ViewHolder(view !! ) {
    private val itemDogBinding: ItemDogBinding

    init {
        itemDogBinding = com.example.retrofitapp.databinding.ItemDogBinding.bind(view!!)
    }

    fun bind(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(itemDogBinding.ivDog)
    }
}