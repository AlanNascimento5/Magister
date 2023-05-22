package com.example.magister


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magister.ConversasActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.example.magister.R
import com.example.magister.FormCadastro.Usuarios
import com.squareup.picasso.Picasso

class NovaMensagemActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_mensagem)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Select User"

        recyclerView = findViewById(R.id.newmessage)
        adapter = GroupAdapter()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchUsers()
    }

    companion object {
        const val USER_KEY = "USER_KEY"
    }

    private fun fetchUsers() {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("Usuarios")

        usersCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val user = document.toObject(Usuarios::class.java)
                    adapter.add(UserItem(user))
                }
                Log.d("NewMessage", "Users fetched successfully. Count: ${result.size()}")
            }
            .addOnFailureListener { exception ->
                Log.d("NewMessage", "Error getting users: ${exception.message}")
            }

        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserItem
            val intent = Intent(view.context, ConversasActivity::class.java)
            intent.putExtra(USER_KEY, userItem.user.nome)
            startActivity(intent)
            finish()
        }
    }

    inner class UserItem(val user: Usuarios) : Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.findViewById<TextView>(R.id.username_textview_new_message).text = user.nome

            //val imageView = viewHolder.itemView.findViewById<ImageView>(R.id.imageView)
            //Picasso.get().load(user.profileImageUrl).into(imageView)
        }

        override fun getLayout(): Int {
            return R.layout.user_row_new_message
        }
    }
}