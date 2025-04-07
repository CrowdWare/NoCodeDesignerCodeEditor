package at.crowdware.nocode.texteditor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextLayoutResult
import at.crowdware.nocode.texteditor.richstyle.RichSpan
import at.crowdware.nocode.texteditor.state.TextEditorState

data class LineWrap(
    val line: Int,
	// The character index of the first character on this line
    val wrapStartsAtIndex: Int,
    val virtualLength: Int,
    val virtualLineIndex: Int,
    val offset: Offset,
    val textLayoutResult: TextLayoutResult,
    val richSpans: List<RichSpan> = emptyList(),
)

fun LineWrap.wrapStartToCharacterIndex(state: TextEditorState): Int {
	return state.wrapStartToCharacterIndex(this)
}