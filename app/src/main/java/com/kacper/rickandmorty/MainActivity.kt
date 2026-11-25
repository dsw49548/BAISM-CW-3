package com.kacper.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kacper.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    var characters by remember { mutableStateOf<List<Character>>(emptyList()) }
    var character by remember { mutableStateOf<Character?>(null) }
    var idText by remember { mutableStateOf("1") }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            scope.launch {
                try {
                    error = null
                    characters = ApiClient.api.getCharacters().results
                } catch (e: Exception) {
                    error = e.message ?: e.toString()
                }
            }
        }) {
            Text("Lista postaci")
        }

        Spacer(Modifier.height(8.dp))

        Row {
            OutlinedTextField(
                value = idText,
                onValueChange = { idText = it },
                label = { Text("ID") },
                modifier = Modifier.width(100.dp)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        try {
                            error = null
                            character = ApiClient.api.getCharacter(idText.trim().toInt())
                        } catch (e: Exception) {
                            error = e.message ?: e.toString()
                        }
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Pobierz postać")
            }
        }

        error?.let {
            Text("Błąd: $it", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        character?.let {
            Column(Modifier.padding(top = 8.dp)) {
                Text("ID: ${it.id}")
                Text("Name: ${it.name}")
                Text("Status: ${it.status}")
                Text("Species: ${it.species}")
                Text("Gender: ${it.gender}")
            }
        }

        LazyColumn(Modifier.fillMaxWidth().padding(top = 8.dp)) {
            items(characters) {
                Text("${it.id}. ${it.name} - ${it.species}", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
