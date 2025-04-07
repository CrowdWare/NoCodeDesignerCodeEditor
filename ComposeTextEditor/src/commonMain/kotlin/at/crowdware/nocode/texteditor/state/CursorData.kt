package at.crowdware.nocode.texteditor.state

import androidx.compose.ui.text.SpanStyle
import at.crowdware.nocode.texteditor.CharLineOffset
import at.crowdware.nocode.texteditor.TextEditorRange

data class CursorData(
	val position: CharLineOffset,
	val styles: Set<SpanStyle>,
	val selection: TextEditorRange?,
)
