package com.example.recipeapps

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeActivity : AppCompatActivity() {

    lateinit var database : DatabaseReference
    lateinit var ImageUri : Uri
    lateinit var imagelink : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img_recipe.setImageURI(it)
                ImageUri = it
            }
        )

        val types = resources.getStringArray(R.array.Types)

        if (spinner_add_recipe_type != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            spinner_add_recipe_type.adapter = adapter

            spinner_add_recipe_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }



        btn_upload_recipe_image.setOnClickListener {
            getImage.launch("image/*")
        }

        btn_add_recipe.setOnClickListener {
            saverecipe()
        }
    }





    private fun saverecipe() {


        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val imagename = formatter.format(now)
        val recipeimageref = FirebaseStorage.getInstance().getReference("image/$imagename")
        val progressdialog = ProgressDialog(this)

        progressdialog.setTitle("Uploading")
        progressdialog.show()

        recipeimageref.putFile(ImageUri)
            .addOnSuccessListener {
                recipeimageref.downloadUrl.addOnSuccessListener {
                    imagelink = it.toString()

                    val recipename = et_recipe_name.text.toString()
                    val ingredients = et_recipe_ingredients.text.toString()
                    val steps = et_recipe_steps.text.toString()
                    val imageurl = imagelink
                    val recipetypes = spinner_add_recipe_type.selectedItem.toString()

                    database = FirebaseDatabase.getInstance().getReference("recipe")
                    val recipe = Recipe(recipename, ingredients, steps, imageurl, recipetypes)
                    database.child(recipename).setValue(recipe).addOnSuccessListener {
                        et_recipe_name.text?.clear()
                        et_recipe_ingredients.text?.clear()
                        et_recipe_steps.text?.clear()
                        Toast.makeText(this, "Successfully added recipe", Toast.LENGTH_SHORT).show()
                        val intent =
                                Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to add recipe", Toast.LENGTH_SHORT).show()
                    }

                }
                progressdialog.dismiss()
            }.addOnFailureListener {
                progressdialog.dismiss()
                //Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show()
            }









    }



}