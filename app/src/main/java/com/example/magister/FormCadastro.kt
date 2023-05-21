package com.example.magister

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.example.magister.databinding.ActivityFormCadastroBinding
import com.example.magister.databinding.ActivityFormLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
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

        val spinner1 = findViewById<Spinner>(R.id.spinner1)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)

        binding.eye1.setOnClickListener { //Código para fazer a senha ficar vísivel
            val editTextPassword = findViewById<EditText>(R.id.edit_senha1)
            val imageButtonShowPassword = findViewById<ImageButton>(R.id.eye1)

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

        binding.eye2.setOnClickListener { //Código para fazer a senha ficar vísivel
            val editTextPassword = findViewById<EditText>(R.id.edit_confirmar_senha)
            val imageButtonShowPassword = findViewById<ImageButton>(R.id.eye2)

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
            //Esse aqui é um metodo para esconder o teclado quando o botão for apertado
            val InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            InputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            val senha = binding.editSenha1.text.toString() //Declaração de váriaveis
            val confirmasenha = binding.editConfirmarSenha.text.toString()
            val email = binding.editEmail1.text.toString()
            val nome = binding.editNome.text.toString()
            val senhaForte = verificarSenhaForte(senha)

            if (senha != confirmasenha) {
                binding.editConfirmarSenha.error =
                    "A senha e a confimação de senha devem ser iguais!"
            }
            else if (senha.length < 8) {
                binding.editConfirmarSenha.error =
                    "A senha deve conter 8 digitos!"
            }
            else if (senhaForte == false) {
                binding.editConfirmarSenha.error =
                    "A senha deve conter: letras maiúsculas, letras minúsculas, símbolos e números!"
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


    private fun SalvarDadosUsuario() { //Essa é uma funçãozinha para salvar os dados dos usuários no firebase
        val nome = binding.editNome.text.toString()
        val escola = binding.editEscola.text.toString()
        val materia1 = binding.spinner1.selectedItem as String
        val materia2 = binding.spinner2.selectedItem as String
        val materia3 = binding.spinner3.selectedItem as String

        val db = FirebaseFirestore.getInstance()

        val usuarios = hashMapOf<String, Any?>()
        usuarios.put("nome", nome)
        usuarios.put("escola", escola)
        usuarios.put("materia 1", materia1)
        usuarios.put("materia 2", materia2)
        usuarios.put("materia 3", materia3)


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

    private fun IrParaTelaLogin() { //Função para mudar para o MainActivity quando é chamada
        val TelaLogin = Intent(this, ActivityFormLoginBinding::class.java)
        startActivity(TelaLogin)
    }

    fun verificarSenhaForte(senha: String): Boolean {
        // Verificar se a senha contém letras maiúsculas, minúsculas, números e símbolos
        var temMaiuscula = false
        var temMinuscula = false
        var temNumero = false
        var temCaracterEspecial = false
        for (c in senha.toCharArray()) {
            if (c.isUpperCase()) {
                temMaiuscula = true
            } else if (c.isLowerCase()) {
                temMinuscula = true
            } else if (c.isDigit()) {
                temNumero = true
            } else if (!c.isLetterOrDigit()) {
                temCaracterEspecial = true
            }
        }

        // Verificar se a senha atende a todos os critérios
        return temMaiuscula && temMinuscula && temNumero && temCaracterEspecial
    }

}
