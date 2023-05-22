package com.example.magister.ui.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.magister.NovaMensagemActivity
import com.example.magister.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ConversasFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_ultimas_conversas, container, false)

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), NovaMensagemActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }





        return view


    }
}






