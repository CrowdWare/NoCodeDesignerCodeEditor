package at.crowdware.nocode.texteditor

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import at.crowdware.nocode.texteditor.state.TextEditorState

internal typealias LineDecorator = DrawScope.(
	line: Int,
	offset: Offset,
	state: TextEditorState,
	style: TextEditorStyle
) -> Unit