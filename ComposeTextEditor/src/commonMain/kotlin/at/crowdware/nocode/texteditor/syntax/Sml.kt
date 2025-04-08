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
                    i = length
                }
                builder.addStyle(
                    SpanStyle(color = colors.attributeValueColor),
                    start, i
                )
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
                    builder.addStyle(
                        SpanStyle(color = colors.attributeValueColor),
                        start, i
                    )
                }

                else if (c.isDigit()) {
                    val start = i
                    while (i < length && (raw[i].isDigit() || raw[i] == '.')) i++
                    builder.addStyle(
                        SpanStyle(color = colors.numberColor),
                        start, i
                    )
                }

                else if (c.isLetter()) {
                    val start = i
                    while (i < length && (raw[i].isLetterOrDigit() || raw[i] == '_')) i++
                    val after = raw.drop(i).takeWhile { it == ' ' || it == '\t' }
                    val nextChar = raw.getOrNull(i + after.length)

                    val isProperty = nextChar == ':'
                    val isElement = nextChar == '{' || isWordAloneOnLine(raw, start, i)

                    if (isProperty) {
                        builder.addStyle(
                            SpanStyle(color = colors.attributeNameColor),
                            start, i
                        )
                    } else if (isElement) {
                        builder.addStyle(
                            SpanStyle(color = colors.elementColor),
                            start, i
                        )
                    }
                }

                else if (c == ':' || c == '{' || c == '}') {
                    builder.addStyle(
                        SpanStyle(color = colors.bracketColor),
                        i, i + 1
                    )
                    i++
                }

                else {
                    i++
                }
            }
        }

        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }

    private fun isWordAloneOnLine(text: String, start: Int, end: Int): Boolean {
        val before = text.substring(0, start).takeLastWhile { it != '\n' }.trim()
        val after = text.substring(end).takeWhile { it != '\n' }.trim()
        return before.isEmpty() && after.isEmpty()
    }
}