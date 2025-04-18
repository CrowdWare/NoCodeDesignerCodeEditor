package at.crowdware.nocode.texteditor.cursor

import androidx.compose.ui.geometry.Offset
import at.crowdware.nocode.texteditor.CharLineOffset
import at.crowdware.nocode.texteditor.LineWrap
import at.crowdware.nocode.texteditor.state.TextEditorState

/*
fun TextEditorState.calculateCursorPosition(): CursorMetrics {
	val (_, charIndex) = cursorPosition

	val currentWrappedLineIndex = lineOffsets.getWrappedLineIndex(cursorPosition)
	val currentWrappedLine = lineOffsets[currentWrappedLineIndex]
	val startOfLineOffset = lineOffsets.first { it.line == currentWrappedLine.line }.offset

	val layout = currentWrappedLine.textLayoutResult

	val cursorX = layout.getHorizontalPosition(charIndex, usePrimaryDirection = true)
	val cursorY =
		startOfLineOffset.y + layout.multiParagraph.getLineTop(currentWrappedLine.virtualLineIndex) - scrollState.value
	val lineHeight = layout.multiParagraph.getLineHeight(currentWrappedLine.virtualLineIndex)

	return CursorMetrics(
		position = Offset(cursorX, cursorY),
		height = lineHeight
	)
}*/

fun TextEditorState.calculateCursorPosition(): CursorMetrics {
	val (_, charIndex) = cursorPosition

	val currentWrappedLineIndex = lineOffsets.getWrappedLineIndex(cursorPosition)
	if (currentWrappedLineIndex !in lineOffsets.indices) {
		// return dummy cursor metrics or handle gracefully
		return CursorMetrics(
			position = Offset.Zero,
			height = 0f
		)
	}
	val currentWrappedLine = lineOffsets[currentWrappedLineIndex]
	val startOfLineOffset = lineOffsets.first { it.line == currentWrappedLine.line }.offset

	val layout = currentWrappedLine.textLayoutResult

	// ⛑ Bounds check to avoid crash
	val safeCharIndex = charIndex.coerceIn(0, layout.layoutInput.text.length)

	val cursorX = layout.getHorizontalPosition(safeCharIndex, usePrimaryDirection = true)
	val cursorY =
		startOfLineOffset.y + layout.multiParagraph.getLineTop(currentWrappedLine.virtualLineIndex) - scrollState.value
	val lineHeight = layout.multiParagraph.getLineHeight(currentWrappedLine.virtualLineIndex)

	return CursorMetrics(
		position = Offset(cursorX, cursorY),
		height = lineHeight
	)
}

internal fun List<LineWrap>.getWrappedLineIndex(position: CharLineOffset): Int {
	return indexOfLast { lineOffset ->
		lineOffset.line == position.line && lineOffset.wrapStartsAtIndex <= position.char
	}
}

private fun List<LineWrap>.getWrappedLine(position: CharLineOffset): LineWrap {
	return last { lineOffset ->
		lineOffset.line == position.line && lineOffset.wrapStartsAtIndex <= position.char
	}
}