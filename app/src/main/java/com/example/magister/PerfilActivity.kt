package com.example.magister

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.magister.FormLoginActivity
import com.example.magister.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore


class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val spinner1 = findViewById<Spinner>(R.id.spinner1) //instaciando os objetos...
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)

        spinner1.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "Selecione...",
                "Estrutura de dados",
                "Programação Web",
                "POO",
                "Banco de dados",
                "Engenharia de Softwares"
            )
        )

        spinner2.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "Selecione...",
                "Estrutura de dados",
                "Programação Web",
                "POO",
                "Banco de dados",
                "Engenharia de Softwares"
            )
        )

        spinner3.adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf(
                "Selecione...",
                "Estrutura de dados",
                "Programação Web",
                "POO",
                "Banco de dados",
                "Engenharia de Softwares"
            )
        )

        //código para deslogar ao clicar em "sair"
        binding.btSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val TelaLogin = Intent(this, FormLoginActivity::class.java)
            startActivity(TelaLogin)
            finish()
        }
    }

    //aqui é um códigozinho para chamar o nome e o email na tela do perfil, assim será automatico de
    // acordo com o nome que o usuario digitou do cadastro
    override fun onStart() {
        super.onStart()

        val email = FirebaseAuth.getInstance().currentUser!!.email
        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("Usuarios").document(usuarioID)
            .addSnapshotListener(EventListener { value, error ->
                if (value != null) {
                    binding.textNomeUsuario.setText(value.getString("nome"))
                    binding.textEmailUsuario.setText(email)
                }
            })
    }

}