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
import com.example.activityweek12.data.Record
import com.example.activityweek12.network.RetrofitClient
import com.example.activityweek12.ui.theme.ActivityWeek12Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val scope = rememberCoroutineScope()

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
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Conectado a API Local",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Botón Submit
        Button(
            onClick = {
                if (nombre.text.isBlank() || email.text.isBlank()) {
                    Toast.makeText(
                        context,
                        "Por favor, complete todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                isLoading = true

                scope.launch {
                    try {
                        val record = Record(
                            nombre = nombre.text.trim(),
                            email = email.text.trim().lowercase()
                        )

                        val response = withContext(Dispatchers.IO) {
                            RetrofitClient.apiService.addRecord(record)
                        }

                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "✅ Datos guardados correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                                nombre = TextFieldValue("")
                                email = TextFieldValue("")
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    "❌ Error: ${response.code()} - ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                context,
                                "❌ Error de conexión: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } finally {
                        isLoading = false
                    }
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
                Text("Guardar en API Local")
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
