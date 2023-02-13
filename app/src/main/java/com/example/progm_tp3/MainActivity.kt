package com.example.progm_tp3

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.progm_tp3.ui.theme.Progmtp3Theme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Progmtp3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DefaultPreview()
                }
            }
        }
    }
}

/**
 * Saves a file with the given content and filename in the internal storage of the app.
 * @param content The text content to be written in the file.
 * @param filename The name of the file to be created or overwritten.
 * @param context The context of the app that calls this function.
 */
fun saveFile(content: String, filename: String, context: Context) {
    val file = File(
        context.filesDir,
        filename
    ) // Create a file object with the given filename in the app's internal storage directory.
    file.writeText(content) // Write the content to the file, overwriting any existing content.
}

/**
 * A composable function that creates a file with a given filename and some content when a button is clicked.
 * @param filename The name of the file to create.
 */
@Composable
fun CreateSimpleFile(filename: String) {
    val context = LocalContext.current

    val content = "Bonjour Johann Bourcier"
    Button(onClick = { saveFile(content, filename, context) }) {
        Text("Create a file with our names 😜")
    }
}

/**
 * This function takes a context and a filename as parameters and returns the content of the file as a string.
 * If the file does not exist, it returns "File not found" as a string.
 * @param context the context of the application
 * @param filename the name of the file to read
 * @return the content of the file or "File not found"
 */
fun fetchFileContent(context: Context, filename: String): String {
    val file = File(context.filesDir, filename)
    return if (file.exists()) {
        file.readText()
    } else {
        "File not found"
    }
}

/**
 * This function displays the content of a file and a button to fetch it.
 * @param filename the name of the file to display
 */
@Composable
fun FetchFileContent(filename: String) {
    val context = LocalContext.current
    var content by remember {
        mutableStateOf("Fetch to see the content")
    }
    Column {
        Text("Content: $content")
        Button(onClick = {
            content = fetchFileContent(context, filename)
        }) {
            Text("🔁 Fetch")
        }
    }
}

@Composable
fun Question1TO3() {
    val names = listOf("Romain BRIEND", "Mael KERICHARD")
    val fileName = names.joinToString("_")
    Column {
        CreateSimpleFile(fileName)
        FetchFileContent(fileName)
    }
}

@Composable
fun Question4() {
    val context = LocalContext.current
    val names = listOf("Romain BRIEND", "Mael KERICHARD")
    val fileName = names.joinToString("_")
    var content by remember {
        mutableStateOf(fetchFileContent(context, fileName))
    }
    Column {
        Text(text = "Question 4", style = MaterialTheme.typography.titleLarge)
        TextField(
            value = content,
            onValueChange = { content = it },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { content = fetchFileContent(context, fileName) }) {
                Text("Cancel")
            }
            Button(onClick = {
                saveFile(content, fileName, context)
                content = fetchFileContent(context, fileName)
            }) {
                Text("Ok")
            }
        }
    }
}

/**
 * This function returns a list of the names of the files in the context's files directory.
 * If the directory is empty or does not exist, it returns an empty list.
 *
 * @param context the context of the application
 * @return a list of file names or an empty list
 */
fun getFiles(context: Context): List<String> {
    return context.filesDir.listFiles()?.map { it.name } ?: listOf()
}

@Composable
fun Question5() {
    val context = LocalContext.current
    var fileName by remember {
        mutableStateOf("")
    }
    var files by remember {
        mutableStateOf(getFiles(context))
    }
    val content = "Bonjour Johann Bourcier"
    Column {
        Text(text = "Question 5", style = MaterialTheme.typography.titleLarge)
        TextField(
            value = fileName,
            onValueChange = { fileName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("File name") }
        )
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = {
                if (fileName.isEmpty()) {
                    Toast.makeText(context, "Please enter a file name", Toast.LENGTH_SHORT).show()

                } else {
                    saveFile(content, fileName, context)
                    fileName = ""
                    files = getFiles(context)
                    Toast.makeText(context, "File created", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Ok")
            }
        }
        LazyColumn {
            items(files) { name ->
                Row {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.clickable {
                            File(context.filesDir, name).delete()
                            files = getFiles(context)
                        }
                    )
                    Text(text = name)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Progmtp3Theme {
        Question5()

    }
}