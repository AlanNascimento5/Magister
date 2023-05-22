package com.example.magister

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.widget.DrawableUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.magister.databinding.ActivityFormCadastroBinding
import com.example.magister.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.ActionCodeUrl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding // isso aqui é uma biblioteca de facilitar o instaciamento dos objetos
    private val auth =
        FirebaseAuth.getInstance() //variavel global para facilitar o codigo la embaixo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() // isso aqui só serve para tirar a barra feia que fica em cima do app

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

        binding.btCadastrar.setOnClickListener { view ->
            val senha = binding.editSenha1.text.toString() //Declaração de váriaveis
            val confirmasenha = binding.editConfirmarSenha.text.toString()
            val email = binding.editEmail1.text.toString()
            val nome = binding.editNome.text.toString()


            if (senha != confirmasenha) {
                binding.editConfirmarSenha.error =
                    "A senha e a confimação de senha devem ser iguais!"
            }
            else if (nome.isEmpty()) {
                binding.editNome.error = "Insira o nome!"
            }
            else if (email.isEmpty()) {
                binding.editEmail1.error = "Insira o email!"
            }
            else if (senha.isEmpty()) {
                binding.editSenha1.error = "Insira a senha!"
            }
            else if (confirmasenha.isEmpty()) {
                binding.editConfirmarSenha.error = "Confirme a senha!"
            }
            else if (nome.isNotEmpty() && email.isNotEmpty() && senha.isNotEmpty()
                && confirmasenha.isNotEmpty()
            ) {
                auth.createUserWithEmailAndPassword(
                    email,
                    senha
                ) // aqui é onde é feito o cadastro do usuario
                    .addOnCompleteListener { cadastro ->
                        if (cadastro.isSuccessful) {
                            SalvarDadosUsuario() // chamando a função que está lá embaixo
                            val snackbar =
                                Snackbar.make(         // isso aqui é para mostrar um avisinho embaixo na tela
                                    view,
                                    "Cadastro realizado com sucesso, faça o login!",
                                    Snackbar.LENGTH_SHORT
                                )
                            snackbar.setBackgroundTint(Color.parseColor("#562D8B"))
                            snackbar.setTextColor(Color.WHITE)
                            snackbar.show()
                            binding.editNome.setText("")// isso aqui faz com que todos os campos se esvaziem dps de cada cadastro
                            binding.editEmail1.setText("")
                            binding.editSenha1.setText("")
                            binding.editConfirmarSenha.setText("")
                            binding.editEscola.setText("")
                            binding.spinner1.tag = "Selecione..."
                            binding.spinner2.tag = "Selecione..."
                            binding.spinner3.tag = "Selecione..."
                            IrParaTelaLogin()//chamando a função para voltar para MainActivity depois de cadastrar
                        }
                    }.addOnFailureListener { exception ->
                        //Aqui são só as exceptions. O que acontesse se caso cada coisinha não saia como esperado, ele imprime uma mensagem que está em baixo
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


    private fun SalvarDadosUsuario() { //Essa é uma funçãozinha para os id e salvar o nome dos usuarios no firebase
        val nome = binding.editNome.text.toString()
        val escola = binding.editEscola.text.toString()
        val materia1 = binding.spinner1.toString()
        val materia2 = binding.spinner1.toString()
        val materia3 = binding.spinner1.toString()

        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf<String, Any?>()
        usuarios.put("nome", nome)
        usuarios.put("materia 1", materia1)
        usuarios.put("materia 2", materia2)
        usuarios.put("materia 3", materia3)
        usuarios.put("escola", escola)

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

    private fun IrParaTelaLogin() {//Função para mudar para o MainActivity quando é chamada
        val TelaLogin = Intent(this, ActivityFormLoginBinding::class.java)
        startActivity(TelaLogin)
    }

    class Usuarios(val uid: String, val nome: String, val profileImageUrl: String) {
        constructor() : this(uid = "", nome = "", profileImageUrl = "")
    }

}

