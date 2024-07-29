package com.bunamiranda.meuslivros

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bunamiranda.meuslivros.databinding.ActivityTelaAddLivroBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class TelaAddLivro : AppCompatActivity() {

    private lateinit var binding: ActivityTelaAddLivroBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTelaAddLivroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firestore = FirebaseFirestore.getInstance()

        binding.btnAdicionarLivro.setOnClickListener {
            saveBookData()
        }
    }

    private fun saveBookData() {
        val titulo = binding.editTitulo.text.toString().trim()
        val genero = binding.editGenero.text.toString().trim()
        val sinopse = binding.editSinipse.text.toString().trim()

        if (titulo.isEmpty() || genero.isEmpty() || sinopse.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val bookData = hashMapOf(
            "titulo" to titulo,
            "genero" to genero,
            "sinopse" to sinopse
        )

        saveBookDataToFirestore(bookData)
    }

    private fun saveBookDataToFirestore(bookData: HashMap<String, String>) {
        firestore.collection("Filme")
            .add(bookData)
            .addOnSuccessListener {
                Toast.makeText(this, "Filme salvo com sucesso.", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar Filme: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}