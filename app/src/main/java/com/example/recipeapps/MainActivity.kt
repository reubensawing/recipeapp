package com.example.recipeapps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_recipe.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recipeRecyclerView : RecyclerView
    val recipeArrayList = arrayListOf<Recipe>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val types = resources.getStringArray(R.array.Types)

        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    if(position != 0) {

                        recipeArrayList.clear()
                        databaseReference = FirebaseDatabase.getInstance().getReference("recipe")

                        val recipefilter = spinner.selectedItem.toString()
                        val filterrecipe = databaseReference.orderByChild("recipetypes").equalTo(recipefilter)

                        filterrecipe.addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    for (recipeSnapshot in snapshot.children){

                                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                                        recipeArrayList.add(recipe!!)
                                    }

                                    recipeRecyclerView.adapter = RecyclerAdapter(recipeArrayList)



                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                            }


                        })
                    }




                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }

        recipeRecyclerView = rv_recipe_list
        recipeRecyclerView.layoutManager = LinearLayoutManager(this)
        recipeRecyclerView.setHasFixedSize(true)


        getRecipeData()

        floatingActionButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddRecipeActivity::class.java))
            finish()
        }


    }

    private fun getRecipeData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("recipe")

        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                        for (recipeSnapshot in snapshot.children){

                        val recipe = recipeSnapshot.getValue(Recipe::class.java)
                        recipeArrayList.add(recipe!!)
                    }

                    recipeRecyclerView.adapter = RecyclerAdapter(recipeArrayList)



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
            }


        })
    }
}