package at.crowdware.nocode.texteditor.codeeditor

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import at.crowdware.nocode.texteditor.TextEditorStyle
import at.crowdware.nocode.texteditor.syntax.ExtendedColors

data class CodeEditorStyle(
	val baseStyle: TextEditorStyle,
	val gutterBackgroundColor: Color = Color.DarkGray,
	val gutterTextColor: Color = Color.White,
	val gutterStartPadding: Dp = 8.dp,
	val gutterEndPadding: Dp = 8.dp,
	val gutterEndMargin: Dp = 8.dp,
	var backgroundColor: Color = Color.DarkGray,
	val extendedColors: ExtendedColors = ExtendedColors(),
	val tabSize: Int = 4
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
	tabSize: Int = 4,
	extendedColors: ExtendedColors = ExtendedColors(
		syntaxColor = Color(0xFF61BEA6),
		bracketColor = Color(0xFFF5D52E),  // Gelb für geschweifte Klammern
		attributeValueColor = Color(0xFFBE896F),
		attributeNameColor = Color(0xFFA0D4FC),
		mdHeader = Color(0xFFB774B1),
		defaultTextColor = Color(0xFFB0B0B0),
		linkColor = Color(0xFF5C7AF1),
		elementColor = Color(0xFF61BEA6),    // Blau für SML-Elemente
		stringColor = Color(0xFFBE896F),  // Braun für Strings
		numberColor = Color(0xFF66CCFF)   // Hellblau für Zahlen
	)
): CodeEditorStyle = remember(
	textColor, backgroundColor, placeholderText, placeholderColor,
	cursorColor, selectionColor, focusedBorderColor, unfocusedBorderColor,
	gutterBackgroundColor, gutterTextColor,
	gutterStartPadding, gutterEndPadding, gutterEndMargin,
	tabSize, extendedColors
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
		extendedColors = extendedColors,
		tabSize = tabSize
	)
}
