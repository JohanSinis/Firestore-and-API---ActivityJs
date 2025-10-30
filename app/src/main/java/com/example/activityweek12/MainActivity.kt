package com.example.activityweek12

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.example.activityweek12.ui.theme.ActivityWeek12Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ActivityWeek12Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NameGradeForm()
                }
            }
        }
    }
}

@Composable
fun NameGradeForm() {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuarios", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón Submit
        Button(
            onClick = {
                if (nombre.text.isBlank() || email.text.isBlank()) {
                    Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true

                val record = hashMapOf(
                    "nombre" to nombre.text.trim(),
                    "email" to email.text.trim().lowercase()
                )

                db.collection("records")
                    .add(record)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Datos guardados en Firebase", Toast.LENGTH_SHORT).show()
                        nombre = TextFieldValue("")
                        email = TextFieldValue("")
                        isLoading = false
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForm() {
    ActivityWeek12Theme {
        NameGradeForm()
    }
}
