package com.example.roomdatabase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(db: AppDatabase){
    val noteDao = db.noteDao() // get the data access object // We open the door to the WeatherNote table this gives us all the methods : insert(), update(), delete(), getAllNotes()
    val notes by noteDao.getAllNotes().collectAsState(initial = emptyList())
    //.getAllNotes returns Flow<<list>> and collectAsState converts it to State
    //notes -> automatically updates the Ui when the database changes
    val scope = rememberCoroutineScope()
    //Dao methods (insert() etc) are suspend functions so we use coroutines
        Column(modifier = Modifier.padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(notes) { note ->
                    Text("${note.city}: ${note.note} — ${note.createdAt}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                scope.launch {
                    noteDao.insert(WeatherNote(city = "Oslo", note = "Sunny today"))
                }
            }) {
                Text("Add note")
            }
        }
}