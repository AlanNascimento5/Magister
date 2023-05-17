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
        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid // Obtém o valor do Firebase

        db.collection("Usuarios").document(usuarioID)
            .addSnapshotListener(EventListener { value, error ->
                if (value != null) {
                    //Recuperando dados do tipo Text e EditText
                    binding.textNomeUsuario.setText(value.getString("nome"))
                    binding.textEmailUsuario.setText(email)
                    binding.editEscola.setText(value.getString("escola"))

                    //Recuperando dados do tipo Spinner
                    val spinner1 = binding.spinner1
                    val spinner2 = binding.spinner2
                    val spinner3 = binding.spinner3
                    val valueFromFirebase1 = value.getString("materia 1")
                    val valueFromFirebase2 = value.getString("materia 2")
                    val valueFromFirebase3 = value.getString("materia 3")
                    val adapter1 = spinner1.adapter
                    val adapter2 = spinner2.adapter
                    val adapter3 = spinner3.adapter

                    for (i in 0 until adapter1.count) { //Recuperando do primeiro Spinner
                        val item = adapter1.getItem(i).toString()
                        if (item == valueFromFirebase1) {
                            spinner1.setSelection(i)
                            break
                        }
                    }

                    for (i in 0 until adapter2.count) { // Recuperando do segundo Spinner
                        val item = adapter2.getItem(i).toString()
                        if (item == valueFromFirebase2) {
                            spinner2.setSelection(i)
                            break
                        }
                    }

                    for (i in 0 until adapter3.count) { // Recuperando do terceiro Spinner
                        val item = adapter3.getItem(i).toString()
                        if (item == valueFromFirebase3) {
                            spinner3.setSelection(i)
                            break
                        }
                    }
                }
            })
    }

}