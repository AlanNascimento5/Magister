package com.example.magister

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.magister.PerfilActivity
import com.example.magister.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val textCadastrar = findViewById<TextView>(R.id.text_tela_cadastro)

        textCadastrar.setOnClickListener {
            IrParaTelaCadastro()
        }
        //essa parte aqui é onde ocorre o login
        binding.btEntrar.setOnClickListener{ view ->

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()
            val progressbar = binding.progressbar

            if (email.isEmpty()) {
                binding.editEmail.error = "Insira o email!"
            }
            else if (senha.isEmpty()) {
                binding.editSenha.error = "Insira a senha!"
            }else{
                auth.signInWithEmailAndPassword(email,senha).addOnCompleteListener{ autenticacao ->
                    if (autenticacao.isSuccessful){
                        progressbar.isVisible = true

                        Handler().postDelayed({
                            IrParaMainActivity()
                        }, 2000)
                    }
                    //aqui é as exceptions do login
                }.addOnFailureListener { exception ->
                    val snackbar = Snackbar.make(view, "Não foi possível efetuar o login!", Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.parseColor("#003400"))
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }
            }
        }
    }

    //função para ir pra tela de Cadastro
    private fun IrParaTelaCadastro() {
        val TelaCadastro = Intent(this, FormCadastro::class.java)
        startActivity(TelaCadastro)
    }
    //função para ir para tela principal (feed)
    private fun IrParaMainActivity() {
        val TelaPerfil = Intent(this, PerfilActivity::class.java)
        startActivity(TelaPerfil)
    }

    //aqui é uma configuraçãozinha para quando sair do app e voltar o usuario continue logado
    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            IrParaMainActivity()
        }
    }
}