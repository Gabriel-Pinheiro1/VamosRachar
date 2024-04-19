package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtPessoas: EditText
    private lateinit var edtConta: EditText
    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtPessoas = findViewById<EditText>(R.id.edtPessoas)
        edtPessoas.addTextChangedListener(this)
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtConta.addTextChangedListener(this)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    fun calcularValorFinal(): Double {
        val valorTotal = edtConta.text.toString().toDouble()
        val numPessoas = edtPessoas.text.toString().toInt()
        return calcularConta(valorTotal, numPessoas)
    }
    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        if(edtConta.text.isNotEmpty() && edtPessoas.text.isNotEmpty()){
            val valorFinal = calcularValorFinal()
            val valorFormatado = String.format("%.2f", valorFinal)
            val textValorFinal: TextView = findViewById(R.id.valorFinal)
            textValorFinal.text =  "R$:" + valorFormatado

            //compartilharValor(valorFinal)
        }
    }

    fun calcularConta(valorTotal: Double, numPessoas: Int): Double{
        return valorTotal / numPessoas
    }

    fun compartilharValor(view: View) {

        val valorFinal = calcularValorFinal()
        val valorFormatado = String.format("%.2f", valorFinal)

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "O valor final por pessoa é $valorFormatado")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    fun clickFalar(v: View){
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            val valorFinal = calcularValorFinal()
            val valorFormatado = String.format("%.2f", valorFinal)
            val textoFalado = "O valor final a ser pago é de $valorFormatado reais"
            tts.speak(textoFalado, TextToSpeech.QUEUE_FLUSH, null,null)


        }




    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

