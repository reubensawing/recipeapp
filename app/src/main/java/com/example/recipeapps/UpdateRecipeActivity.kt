package com.example.recipeapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_update_recipe.*

class UpdateRecipeActivity : AppCompatActivity() {

    lateinit var database: DatabaseReference
    lateinit var rcvrecipenameupdate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_recipe)

        val intent = intent

        val rcvrecipenameupdate = intent.getStringExtra("updaterecipename").toString()
        val rcvrecipeingupdate = intent.getStringExtra("updaterecipeingredients").toString()
        val rcvrecipestepsupdate = intent.getStringExtra("updaterecipesteps").toString()

        tv_recipename_update.setText(rcvrecipenameupdate)
        et_recipe_ingredients_update.setText(rcvrecipeingupdate)
        et_recipe_steps_update.setText(rcvrecipestepsupdate)

        val newrecipeing = et_recipe_ingredients_update.text.toString()
        val newrecipesteps = et_recipe_steps_update.text.toString()

        btn_update_recipe.setOnClickListener {




            database = FirebaseDatabase.getInstance().getReference("recipe")

            val updaterecipe = mapOf<String, String>(

                "steps" to newrecipesteps,
                "ingredients" to newrecipeing
            )


                database.child(rcvrecipenameupdate).updateChildren(updaterecipe).addOnSuccessListener {


                    val intent = Intent(this@UpdateRecipeActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()


                }


        }




    }

}
