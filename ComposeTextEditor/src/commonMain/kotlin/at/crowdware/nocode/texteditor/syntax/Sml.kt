package at.crowdware.nocode.texteditor.syntax

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.graphics.Color


class SmlSyntaxHighlighter(val colors: ExtendedColors) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Erstelle einen neuen Builder mit dem Text, aber ohne Annotationen
        val builder = AnnotatedString.Builder(text.text)

        // Highlight string values first (including those with colons and multiline strings)
        val stringRegex = Regex("\"(.|\\n)*?\"", RegexOption.DOT_MATCHES_ALL)
        stringRegex.findAll(text).forEach { match ->
            builder.addStyle(SpanStyle(color = colors.attributeValueColor), match.range.first, match.range.last + 1)
        }

        // Highlight SML elements (mit geschweifter Klammer auf derselben Zeile)
        val elementRegex = Regex("(\\w+)\\s*\\{")
        elementRegex.findAll(text).forEach { match ->
            // Check if this element name is not within a string
            if (!isWithinString(text, match.range.first)) {
                builder.addStyle(SpanStyle(color = colors.elementColor), match.range.first, match.range.last)
                builder.addStyle(SpanStyle(color = colors.bracketColor), match.range.last, match.range.last + 1)
            }
        }
        
        // Highlight Elementnamen (mit geschweifter Klammer auf der nächsten Zeile)
        // Suche nach Wörtern am Ende einer Zeile
        val lineEndRegex = Regex("(\\w+)\\s*$", RegexOption.MULTILINE)
        lineEndRegex.findAll(text).forEach { match ->
            // Check if this element name is not within a string
            if (!isWithinString(text, match.range.first)) {
                val elementName = match.groups[1]?.value ?: ""
                val elementNameStart = match.range.first
                val elementNameEnd = elementNameStart + elementName.length
                
                // Färbe alle Wörter am Ende einer Zeile ein
                builder.addStyle(SpanStyle(color = colors.elementColor), elementNameStart, elementNameEnd)
            }
        }

        // Highlight properties (excluding the colon)
        val propertyRegex = Regex("(\\w+)(?=\\s*:)")
        propertyRegex.findAll(text).forEach { match ->
            // Check if this property name is not within a string
            if (!isWithinString(text, match.range.first)) {
                builder.addStyle(SpanStyle(color = colors.attributeNameColor), match.range.first, match.range.last + 1)
            }
        }

        // Highlight colons and brackets
        val colonAndBracketRegex = Regex(":|[{}]")
        colonAndBracketRegex.findAll(text).forEach { match ->
            // Check if this colon or bracket is not within a string
            if (!isWithinString(text, match.range.first)) {
                builder.addStyle(SpanStyle(color = colors.bracketColor), match.range.first, match.range.last + 1)
            }
        }

        return TransformedText(builder.toAnnotatedString(), OffsetMapping.Identity)
    }

    private fun isWithinString(text: CharSequence, index: Int): Boolean {
        var inString = false
        for (i in 0 until index) {
            if (text[i] == '"') {
                inString = !inString
            }
        }
        return inString
    }
}
