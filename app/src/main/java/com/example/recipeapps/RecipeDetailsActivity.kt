package com.example.recipeapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_recipe_details.*
import kotlinx.android.synthetic.main.card_layout.view.*

class RecipeDetailsActivity : AppCompatActivity() {

    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)

        val intent = intent

        val rcvrecipename = intent.getStringExtra("recipenamekey").toString()
        val rcvrecipeing = intent.getStringExtra("recipeingredientskey").toString()
        val rcvrecipeimg = intent.getStringExtra("recipeimgkey").toString()
        val rcvrecipesteps = intent.getStringExtra("recipestepskey").toString()

        details_tv_recipename.text = rcvrecipename
        details_tv_recipeingredients.text = rcvrecipeing
        details_tv_recipesteps.text = rcvrecipesteps
        Glide.with(this).load(rcvrecipeimg).into(details_iv_recipeimg)

        btn_update.setOnClickListener {
           val intent = Intent(this@RecipeDetailsActivity, UpdateRecipeActivity::class.java)
            intent.putExtra("updaterecipename", rcvrecipename)
            intent.putExtra("updaterecipeingredients", rcvrecipeing)
            intent.putExtra("updaterecipesteps", rcvrecipesteps)

            startActivity(intent)
            finish()



        }
        btn_delete.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("recipe")

            database.child(rcvrecipename).removeValue().addOnSuccessListener {

                val intent = Intent(this@RecipeDetailsActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }


        }

    }
}