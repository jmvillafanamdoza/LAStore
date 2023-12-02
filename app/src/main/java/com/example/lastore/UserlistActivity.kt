package com.example.lastore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserlistActivity: AppCompatActivity() {

    private lateinit var databaseReference :DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private var userArraylist: ArrayList<UserData> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlist)
        userRecyclerview = findViewById(R.id.userList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)


        // Set the adapter with the initially empty list
        userRecyclerview.adapter = UserAdapter(userArraylist)

        getUserData()
    }

    private fun getUserData() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseReference.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userArraylist.clear()

                    for (userDataSnapshot in snapshot.children) {
                        Log.d("FirebaseKey", "Key: ${userDataSnapshot.key}")
                        Log.d("FirebaseValue", "Value: ${userDataSnapshot.getValue()}")
                        // Only add data if it can be parsed as UserData
                        try {
                            val user = userDataSnapshot.getValue(UserData::class.java)
                            user?.let { userArraylist.add(it) }
                        } catch (e: DatabaseException) {
                            // Handle the exception or log it
                            Log.e("FirebaseError", "Error parsing data: ${e.message}")
                        }
                    }

                    userRecyclerview.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                // TODO: Implement error handling
            }
        })

    }

}