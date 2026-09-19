package com.example.application_masse_corporelle

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        // Listener pour le bouton Calculer
        buttonCalculer.setOnClickListener {
            calculerIMC()
        }

        // Listener pour le bouton Effacer
        buttonEffacer.setOnClickListener {
            effacerChamps()
        }
    }

    private fun calculerIMC() {
        val poidsStr = editTextPoids.text.toString()
        val tailleStr = editTextTaille.text.toString()

        // 1. Vérifier que les deux champs sont renseignés
        if (poidsStr.isEmpty() || tailleStr.isEmpty()) {
            Toast.makeText(this, getString(R.string.err_empty), Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Convertir les valeurs en nombres décimaux
        val poids = poidsStr.toDoubleOrNull()
        val taille = tailleStr.toDoubleOrNull()

        // 3. Vérifier que les valeurs sont valides et strictement positives
        if (poids == null || taille == null || poids <= 0 || taille <= 0) {
            Toast.makeText(this, getString(R.string.err_positive), Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Calculer l'IMC
        val imc = poids / (taille * taille)

        // 5. Arrondir et afficher la valeur
        val imcFormate = String.format(Locale.getDefault(), "%.2f", imc)
        textViewImc.text = getString(R.string.result_imc, imcFormate)

        // 6. Déterminer la catégorie et la couleur
        val (categorie, couleur) = determinerCategorie(imc)
        
        textViewCategorie.text = getString(R.string.result_categorie, categorie)
        textViewCategorie.setTextColor(couleur)
    }

    private fun determinerCategorie(imc: Double): Pair<String, Int> {
        return when {
            imc < 18.5 -> getString(R.string.cat_insuffisant) to getColor(R.color.imc_orange)
            imc < 25.0 -> getString(R.string.cat_normale) to getColor(R.color.imc_green)
            imc < 30.0 -> getString(R.string.cat_surpoids) to getColor(R.color.imc_orange)
            imc < 35.0 -> getString(R.string.cat_obesite_moderee) to getColor(R.color.imc_red)
            imc < 40.0 -> getString(R.string.cat_obesite_severe) to getColor(R.color.imc_red)
            else -> getString(R.string.cat_obesite_morbide) to getColor(R.color.imc_dark_red)
        }
    }

    private fun effacerChamps() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        textViewImc.text = ""
        textViewCategorie.text = ""
        editTextPoids.requestFocus()
    }
}