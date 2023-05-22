package com.example.magister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.magister.NovaMensagemActivity.Companion.USER_KEY
import com.example.magister.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ConversasActivity : AppCompatActivity() {

    companion object {
        const val TAG = "ChatLog"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GroupAdapter<GroupieViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversas)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val userID = intent.getStringExtra("USER_ID")

        // Obtém o nome do usuário da Intent (se disponível) e define como título da ActionBar
        val username = intent.getStringExtra("USERNAME")
        supportActionBar?.title = username

        setupDummyData()

        val sendButton = findViewById<Button>(R.id.send_button_chat_log)
        sendButton.setOnClickListener {
            Log.d(TAG, "Tentativa de envio de mensagem...")
            performSendMessage()
        }
    }

    private fun performSendMessage() {
        val editTextChatLog = findViewById<EditText>(R.id.edittext_chat_log)
        val text = editTextChatLog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid



        val userID = intent.getStringExtra("USER_KEY")
        Log.d(TAG, "Id do user toID ${userID}")
        val toId = userID

        if (fromId == null) return

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("Mensagens")

        val chatMessage = ChatMessage(text, fromId, toId!!, System.currentTimeMillis() / 1000)
        collectionRef.add(chatMessage)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "A mensagem foi salva: ${documentReference.id}")
                Log.d(TAG, "Id do usuário que recebe a mensagem:  ${toId}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro ao salvar a mensagem no banco de dados", e)
            }
    }

    private fun setupDummyData() {
        recyclerView = findViewById(R.id.recyclerview_chat_log)
        adapter = GroupAdapter()

        recyclerView.adapter = adapter

        adapter.add(ChatFromItem("FROM NEW MESSAGE NIGGA"))
        adapter.add(ChatToItem("TOMARA QUE FUNCIONE, FALA TU"))
        adapter.add(ChatFromItem("SE PA QUE VAI FUNCIONAR, TA DE BOA"))
        adapter.add(ChatToItem("ALELUIA"))
    }
}

data class ChatMessage(val text: String, val fromId: String, val toId: String, val timestamp: Long)

class ChatFromItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_from_row).text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.textview_to_row).text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}