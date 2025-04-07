package at.crowdware.nocode.texteditor.richstyle


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import at.crowdware.nocode.texteditor.LineWrap

class HighlightSpanStyle(
	private val color: Color
) : RichSpanStyle {
	override fun DrawScope.drawCustomStyle(
		layoutResult: TextLayoutResult,
		lineWrap: LineWrap,
		textRange: TextRange
	) {
		val lineHeight = layoutResult.multiParagraph.getLineHeight(lineWrap.virtualLineIndex)

		val lineStartOffset = layoutResult.getLineStart(lineWrap.virtualLineIndex)
		val startX = if (textRange.start <= lineStartOffset) {
			layoutResult.getLineLeft(lineWrap.virtualLineIndex)
		} else {
			try {
				layoutResult.getHorizontalPosition(textRange.start, usePrimaryDirection = true)
			} catch (e: Exception) {
				error(e)
			}
		}

		val lineEndOffset = layoutResult.getLineEnd(lineWrap.virtualLineIndex, false)
		val endX = if (textRange.end >= lineEndOffset) {
			layoutResult.getLineRight(lineWrap.virtualLineIndex)
		} else {
			layoutResult.getHorizontalPosition(textRange.end, usePrimaryDirection = true)
		}

		drawRect(
			color = color,
			topLeft = Offset(x = startX, y = 0f),
			size = Size(width = endX - startX, height = lineHeight)
		)
	}
}

