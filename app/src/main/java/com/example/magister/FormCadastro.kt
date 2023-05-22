package com.example.magister

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.magister.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val spinner1 = binding.spinner1
        val spinner2 = binding.spinner2
        val spinner3 = binding.spinner3

        spinner1.adapter = ArrayAdapter(
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

        spinner2.adapter = ArrayAdapter(
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

        spinner3.adapter = ArrayAdapter(
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

        binding.btCadastrar.setOnClickListener { view ->
            val senha = binding.editSenha1.text.toString()
            val confirmasenha = binding.editConfirmarSenha.text.toString()
            val email = binding.editEmail1.text.toString()
            val nome = binding.editNome.text.toString()

            if (senha != confirmasenha) {
                binding.editConfirmarSenha.error =
                    "A senha e a confimação de senha devem ser iguais!"
            } else if (nome.isEmpty()) {
                binding.editNome.error = "Insira o nome!"
            } else if (email.isEmpty()) {
                binding.editEmail1.error = "Insira o email!"
            } else if (senha.isEmpty()) {
                binding.editSenha1.error = "Insira a senha!"
            } else if (confirmasenha.isEmpty()) {
                binding.editConfirmarSenha.error = "Confirme a senha!"
            } else if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()
                && confirmasenha.isNotEmpty()
            ) {
                auth.createUserWithEmailAndPassword(
                    email,
                    senha
                ).addOnCompleteListener { cadastro ->
                    if (cadastro.isSuccessful) {
                        SalvarDadosUsuario(nome, spinner1.selectedItem.toString(), spinner2.selectedItem.toString(), spinner3.selectedItem.toString())
                        val snackbar = Snackbar.make(
                            view,
                            "Cadastro realizado com sucesso, faça o login!",
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setBackgroundTint(Color.parseColor("#562D8B"))
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.show()
                        binding.editNome.setText("")
                        binding.editEmail1.setText("")
                        binding.editSenha1.setText("")
                        binding.editConfirmarSenha.setText("")
                        binding.editEscola.setText("")
                        binding.spinner1.setSelection(0)
                        binding.spinner2.setSelection(0)
                        binding.spinner3.setSelection(0)
                        IrParaTelaLogin()
                    }
                }.addOnFailureListener { exception ->
                    val mensagemErro = when (exception) {
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                        is FirebaseAuthInvalidCredentialsException -> "Digite um email válido!"
                        is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                        is FirebaseNetworkException -> "Sem conexão com a internet!"
                        else -> "Erro ao cadastrar usuário!"
                    }
                    val snackbar = Snackbar.make(view, mensagemErro, Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.parseColor("#562D8B"))
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }
            }
        }
    }

    private fun SalvarDadosUsuario(nome: String, materia1: String, materia2: String, materia3: String) {
        val escola = binding.editEscola.text.toString()

        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf<String, Any?>()
        usuarios["nome"] = nome
        usuarios["materia 1"] = materia1
        usuarios["materia 2"] = materia2
        usuarios["materia 3"] = materia3
        usuarios["escola"] = escola

        val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("Usuarios").document(usuarioID).set(usuarios)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("db", "Sucesso ao salvar os dados")
                } else {
                    Log.d("db_error", "Erro ao salvar os dados")
                }
            }
    }

    private fun IrParaTelaLogin() {
        val intent = Intent(this, FormLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}