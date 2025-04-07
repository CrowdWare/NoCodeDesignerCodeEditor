package at.crowdware.nocode.texteditor.scrollbar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.crowdware.nocode.texteditor.state.TextEditorScrollState

@Composable
expect fun TextEditorScrollbar(
    modifier: Modifier = Modifier,
    scrollState: TextEditorScrollState,
    content: @Composable (modifier: Modifier) -> Unit,
)