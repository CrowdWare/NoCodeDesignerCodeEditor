package at.crowdware.nocode.texteditor.codeeditor

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import at.crowdware.nocode.texteditor.BasicTextEditor
import at.crowdware.nocode.texteditor.RichSpanClickListener
import at.crowdware.nocode.texteditor.TextEditorStyle
import at.crowdware.nocode.texteditor.focusBorder
import at.crowdware.nocode.texteditor.state.TextEditorState
import at.crowdware.nocode.texteditor.state.rememberTextEditorState
import at.crowdware.nocode.texteditor.syntax.MarkdownSyntaxHighlighter
import at.crowdware.nocode.texteditor.syntax.SmlSyntaxHighlighter
import at.crowdware.nocode.texteditor.syntax.SyntaxMode
import kotlin.math.absoluteValue
import kotlin.math.log10


private fun numDigits(number: Int): Int {
	if (number == 0) return 1
	return log10(number.absoluteValue.toFloat()).toInt() + 1
}

private fun calculateColWidth(state: TextEditorState, density: Density): Dp {
	return with(density) {
		state.textMeasurer.measure("0").size.width.toDp()
	}
}

private fun gutterWidth(state: TextEditorState, style: CodeEditorStyle, colWidth: Dp): Dp {
	val numLines = state.textLines.size

	val maxLineNumber = numLines.coerceAtLeast(1)
	val numDigits = maxOf(numDigits(maxLineNumber), 2)

	return style.gutterStartPadding + (colWidth * numDigits) + style.gutterEndPadding
}

private fun DrawScope.drawLineNumbers(
	line: Int,
	offset: Offset,
	state: TextEditorState,
	style: CodeEditorStyle,
	gutterWidth: Dp
) {
	val lineNumberText = (line + 1).toString()

	val textWidth = state.textMeasurer.measure(text = lineNumberText).size.width

	val gutterRightEdge = offset.x - style.gutterEndMargin.toPx()
	val x = gutterRightEdge - textWidth - style.gutterEndPadding.toPx()

	val gutterLeftEdge = gutterRightEdge - gutterWidth.toPx()
	val constrainedX = x.coerceAtLeast(gutterLeftEdge + style.gutterStartPadding.toPx())
	val lineNumberOffset = offset.copy(x = constrainedX)

	drawText(
		textMeasurer = state.textMeasurer,
		text = lineNumberText,
		style = TextStyle.Default.copy(
			color = style.gutterTextColor,
			fontFamily = style.baseStyle.fontFamily
		),
		topLeft = lineNumberOffset
	)
}

@Composable
fun CodeEditor(
	state: TextEditorState = rememberTextEditorState(),
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	style: CodeEditorStyle = rememberCodeEditorStyle(),
	onRichSpanClick: RichSpanClickListener? = null,
	syntaxMode: SyntaxMode = SyntaxMode.NONE,
) {
	// Set the font family for the TextEditorState to Monospace
	state.fontFamily = FontFamily.Monospace
	val density = LocalDensity.current

	val colWidth by remember(state.textMeasurer, density) {
		derivedStateOf { calculateColWidth(state, density) }
	}

	val gutterWidth by remember(state.lineOffsets, style, colWidth) {
		derivedStateOf { gutterWidth(state, style, colWidth) }
	}
	
	// Erstelle den passenden Syntax-Highlighter basierend auf dem syntaxMode
	// Nicht remember verwenden, damit der Highlighter bei jeder Komposition neu erstellt wird
	val syntaxHighlighter = when (syntaxMode) {
		SyntaxMode.SML -> SmlSyntaxHighlighter(style.extendedColors)
		SyntaxMode.MARKDOWN -> MarkdownSyntaxHighlighter(style.extendedColors)
		SyntaxMode.NONE -> null
	}

	Surface(modifier = modifier.focusBorder(state.isFocused && enabled, style.baseStyle)) {
		BasicTextEditor(
			state = state,
			modifier = Modifier
				.padding(start = gutterWidth + style.gutterEndMargin)
				.graphicsLayer { clip = false }
				.drawBehind {
					drawRect(
						color = style.gutterBackgroundColor,
						topLeft = Offset(-(gutterWidth.toPx() + style.gutterEndMargin.toPx()), 0f),
						size = Size(gutterWidth.toPx() + 8, size.height)
					)
				},
			enabled = enabled,
			style = style.baseStyle,
			onRichSpanClick = onRichSpanClick,
			decorateLine = { line: Int, offset: Offset, state: TextEditorState, _: TextEditorStyle ->
				drawLineNumbers(line, offset, state, style, gutterWidth)
			},
			textTransformer = { text ->
				// Wende den Syntax-Highlighter an, falls einer definiert ist
				syntaxHighlighter?.filter(text) ?: TransformedText(text, androidx.compose.ui.text.input.OffsetMapping.Identity)
			}
		)
	}
}
