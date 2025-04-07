package at.crowdware.nocode.texteditor.codeeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import at.crowdware.nocode.texteditor.TextEditorStyle

data class CodeEditorStyle(
	val baseStyle: TextEditorStyle,
	val gutterBackgroundColor: Color = Color.DarkGray,
	val gutterTextColor: Color = Color.White,
	val gutterStartPadding: Dp = 8.dp,
	val gutterEndPadding: Dp = 8.dp,
	val gutterEndMargin: Dp = 8.dp,
	var backgroundColor: Color = Color.DarkGray
)

@Composable
fun rememberCodeEditorStyle(
	textColor: Color = MaterialTheme.colorScheme.onSurface,
	backgroundColor: Color = MaterialTheme.colorScheme.background,
	placeholderText: String = "",
	placeholderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
	cursorColor: Color = MaterialTheme.colorScheme.onSurface,
	selectionColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
	focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
	unfocusedBorderColor: Color = MaterialTheme.colorScheme.outlineVariant,
	gutterBackgroundColor: Color = Color.DarkGray,
	gutterTextColor: Color = Color.White,
	gutterStartPadding: Dp = 8.dp,
	gutterEndPadding: Dp = 8.dp,
	gutterEndMargin: Dp = 8.dp,
): CodeEditorStyle = remember(
	textColor, backgroundColor, placeholderText, placeholderColor,
	cursorColor, selectionColor, focusedBorderColor, unfocusedBorderColor,
	gutterBackgroundColor, gutterTextColor,
	gutterStartPadding, gutterEndPadding, gutterEndMargin
) {
	CodeEditorStyle(
		baseStyle = TextEditorStyle(
			textColor = textColor,
			backgroundColor = backgroundColor,
			placeholderText = placeholderText,
			placeholderColor = placeholderColor,
			cursorColor = cursorColor,
			selectionColor = selectionColor,
			focusedBorderColor = focusedBorderColor,
			unfocusedBorderColor = unfocusedBorderColor,
			fontFamily = FontFamily.Monospace
		),
		gutterBackgroundColor = gutterBackgroundColor,
		gutterTextColor = gutterTextColor,
		gutterStartPadding = gutterStartPadding,
		gutterEndPadding = gutterEndPadding,
		gutterEndMargin = gutterEndMargin,
	)
}
