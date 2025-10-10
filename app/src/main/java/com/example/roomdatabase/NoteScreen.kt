package com.example.roomdatabase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


@Composable
fun NoteScreen(db: AppDatabase) {
    val noteDao =
        db.noteDao() // get the data access object // We open the door to the WeatherNote table this gives us all the methods : insert(), update(), delete(), getAllNotes()
    val notes by noteDao.getAllNotes().collectAsState(initial = emptyList())
    //.getAllNotes returns Flow<<list>> and collectAsState converts it to State
    //notes -> automatically updates the Ui when the database changes
    val scope = rememberCoroutineScope()
    //Dao methods (insert() etc) are suspend functions so we use coroutines


    // User inputs
    var cityInput by remember { mutableStateOf("") }
    var noteInput by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.padding(top = 24.dp))
            Text(
                text = "Weather Notes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            //City Name
            OutlinedTextField(
                value = cityInput,
                onValueChange = { cityInput = it },
                label = { Text("City") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Note
            OutlinedTextField(
                value = noteInput,
                onValueChange = { noteInput = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            // Notes list
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 88.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = note.city,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = note.note,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = formatTimestamp(note.createdAt),
                                style = MaterialTheme.typography.bodySmall
                            )

                            IconButton(
                                onClick = {
                                    scope.launch {
                                        noteDao.delete(note) // delete from database
                                    }
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Delete Note", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
        FloatingActionButton(
            onClick = {
                scope.launch {
                    if (cityInput.isNotBlank() && noteInput.isNotBlank()) {
                        noteDao.insert(
                            WeatherNote(
                                city = cityInput.trim(),
                                note = noteInput.trim()
                            )
                        )
                        // Clear the fields after saving
                        cityInput = ""
                        noteInput = ""
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}