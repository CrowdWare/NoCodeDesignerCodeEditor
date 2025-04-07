package at.crowdware.nocode.texteditor.state

import androidx.compose.ui.text.AnnotatedString
import at.crowdware.nocode.texteditor.richstyle.RichSpan
import at.crowdware.nocode.texteditor.richstyle.RichSpanStyle

// History manager for undo/redo support
class TextEditHistory(private val maxHistorySize: Int = 100) {
	private val undoQueue = ArrayDeque<HistoryEntry>(maxHistorySize)
	private val redoQueue = ArrayDeque<HistoryEntry>(maxHistorySize)

	fun hasUndoLevels(): Boolean = undoQueue.isNotEmpty()
	fun hasRedoLevels(): Boolean = redoQueue.isNotEmpty()

	fun recordEdit(operation: TextEditOperation, metadata: OperationMetadata) {
		if (undoQueue.size >= maxHistorySize) {
			undoQueue.removeFirstOrNull()
		}
		undoQueue.addLast(HistoryEntry(operation, metadata))
		redoQueue.clear() // Clear redo queue when new edit is made
	}

	fun undo(): HistoryEntry? {
		return undoQueue.removeLastOrNull()?.also { redoQueue.addLast(it) }
	}

	fun redo(): HistoryEntry? {
		return redoQueue.removeLastOrNull()?.also { undoQueue.addLast(it) }
	}

	fun clear() {
		undoQueue.clear()
		redoQueue.clear()
	}
}

data class RelativePosition(
	val lineDiff: Int,
	val char: Int
)

data class PreservedRichSpan(
	val relativeStart: RelativePosition,
	val relativeEnd: RelativePosition,
	val style: RichSpanStyle
)

data class OperationMetadata(
    val deletedText: AnnotatedString? = null,
    val deletedSpans: List<RichSpan> = emptyList(),
    val preservedRichSpans: List<PreservedRichSpan> = emptyList(),
)

data class HistoryEntry(
	val operation: TextEditOperation,
	val metadata: OperationMetadata
)