package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.Color


class SmlSyntaxHighlighter(val colors: ExtendedColors) : VisualTransformation {
    private var inString = false

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val builder = AnnotatedString.Builder(raw)
        val length = raw.length
        var i = 0

        while (i < length) {
            if (inString) {
                val start = i
                while (i < length && raw[i] != '"') i++
                if (i < length && raw[i] == '"') {
                    i++
                    inString = false
                } else {
                    i = length // kein Ende, bleibt offen
                }
                builder.addStyle(SpanStyle(color = colors.attributeValueColor), start, i)
            } else {
                val c = raw[i]
                if (c == '"') {
                    val start = i
                    i++
                    while (i < length && raw[i] != '"') i++
                    if (i < length && raw[i] == '"') {
                        i++
                    } else {
                        inString = true
                        i = length
                    }
                    builder.addStyle(SpanStyle(color = colors.attributeValueColor), start, i)
                } else {
                    i++
                }
            }
        }

        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }
}