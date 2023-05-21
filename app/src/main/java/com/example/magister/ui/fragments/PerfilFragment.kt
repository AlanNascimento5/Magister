package com.example.magister.ui.fragments

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.magister.FormLoginActivity
import com.example.magister.MainActivity
import com.example.magister.databinding.ActivityPerfilBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilFragment : Fragment() {
    private lateinit var binding: ActivityPerfilBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ActivityPerfilBinding.inflate(inflater, container, false)
        val view = binding.root

        val spinner1 = binding.spinner1
        val spinner2 = binding.spinner2
        val spinner3 = binding.spinner3

        spinner1.adapter = ArrayAdapter(
            requireContext(),
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
            requireContext(),
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
            requireContext(),
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

        binding.btSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val telaLogin = Intent(requireContext(), FormLoginActivity::class.java)
            startActivity(telaLogin)
            requireActivity().finish()
        }

        binding.btSalvar.setOnClickListener {
            //Esse aqui é um metodo para esconder o teclado quando o botão for apertado
            val InputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            InputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            //Apartir daqui começa os códigos para atualizar os dados
            val escola = binding.editEscola.text.toString()
            val materia1 = binding.spinner1.selectedItem as String
            val materia2 = binding.spinner2.selectedItem as String
            val materia3 = binding.spinner3.selectedItem as String

            val usuarios = hashMapOf<String, Any?>()
            usuarios.put("escola", escola)
            usuarios.put("materia 1", materia1)
            usuarios.put("materia 2", materia2)
            usuarios.put("materia 3", materia3)

            val usuarioID = FirebaseAuth.getInstance().currentUser!!.uid

            db.collection("Usuarios").document(usuarioID)
                .update(usuarios).addOnCompleteListener {
                    Log.d("db_update", "Sucesso ao atualizar os dados")
                    val snackbar = Snackbar.make(view, "Dados atulizados com sucesso!", Snackbar.LENGTH_LONG)
                    snackbar.setBackgroundTint(Color.parseColor("#562D8B"))
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        val email = FirebaseAuth.getInstance().currentUser?.email
        val usuarioID = FirebaseAuth.getInstance().currentUser?.uid

        if (usuarioID != null) {
            db.collection("Usuarios").document(usuarioID)
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        binding.textNomeUsuario.setText(value.getString("nome"))
                        binding.textEmailUsuario.setText(email)
                        binding.editEscola.setText(value.getString("escola"))

                        val spinner1 = binding.spinner1
                        val spinner2 = binding.spinner2
                        val spinner3 = binding.spinner3

                        val valueFromFirebase1 = value.getString("materia 1")
                        val valueFromFirebase2 = value.getString("materia 2")
                        val valueFromFirebase3 = value.getString("materia 3")

                        val adapter1 = spinner1.adapter
                        val adapter2 = spinner2.adapter
                        val adapter3 = spinner3.adapter

                        for (i in 0 until adapter1.count) {
                            val item = adapter1.getItem(i).toString()
                            if (item == valueFromFirebase1) {
                                spinner1.setSelection(i)
                                break
                            }
                        }

                        for (i in 0 until adapter2.count) {
                            val item = adapter2.getItem(i).toString()
                            if (item == valueFromFirebase2) {
                                spinner2.setSelection(i)
                                break
                            }
                        }

                        for (i in 0 until adapter3.count) {
                            val item = adapter3.getItem(i).toString()
                            if (item == valueFromFirebase3) {
                                spinner3.setSelection(i)
                                break
                            }
                        }
                    }
                }
        }
    }
}
