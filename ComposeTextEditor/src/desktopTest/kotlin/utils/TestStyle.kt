package utils

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import at.crowdware.nocode.texteditor.LineWrap
import at.crowdware.nocode.texteditor.richstyle.RichSpanStyle

internal class TestStyle : RichSpanStyle {
	override fun DrawScope.drawCustomStyle(
		layoutResult: TextLayoutResult,
		lineWrap: LineWrap,
		textRange: TextRange
	) {
		// No-op for testing
	}
}