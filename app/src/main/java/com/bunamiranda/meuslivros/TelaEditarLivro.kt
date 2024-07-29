package com.bunamiranda.meuslivros

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bunamiranda.meuslivros.databinding.ActivityTelaEditarLivroBinding
import com.bunamiranda.meuslivros.model.Livro
import com.google.firebase.firestore.FirebaseFirestore

class TelaEditarLivro : AppCompatActivity() {

    private lateinit var binding: ActivityTelaEditarLivroBinding
    private lateinit var firestore: FirebaseFirestore
    private var livroId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTelaEditarLivroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val livro = intent.getParcelableExtra<Livro>("Filme")
        livro?.let {
            carregarDados(it)
            livroId = it.id
        }

        binding.btnAtualizarFilme.setOnClickListener {
            salvarAlteracoes()
        }
    }

    private fun carregarDados(livro: Livro) {
        binding.editAtualizarTitulo.setText(livro.titulo)
        binding.editGenero.setText(livro.genero)
        binding.editSinipse.setText(livro.sinopse)
    }

    private fun salvarAlteracoes() {
        val titulo = binding.editAtualizarTitulo.text.toString().trim()
        val genero = binding.editGenero.text.toString().trim()
        val sinopse = binding.editSinipse.text.toString().trim()

        if (titulo.isEmpty() || genero.isEmpty() || sinopse.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        livroId?.let {
            val bookData = hashMapOf(
                "titulo" to titulo,
                "genero" to genero,
                "sinopse" to sinopse
            )

            firestore.collection("Filme").document(it)
                .set(bookData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Filme atualizado com sucesso.", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Erro ao atualizar o Filme: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}