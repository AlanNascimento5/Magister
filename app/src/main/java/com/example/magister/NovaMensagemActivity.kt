package com.example.magister

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.firestore.auth.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.example.magister.R
import com.example.magister.FormCadastro.User
import com.squareup.picasso.Picasso

class NovaMensagemActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_mensagem)

        supportActionBar?.title = "Select User"

        recyclerView = findViewById(R.id.newmessage)
        adapter = GroupAdapter()

        recyclerView.adapter = adapter

        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                //adapter.notifyDataSetChanged()

                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ConversasActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user.username)
                    //intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("NewMessage", databaseError.message)
            }
        })
    }

    inner class UserItem(val user: FormCadastro.User) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.username_textview_new_message).text = user.username

            Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.imageView))
        }

        override fun getLayout(): Int {
            return R.layout.user_row_new_message
        }
    }
}