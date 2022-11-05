package com.phanith.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(),UserAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var data: ArrayList<User>
    private lateinit var adapter: UserAdapter

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        data = ArrayList()
        adapter = UserAdapter(data,this)

        dbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                // Clear previous data to avoid adding the same data
                data.clear()
                //Adding user data to data list
                for( postSnapshot: DataSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (currentUser != null) {
                        data.add(currentUser)

                        currentUser.name?.let { Log.i("user", it) }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })



        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        //layout manager are responsible for position item into the recycle view
        recyclerView.layoutManager = LinearLayoutManager(this)
        // add some optimization
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter


    }

    override fun OnItemClick(position: Int) {
        val currentUser = data[position]
        val intent = Intent(this@MainActivity,IndividualChat::class.java).apply {
           //pass name of the user when onclick so we can display it on menu
            putExtra("name", currentUser.name)
            putExtra("uid",currentUser.uid )
        }

        startActivity(intent)
    }

    // inflate the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    //menu on item click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()
            val intent = Intent(this@MainActivity,LogIn::class.java)
            startActivity(intent)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}