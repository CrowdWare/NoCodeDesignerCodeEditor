package at.crowdware.nocode.texteditor.state

import at.crowdware.nocode.texteditor.TextEditorRange

data class WordSegment(
	val text: String,
	val range: TextEditorRange,
) {
	override fun toString(): String {
		return text + " (${range.start}-${range.end})"
	}
}