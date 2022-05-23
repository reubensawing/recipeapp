package com.example.recipeapps

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerAdapter(private val recipelist: ArrayList<Recipe>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val currentlist = recipelist[position]


        holder.displayrecipename.text = currentlist.recipename

       Glide.with(holder.itemView).load(recipelist.get(position).imageurl).into(holder.itemView.display_img)

        holder.itemView.setOnClickListener {
            val passrecipename =  recipelist[position].recipename
            val passrecipeimg = recipelist[position].imageurl
            val passrecipeingredients = recipelist[position].ingredients
            val passrecipesteps = recipelist[position].steps

            val activity = holder.itemView.context as Activity

            val intent = Intent(activity, RecipeDetailsActivity::class.java)
            intent.putExtra("recipenamekey", passrecipename)
            intent.putExtra("recipeingredientskey", passrecipeingredients)
            intent.putExtra("recipeimgkey", passrecipeimg)
            intent.putExtra("recipestepskey", passrecipesteps)

            activity.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return recipelist.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var displayimage: ImageView
        var displayrecipename: TextView

        init {
            displayimage = itemView.display_img
            displayrecipename = itemView.display_recipename
        }
    }
}