package com.example.magister

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
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

        binding.eye.setOnClickListener {//Código para fazer a senha ficar vísivel
            val editTextPassword = findViewById<EditText>(R.id.edit_senha)
            val imageButtonShowPassword = findViewById<ImageButton>(R.id.eye)

            if (editTextPassword.transformationMethod is PasswordTransformationMethod) {
                // Torna a senha visível
                editTextPassword.transformationMethod = SingleLineTransformationMethod.getInstance()
                imageButtonShowPassword.setImageResource(R.drawable.ic_eyeoff)
            } else {
                // Torna a senha oculta
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                imageButtonShowPassword.setImageResource(R.drawable.ic_eye)
            }

            // Move o cursor para o final do texto
            editTextPassword.setSelection(editTextPassword.text.length)
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
                    snackbar.setBackgroundTint(Color.parseColor("#562D8B"))
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