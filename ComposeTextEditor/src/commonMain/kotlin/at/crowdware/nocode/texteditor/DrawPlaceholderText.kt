package at.crowdware.nocode.texteditor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import at.crowdware.nocode.texteditor.state.TextEditorState

internal fun DrawScope.DrawPlaceholderText(
	state: TextEditorState,
	style: TextEditorStyle
) {
	drawText(
		textMeasurer = state.textMeasurer,
		text = style.placeholderText,
		style = TextStyle.Default.copy(
			color = style.placeholderColor,
			fontFamily = FontFamily.Monospace
		),
		topLeft = Offset(0f, 0f)
	)
}
