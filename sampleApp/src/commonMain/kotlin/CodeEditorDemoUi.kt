import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import at.crowdware.nocode.texteditor.codeeditor.CodeEditor
import at.crowdware.nocode.texteditor.codeeditor.rememberCodeEditorStyle
import at.crowdware.nocode.texteditor.state.TextEditorState
import at.crowdware.nocode.texteditor.state.rememberTextEditorState
import at.crowdware.nocode.texteditor.syntax.SyntaxMode

@Composable
fun CodeEditorDemoUi(
	modifier: Modifier = Modifier,
	navigateTo: (Destination) -> Unit,
) {
	// Zustand für den aktuellen Syntax-Modus
	val syntaxMode = remember { mutableStateOf(SyntaxMode.NONE) }
	
	// Zustand für den aktuellen Beispielcode
	val currentSample = remember { mutableStateOf(SAMPLE_CODE_KOTLIN) }
	
	// TextEditorState mit dem aktuellen Beispielcode
	val state: TextEditorState = rememberTextEditorState(AnnotatedString(currentSample.value))

	LaunchedEffect(Unit) {
		//state.editOperations.collect { operation ->
		//	println("Applying Operation: $operation")
		//}
	}

	Column(modifier = modifier) {
		Row {
			Text(
				"Code Editor",
				modifier = Modifier.padding(8.dp),
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold
			)
			Spacer(modifier = Modifier.weight(1f))
			Button(onClick = { navigateTo(Destination.Menu) }) {
				Text("X")
			}
		}
		
		// Buttons zum Umschalten des Syntax-Modus
		Row(
			modifier = Modifier.fillMaxWidth().padding(8.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text("Syntax-Modus:")
			Button(onClick = { 
				syntaxMode.value = SyntaxMode.NONE
				currentSample.value = SAMPLE_CODE_KOTLIN
				state.setText(currentSample.value)
			}) {
				Text("Keine")
			}
			Button(onClick = { 
				syntaxMode.value = SyntaxMode.SML
				currentSample.value = SAMPLE_CODE_SML
				state.setText(currentSample.value)
			}) {
				Text("SML")
			}
			Button(onClick = { 
				syntaxMode.value = SyntaxMode.MARKDOWN
				currentSample.value = SAMPLE_CODE_MARKDOWN
				state.setText(currentSample.value)
			}) {
				Text("Markdown")
			}
		}

		val style = rememberCodeEditorStyle(
			placeholderText = "Enter code here",
		)

		CodeEditor(
			state = state,
			modifier = Modifier.fillMaxSize(),
			style = style,
			syntaxMode = syntaxMode.value, // Übergebe den aktuellen Syntax-Modus
		)
	}
}

// Beispielcode für Kotlin (kein spezielles Highlighting)
private val SAMPLE_CODE_KOTLIN = """
fun main() {
    println("Hello, Code Editor!")

    // This is a sample code
    for (i in 1..10) {
        println("Count: ${'$'}i")
    }

    val message = "Welcome to the Code Editor"
    println(message)
}
""".trim()

// Beispielcode für SML
private val SAMPLE_CODE_SML = """
Page
{
    width: 200
    height: 100
    color: "blue"
    page: "about"
    
    Markdown 
	{
        text: "# Titel\n" +
              "Lorem ipsum **dolor**\n" +
              "Noch eine Zeile\n"
    }
    Spacer 
	{ amount: 16 }
    Button 
	{
        text: "Click me"
        onClicked: "page:about"
        width: 120
        height: 40
    }
}
""".trim()

// Beispielcode für Markdown
private val SAMPLE_CODE_MARKDOWN = """
# Markdown Beispiel

Dies ist ein **fetter Text** und *kursiver Text*.

## Unterüberschrift

- Listenpunkt 1
- Listenpunkt 2

[Ein Link](https://example.com)

```
// Ein Codeblock
function helloWorld() {
    console.log("Hello World!");
}
```

<div class="custom">HTML wird auch unterstützt</div>
""".trim()
