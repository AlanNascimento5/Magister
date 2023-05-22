package com.example.magister



import android.widget.Button
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.database.*
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

class ConversasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    //private lateinit var database: DatabaseReference
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversas)

        val username = intent.getStringExtra(NovaMensagemActivity.USER_KEY)

        supportActionBar?.title = username

        setupDummyData()

    }
    private fun setupDummyData() {

        recyclerView = findViewById(R.id.recyclerview_chat_log)
        adapter = GroupAdapter()

        recyclerView.adapter = adapter

        //adapter.add(ChatFromItem())
        //adapter.add(ChatToItem())
        //adapter.add(ChatFromItem())
        //adapter.add(ChatToItem())

    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_from_row).text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_to_row).text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

