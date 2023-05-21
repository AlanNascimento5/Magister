package com.example.magister

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        //aqui é só a splash screen a tela de inicialização que passa rapidamente e dps vai para tela inicial
        Handler().postDelayed({
            IrParaMainActivity()
        }, 1000)
    }

    private fun IrParaMainActivity(){
        val TelaLogin = Intent(this, FormLoginActivity::class.java)
        startActivity(TelaLogin)
    }
}